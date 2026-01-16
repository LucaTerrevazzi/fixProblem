package server.ws;

public class RecipeEvent {

    public enum Type {
        RECIPE_ADDED,
        RECIPE_DELETED,
        RECIPE_TITLE_UPDATED,
        RECIPE_UPDATED
    }

    public Type type;
    public long id;

    // for list updates
    public String title;

    // sparkle/basic conflict friendliness
    public long version;

    // optional: for RECIPE_UPDATED to keep payload tiny
    public String[] changedFields;

    public RecipeEvent() {}

    public static RecipeEvent added(long id, String title) {
        var e = new RecipeEvent();
        e.type = Type.RECIPE_ADDED;
        e.id = id;
        e.title = title;
        return e;
    }

    public static RecipeEvent deleted(long id) {
        var e = new RecipeEvent();
        e.type = Type.RECIPE_DELETED;
        e.id = id;
        return e;
    }

    public static RecipeEvent titleUpdated(long id, String title) {
        var e = new RecipeEvent();
        e.type = Type.RECIPE_TITLE_UPDATED;
        e.id = id;
        e.title = title;
        return e;
    }

    public static RecipeEvent updated(long id, long version, String... changedFields) {
        var e = new RecipeEvent();
        e.type = Type.RECIPE_UPDATED;
        e.id = id;
        e.version = version;
        e.changedFields = changedFields;
        return e;
    }
}
