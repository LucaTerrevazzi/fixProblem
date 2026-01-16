package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class IngredientOverviewCtrl implements Initializable {

    public Button editIngredientButton;
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
    @FXML private Label ingredientLanguage;
    @FXML private Label fatLabel;
    @FXML private Label proteinLabel;
    @FXML private Label carbsLabel;
    @FXML private Label kcalLabel;


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

    public void goToEditIngredientScene() {
        System.out.println(" Go to edit scene ");
        Ingredient selected = ingredientListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No ingredient selected for editing");
            return;
        }

        pc.showEditIngredient(selected);

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
    public void deleteIngredient() {
        Ingredient selected = ingredientListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("No selection");
            a.setHeaderText(null);
            a.setContentText("Select an ingredient first.");
            a.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete ingredient");
        confirm.setHeaderText("Delete \"" + selected.getIngredientName() + "\"?");
        confirm.setContentText("This cannot be undone.");

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) {
            return;
        }

        boolean success = server.deleteIngredient(
                selected.getIngredientID().longValue()
        );

        if (!success) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Delete failed");
            err.setHeaderText("Could not delete ingredient.");
            err.setContentText("It may be used in a recipe or the server rejected the request.");
            err.showAndWait();
            return;
        }

        refresh();
        ingredientListView.getSelectionModel().clearSelection();
        ingredientName.setText("");
    }



    public void refresh() {
        System.out.println("Refresh button clicked! (Or refreshed automatically) ");

        Ingredient previouslySelected = ingredientListView.getSelectionModel().getSelectedItem();
        Long prevId = previouslySelected != null ? previouslySelected.getIngredientID() : null;

        ingredients.setAll(
                server.getIngredients().stream()
                        .sorted(java.util.Comparator.comparing(
                                i -> i.getIngredientName().toLowerCase()
                        ))
                        .toList()
        );

        if (prevId != null) {
            for (Ingredient ing : ingredients) {
                if (prevId.equals(ing.getIngredientID())) {
                    ingredientListView.getSelectionModel().select(ing);
                    break;
                }
            }
        }

        if (ingredientListView.getSelectionModel().getSelectedItem() == null && !ingredients.isEmpty()) {
            ingredientListView.getSelectionModel().selectFirst();
        }

        showIngredientDetails(ingredientListView.getSelectionModel().getSelectedItem());
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
                .addListener((obs, oldIng, selectedIng) -> showIngredientDetails(selectedIng));

        refresh();
    }
    private void showIngredientDetails(Ingredient ing) {
        if (ing == null) {
            ingredientName.setText("");
            ingredientLanguage.setText("");
            proteinLabel.setText("0g");
            fatLabel.setText("0g");
            carbsLabel.setText("0g");
            kcalLabel.setText("0 kcal");
            return;
        }

        ingredientName.setText(ing.getIngredientName());
        ingredientLanguage.setText(String.valueOf(ing.getIngredientLanguage()));

        proteinLabel.setText(String.format("%.1fg", ing.getProtein()));
        fatLabel.setText(String.format("%.1fg", ing.getFat()));
        carbsLabel.setText(String.format("%.1fg", ing.getCarbs()));
        kcalLabel.setText(String.format("%.0f kcal", ing.getKcal()));
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}