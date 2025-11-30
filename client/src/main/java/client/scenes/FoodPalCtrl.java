package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class FoodPalCtrl {

    private Stage primaryStage;
    private Scene recipeOverviewScene;
    private Scene addScene;

    public void init(Stage primaryStage, Pair<RecipeOverviewCtrl, Parent> overview, Pair<AddCtrl, Parent> add) {
        this.primaryStage = primaryStage;
        this.recipeOverviewScene = new Scene(overview.getValue());
        this.addScene = new Scene(add.getValue());
        showRecipeOverview();
        primaryStage.show();
    }

    public void showRecipeOverview() {
        primaryStage.setTitle("Recipe Overview");
        primaryStage.setScene(recipeOverviewScene);
    }

    public void showAdd() {
        primaryStage.setTitle("Add Recipe");
        primaryStage.setScene(addScene);
    }
}

