package client.utils;

import client.ws.RecipeEvent;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class WsClient {

    public enum Status { DISCONNECTED, CONNECTING, CONNECTED, RECONNECTING }

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "ws-reconnect"));

    private final AtomicBoolean manualDisconnect = new AtomicBoolean(false);

    private final Map<String, StompSession.Subscription> subscriptions = new ConcurrentHashMap<>();
    private final Set<String> desiredSubscriptions = ConcurrentHashMap.newKeySet();
    private final Map<String, Consumer<RecipeEvent>> handlers = new ConcurrentHashMap<>();
    private volatile String lastServerBaseUrl = null;
    private final AtomicBoolean reconnectScheduled = new AtomicBoolean(false);
    private volatile int reconnectDelayMs = 800;
    private static final int RECONNECT_MAX_DELAY_MS = 8000;


    private final WebSocketStompClient stompClient;

    private volatile StompSession session;
    private volatile Status status = Status.DISCONNECTED;

    private final CopyOnWriteArrayList<Consumer<Status>> statusListeners = new CopyOnWriteArrayList<>();

    public WsClient() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        scheduler.scheduleAtFixedRate(() -> {
            if (manualDisconnect.get()) return;
            if (lastServerBaseUrl == null) return;

            boolean connected = (session != null && session.isConnected());
            if (!connected) {
                scheduleReconnect(lastServerBaseUrl);
            }
        }, 2, 2, TimeUnit.SECONDS);

    }


    public void addStatusListener(Consumer<Status> listener) {
        if (listener != null) statusListeners.add(listener);
    }

    private void setStatus(Status s) {
        status = s;
        Platform.runLater(() -> {
            for (var l : statusListeners) l.accept(s);
        });
    }

    public void connect(String serverBaseUrl) {
        manualDisconnect.set(false);
        lastServerBaseUrl = serverBaseUrl;

        String wsUrl = toWsUrl(serverBaseUrl) + "ws";

        if (session != null && session.isConnected()) return;

        setStatus((status == Status.RECONNECTING) ? Status.RECONNECTING : Status.CONNECTING);

        CompletableFuture<StompSession> future =
                stompClient.connectAsync(wsUrl, new StompSessionHandlerAdapter() {

                    @Override
                    public void afterConnected(StompSession sess, StompHeaders connectedHeaders) {
                        session = sess;
                        reconnectDelayMs = 800;
                        reconnectScheduled.set(false);
                        setStatus(Status.CONNECTED);
                        resubscribeAllStored();
                    }

                    @Override
                    public void handleTransportError(StompSession sess, Throwable exception) {
                        session = null; // IMPORTANT: mark dead
                        if (!manualDisconnect.get()) scheduleReconnect(serverBaseUrl);
                    }
                });
        future.orTimeout(2, TimeUnit.SECONDS)
                .whenComplete((sess, ex) -> {
                    if (ex != null) {
                        session = null;
                        if (!manualDisconnect.get()) scheduleReconnect(serverBaseUrl);
                    }
                });
    }


    public void disconnect() {
        manualDisconnect.set(true);
        try {
            subscriptions.values().forEach(s -> {
                try { s.unsubscribe(); } catch (Exception ignored) {}
            });
            subscriptions.clear();
            desiredSubscriptions.clear();
            handlers.clear();
            if (session != null) session.disconnect();
        } catch (Exception ignored) {}
        session = null;
        setStatus(Status.DISCONNECTED);
    }
    public void subscribeRecipeListStored(Consumer<RecipeEvent> onEvent) {
        subscribeStored("/topic/recipes", onEvent);
    }

    public void subscribeRecipeStored(long id, Consumer<RecipeEvent> onEvent) {
        subscribeStored("/topic/recipes/" + id, onEvent);
    }

    public void unsubscribeRecipe(long id) {
        unsubscribe("/topic/recipes/" + id);
    }

    public void unsubscribeRecipeList() {
        unsubscribe("/topic/recipes");
    }

    private void subscribeStored(String destination, Consumer<RecipeEvent> onEvent) {
        desiredSubscriptions.add(destination);
        handlers.put(destination, onEvent);
        subscribeNowIfConnected(destination, onEvent);
    }

    private void subscribeNowIfConnected(String destination, Consumer<RecipeEvent> onEvent) {
        if (session == null || !session.isConnected()) return;
        if (subscriptions.containsKey(destination)) return;

        StompSession.Subscription sub = session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RecipeEvent.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                RecipeEvent ev = (RecipeEvent) payload;
                Platform.runLater(() -> onEvent.accept(ev));
            }
        });

        subscriptions.put(destination, sub);
    }

    private void unsubscribe(String destination) {
        desiredSubscriptions.remove(destination);
        handlers.remove(destination);

        StompSession.Subscription sub = subscriptions.remove(destination);
        if (sub != null) {
            try { sub.unsubscribe(); } catch (Exception ignored) {}
        }
    }

    private void resubscribeAllStored() {
        subscriptions.clear();
        for (String dest : desiredSubscriptions) {
            Consumer<RecipeEvent> h = handlers.get(dest);
            if (h != null) subscribeNowIfConnected(dest, h);
        }
    }

    private void scheduleReconnect(String serverBaseUrl) {
        if (manualDisconnect.get()) return;
        if (!reconnectScheduled.compareAndSet(false, true)) return;

        setStatus(Status.RECONNECTING);

        int delay = reconnectDelayMs;

        scheduler.schedule(() -> {
            if (manualDisconnect.get()) return;
            reconnectScheduled.set(false);

            try {
                connect(serverBaseUrl);
            } catch (Exception ignored) {}
            if (session == null || !session.isConnected()) {
                reconnectDelayMs = Math.min(reconnectDelayMs * 2, RECONNECT_MAX_DELAY_MS);
                scheduleReconnect(serverBaseUrl);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }


    private static String toWsUrl(String httpBase) {
        String trimmed = httpBase.trim();
        if (!trimmed.endsWith("/")) trimmed += "/";
        if (trimmed.startsWith("https://")) return "wss://" + trimmed.substring("https://".length());
        if (trimmed.startsWith("http://")) return "ws://" + trimmed.substring("http://".length());
        return trimmed;
    }
}
