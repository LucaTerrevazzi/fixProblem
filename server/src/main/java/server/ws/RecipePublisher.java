package server.ws;

import commons.Recipe;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RecipePublisher {

    private final SimpMessagingTemplate messaging;

    public RecipePublisher(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }
    public void publishList(RecipeEvent event) {
        messaging.convertAndSend("/topic/recipes", event);
    }
    public void publishRecipe(long id, RecipeEvent event) {
        messaging.convertAndSend("/topic/recipes/" + id, event);
    }
    public void recipeAdded(Recipe r) {
        publishList(RecipeEvent.added(r.getRecipeID(), r.getRecipeName()));
    }

    public void recipeDeleted(long id) {
        publishList(RecipeEvent.deleted(id));
    }

    public void recipeTitleUpdated(long id, String title) {
        publishList(RecipeEvent.titleUpdated(id, title));
    }

    public void recipeUpdated(long id, long version, String... changedFields) {
        publishRecipe(id, RecipeEvent.updated(id, version, changedFields));
    }
}
