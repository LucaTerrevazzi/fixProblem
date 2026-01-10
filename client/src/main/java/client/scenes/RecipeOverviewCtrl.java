package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Instruction;
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

    private Recipe selectedRecipe = null;

    @FXML
    private Button addRecipeButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button deleteRecipeButton;
    @FXML
    private Button editRecipeButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button downloadButton;
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

    public void goToEditScene(){
        System.out.println(" Go to edit scene ");
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No recipe selected for editing");
            return;
        }

        Recipe fullRecipe = server.getRecipeById(selected.getRecipeID());

        pc.showEditRecipe(fullRecipe);

    }

    public void goToAddScene() {
        System.out.println(" Go to add scene ");
        pc.showAddRecipe();
    }

    public void goToDownloadRecipe() {
        if(selectedRecipe != null) {
            System.out.println(" Go to download Recipe ");
            pc.showDownloadRecipe(selectedRecipe);
        } else {
            System.out.println(" No Recipe Selected ");
        }
    }

    public void goToFavorites(){
        System.out.println("Go to the Favorites scene *not functional yet*");
    }

    public void goToIngredients(){
        System.out.println("Go to the Ingredients scene");
        pc.showIngredientOverview();
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
                        Recipe fullRecipe =
                                server.getRecipeById(selectedRecipe.getRecipeID());
                        showRecipeDetails(fullRecipe);
                    }
                });


        refresh();
    }

    private void showRecipeDetails(Recipe recipe) {
        selectedRecipe = recipe;

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

        recipeLanguage.setText("Language: " + recipe.getRecipeLanguage().toString());
    }

}