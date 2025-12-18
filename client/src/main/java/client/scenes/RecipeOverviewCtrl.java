package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeOverviewCtrl implements Initializable {

    @FXML
    private Button addRecipeButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button deleteRecipeButton;
    @FXML
    private Button refreshButton;
    @Inject
    private ServerUtils server;
    @Inject
    private FoodPalCtrl pc ;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Label recipeName;
    private final ObservableList<Recipe> recipes =
            FXCollections.observableArrayList();

    @Inject
    public RecipeOverviewCtrl(FoodPalCtrl p, ServerUtils server) {
        this.pc = p;
        this.server = server;
    }

    public void goToAddScene() {
        System.out.println( " Go to add scene " );
        pc.showAdd();
    }

    public void goToFavorites(){
        System.out.println("Go to the Favorites scene *not functional yet*");
    }
    public void goToIngredients(){
        System.out.println("Go to the Ingredients scene *not functional yet*");
    }
    public void goToDeleteScene(){
        System.out.println("Go to the Delete recipe scene *not functional yet*");
    }

    public void refresh(){
        System.out.println("Refresh button clicked!");
        recipes.clear();
        recipes.addAll(server.getRecipes());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("RecipeOverviewCtrl initialized");

        recipeListView.setItems(recipes);

        recipeListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Recipe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getRecipeName());
                }
            }
        });

        recipeListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldRecipe, selectedRecipe) -> {
                    if (selectedRecipe != null) {
                        recipeName.setText(selectedRecipe.getRecipeName());
                    }
                });

        refresh();
    }
}