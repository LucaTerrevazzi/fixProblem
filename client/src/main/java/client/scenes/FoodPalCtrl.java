package client.scenes;

import commons.Recipe;
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
    private Scene downloadRecipeScene;
    private Scene editRecipeScene;
    private EditRecipeCtrl editRecipeCtrl;

    private RecipeOverviewCtrl recipeOverviewCtrl;
    public void init(Stage primaryStage, Pair<RecipeOverviewCtrl, Parent> overview,
                     Pair<AddRecipeCtrl, Parent> addRecipe,
                     Pair<IngredientOverviewCtrl, Parent> ingredientOverview,
                     Pair<AddIngredientCtrl, Parent> addIngredient,
                     Pair<EditRecipeCtrl, Parent> editRecipe,
                     Pair<DownloadRecipeCtrl, Parent> downloadRecipe) {
        this.primaryStage = primaryStage;
        this.editRecipeCtrl = editRecipe.getKey();
        this.recipeOverviewScene = new Scene(overview.getValue());
        this.addRecipeScene = new Scene(addRecipe.getValue());
        this.ingredientOverviewScene = new Scene(ingredientOverview.getValue());
        this.addIngredientScene = new Scene(addIngredient.getValue());
        this.downloadRecipeScene = new Scene(downloadRecipe.getValue());
        this.editRecipeScene = new Scene(editRecipe.getValue());
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

    public void showDownloadRecipe(Recipe recipe) {
        primaryStage.setTitle("Download Recipe");
        primaryStage.setScene(downloadRecipeScene);
    }

    public void showIngredientOverview() {
        primaryStage.setTitle("Ingredient Overview");
        primaryStage.setScene(ingredientOverviewScene);
    }

    public void showAddIngredient() {
        primaryStage.setTitle("Add Ingredient");
        primaryStage.setScene(addIngredientScene);
    }

    public void showEditRecipe(Recipe recipe) {
        editRecipeCtrl.setRecipeToEdit(recipe);
        primaryStage.setTitle("Edit Recipe");
        primaryStage.setScene(editRecipeScene);
    }
}

