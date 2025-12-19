package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
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

public class IngredientOverviewCtrl implements Initializable {

    @FXML
    private Button addIngredientButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private Button recipesButton;
    @FXML
    private Button deleteIngredientButton;
    @FXML
    private Button refreshButton;
    @Inject
    private ServerUtils server;
    @Inject
    private FoodPalCtrl pc ;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Label ingredientName;
    private final ObservableList<Ingredient> ingredients =
            FXCollections.observableArrayList();

    @Inject
    public IngredientOverviewCtrl(FoodPalCtrl p, ServerUtils server) {
        this.pc = p;
        this.server = server;
    }

    public void goToAddIngredientScene() {
        System.out.println("Go to the add ingredient scene *not functional yet*");
        pc.showAddIngredient();
    }

    public void goToFavorites(){
        System.out.println("Go to the Favorites scene *not functional yet*");
    }
    public void goToRecipes(){
        System.out.println("Go to the Recipes scene");
        pc.showRecipeOverview();
    }
    public void goToDeleteIngredientScene(){
        System.out.println("Go to the delete ingredient scene *not functional yet*");
    }

    public void refresh(){
        System.out.println("Refresh button clicked!");
        ingredients.clear();
        ingredients.addAll(server.getIngredients());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("IngredientOverviewCtrl initialized");

        ingredientListView.setItems(ingredients);

        ingredientListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Ingredient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIngredientName());
                }
            }
        });

        ingredientListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldRecipe, selectedRecipe) -> {
                    if (selectedRecipe != null) {
                        ingredientName.setText(selectedRecipe.getIngredientName());
                    }
                });

        refresh();
    }
}