package client.ws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeEvent {

    public enum Type {
        RECIPE_ADDED,
        RECIPE_DELETED,
        RECIPE_TITLE_UPDATED,
        RECIPE_UPDATED
    }
    public Type type;
    public long id;
    public String title;
    public long version;
    public String[] changedFields;
    public RecipeEvent() {}
}
