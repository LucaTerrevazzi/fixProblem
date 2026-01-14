package client.utils;

import commons.Recipe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public final class FavoritesStorage {
    private static final String APP_DIR = ".foodpal";
    private static final String FAVORITES_FILE = "favorites.json";
    private static boolean configured = false;

    private FavoritesStorage() {
    }

    public static synchronized void configure() throws IOException {
        if (configured) {
            return;
        }
        Path dir = Paths.get(System.getProperty("user.home"), APP_DIR);
        Files.createDirectories(dir);
        Path file = dir.resolve(FAVORITES_FILE);
        FavoritesUtil.setFilePath(file.toString());
        configured = true;
    }

    public static FavoritesData loadFavorites() throws IOException {
        configure();
        return FavoritesUtil.loadJson();
    }

    public static Set<Long> loadFavoriteIds() throws IOException {
        return loadFavorites().getFavorites().stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    public static void addFavorite(Recipe recipe) throws IOException {
        configure();
        FavoritesUtil.addFavorite(recipe);
    }

    public static void removeFavorite(Recipe recipe) throws IOException {
        configure();
        FavoritesUtil.removeFavorite(recipe);
    }
}
