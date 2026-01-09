package client.scenes;

import commons.Recipe;

public final class RecipeHolder {

    private Recipe selectedRecipe;
    private final static RecipeHolder INSTANCE = new RecipeHolder();

    private RecipeHolder() {}

    public static RecipeHolder getInstance() {
        return INSTANCE;
    }

    public void setRecipe(Recipe r) {
        this.selectedRecipe = r;
    }

    public Recipe getRecipe() {
        return this.selectedRecipe;
    }
}
