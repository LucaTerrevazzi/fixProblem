package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class FoodPalCtrl {

    private Stage primaryStage;
    private Scene recipeOverviewScene;
    private Scene addRecipeScene;
    private Scene ingredientOverviewScene;
    private Scene addIngredientScene;
    private RecipeOverviewCtrl recipeOverviewCtrl;
    public void init(Stage primaryStage, Pair<RecipeOverviewCtrl, Parent> overview,
                     Pair<AddRecipeCtrl, Parent> addRecipe,
                     Pair<IngredientOverviewCtrl, Parent> ingredientOverview,
                     Pair<AddIngredientCtrl, Parent> addIngredient) {
        this.primaryStage = primaryStage;
        this.recipeOverviewScene = new Scene(overview.getValue());
        this.addRecipeScene = new Scene(addRecipe.getValue());
        this.ingredientOverviewScene = new Scene(ingredientOverview.getValue());
        this.addIngredientScene = new Scene(addIngredient.getValue());
        showRecipeOverview();
        primaryStage.show();
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
    }

    public void showAddRecipe() {
        primaryStage.setTitle("Add Recipe");
        primaryStage.setScene(addRecipeScene);
    }

    public void showIngredientOverview() {
        primaryStage.setTitle("Ingredient Overview");
        primaryStage.setScene(ingredientOverviewScene);
    }

    public void showAddIngredient() {
        primaryStage.setTitle("Add Ingredient");
        primaryStage.setScene(addIngredientScene);
    }
}

