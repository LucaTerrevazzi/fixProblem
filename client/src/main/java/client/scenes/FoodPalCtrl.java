package client.scenes;

import client.utils.RecipeUtil;
import commons.Ingredient;
import commons.Recipe;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class FoodPalCtrl {

    private Stage primaryStage;
    private Scene recipeOverviewScene;
    private RecipeOverviewCtrl recipeOverviewCtrl;
    private Scene addRecipeScene;
    private AddRecipeCtrl addRecipeCtrl;
    private Scene ingredientOverviewScene;
    private IngredientOverviewCtrl ingredientOverviewCtrl;
    private Scene addIngredientScene;
    private Scene downloadRecipeScene;
    private DownloadRecipeCtrl downloadRecipeCtrl;
    private Scene editRecipeScene;
    private EditRecipeCtrl editRecipeCtrl;
    private EditIngredientCtrl editIngredientCtrl;
    private Scene editIngredientScene;

    public void init(Stage primaryStage, Pair<RecipeOverviewCtrl, Parent> overview,
                     Pair<AddRecipeCtrl, Parent> addRecipe,
                     Pair<IngredientOverviewCtrl, Parent> ingredientOverview,
                     Pair<AddIngredientCtrl, Parent> addIngredient,
                     Pair<EditRecipeCtrl, Parent> editRecipe,
                     Pair<DownloadRecipeCtrl, Parent> downloadRecipe,
                     Pair<EditIngredientCtrl, Parent> editIngredient) {
        this.primaryStage = primaryStage;
        this.editRecipeCtrl = editRecipe.getKey();
        this.recipeOverviewScene = new Scene(overview.getValue());
        this.recipeOverviewCtrl = overview.getKey();
        this.addRecipeScene = new Scene(addRecipe.getValue());
        this.addRecipeCtrl = addRecipe.getKey();
        this.ingredientOverviewScene = new Scene(ingredientOverview.getValue());
        this.ingredientOverviewCtrl = ingredientOverview.getKey();
        this.addIngredientScene = new Scene(addIngredient.getValue());
        this.downloadRecipeScene = new Scene(downloadRecipe.getValue());
        this.downloadRecipeCtrl = downloadRecipe.getKey();
        this.editRecipeScene = new Scene(editRecipe.getValue());
        this.editIngredientScene = new Scene(editIngredient.getValue());
        this.editIngredientCtrl = editIngredient.getKey();
        showRecipeOverview();
        primaryStage.show();
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
        recipeOverviewCtrl.refresh();
    }

    public void showAddRecipe() {
        addRecipeCtrl.cloneRecipe();
        primaryStage.setTitle("Add Recipe");
        primaryStage.setScene(addRecipeScene);
    }

    public void showDownloadRecipe(Recipe recipe) {
        System.out.println(RecipeUtil.toMarkdown(recipe));
        downloadRecipeCtrl.setRecipe(recipe);
        primaryStage.setTitle("Download Recipe");
        primaryStage.setScene(downloadRecipeScene);
    }

    public void showIngredientOverview() {
        primaryStage.setTitle("Ingredient Overview");
        primaryStage.setScene(ingredientOverviewScene);
        ingredientOverviewCtrl.refresh();

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

    public void showEditIngredient(Ingredient ingredient) {
        editIngredientCtrl.setIngredientToEdit(ingredient);
        primaryStage.setTitle("Edit Ingredient");
        primaryStage.setScene(editIngredientScene);
    }
}

