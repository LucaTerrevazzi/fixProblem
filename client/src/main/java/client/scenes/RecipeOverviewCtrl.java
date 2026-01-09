package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Instruction;
import commons.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    private ListView<String> ingredientsList;

    @FXML
    private ListView<String> instructionsList;

    @FXML
    private Label recipeLanguage;

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
        System.out.println("Go to add scene");
        pc.showAddRecipe();
    }

    public void goToFavorites(){
        System.out.println("Go to the Favorites scene *not functional yet*");
    }

    public void goToIngredients(){
        System.out.println("Go to the Ingredients scene");
        pc.showIngredientOverview();
    }

    public void deleteRecipeWarning(){
        System.out.println("Go to the Delete recipe warning");

        Recipe selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Recipe deletion");
        alert.setContentText("Are you sure you want to delete the recipe " + selectedRecipe.getRecipeName() + "?\n" +
                "This action cannot be undone");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        server.deleteRecipe(selectedRecipe);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Recipe " + selectedRecipe.getRecipeName() + " deleted");
                });
    }

    public void refresh(){
        System.out.println("Refresh button clicked!");
        recipes.clear();
        recipes.addAll(server.getRecipes());
        deleteRecipeButton.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("RecipeOverviewCtrl initialized");

        recipeListView.setItems(recipes);

        deleteRecipeButton.setDisable(true);

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
                        Recipe fullRecipe =
                                server.getRecipeById(selectedRecipe.getRecipeID());
                        showRecipeDetails(fullRecipe);
                    }

                    deleteRecipeButton.setDisable(false);
                });

        if (recipeListView.getSelectionModel().getSelectedItem() == null) {
            deleteRecipeButton.setDisable(true);
        }

        refresh();
    }

    private void showRecipeDetails(Recipe recipe) {
        refresh();
        System.out.println(
                "DEBUG → recipe id=" + recipe.getRecipeID()
                        + " steps=" + (recipe.getSteps() == null ? "NULL" : recipe.getSteps().size())
        );
        recipeName.setText(recipe.getRecipeName());

        ingredientsList.setItems(
                FXCollections.observableArrayList(
                        recipe.getIngredients().stream()
                                .map(ri -> ri.getAmount() + " " +
                                        ri.getUnit() + " " +
                                        ri.getIngredient().getIngredientName())
                                .toList()
                )
        );

        instructionsList.setItems(
                FXCollections.observableArrayList(
                        recipe.getSteps().stream()
                                .map(Instruction::getDescription)
                                .toList()
                )
        );

        recipeLanguage.setText("Language: " + recipe.getRecipeLanguage());
    }

}