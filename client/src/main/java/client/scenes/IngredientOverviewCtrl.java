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
import java.text.MessageFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class IngredientOverviewCtrl implements Initializable {

    @FXML
    private Button editIngredientButton;
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

    @FXML
    private Label ingredientLanguage;
    @FXML
    private Label fatLabel;
    @FXML
    private Label proteinLabel;
    @FXML
    private Label carbsLabel;
    @FXML
    private Label kcalLabel;
    @FXML
    private Label recipeCounterText;

    @FXML
    private ListView<Ingredient> ingredientListView;

    @FXML
    private Label ingredientName;

    private final ObservableList<Ingredient> ingredients =
            FXCollections.observableArrayList();

    private ResourceBundle bundle;

    private final ServerUtils server;
    private final FoodPalCtrl pc;

    @Inject
    public IngredientOverviewCtrl(FoodPalCtrl p, ServerUtils server) {
        this.pc = p;
        this.server = server;
    }

    public void goToEditIngredientScene() {
        Ingredient selected = ingredientListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        pc.showEditIngredient(selected);
    }

    public void goToAddIngredientScene() {
        pc.showAddIngredient();
    }

    public void goToFavorites() {
        // not implemented yet
    }

    public void goToRecipes() {
        pc.showRecipeOverview();
    }

    public void deleteIngredient() {
        Ingredient selected = ingredientListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(bundle.getString("dialog.noSelection.title"));
            a.setHeaderText(null);
            a.setContentText(bundle.getString("dialog.noSelection.ingredient"));
            a.showAndWait();
            return;
        }

        int recipeCount = server.countRecipesUsingIngredient(selected.getIngredientID());

        String usageText = (recipeCount == 1)
                ? MessageFormat.format(bundle.getString("ingredient.usedIn.one"), recipeCount)
                : MessageFormat.format(bundle.getString("ingredient.usedIn.many"), recipeCount);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(bundle.getString("dialog.deleteIngredient.title"));
        confirm.setHeaderText(
                MessageFormat.format(
                        bundle.getString("dialog.deleteIngredient.header"),
                        selected.getIngredientName(),
                        usageText
                )
        );
        confirm.setContentText(bundle.getString("dialog.deleteIngredient.content"));

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) {
            return;
        }

        boolean success = server.deleteIngredient(selected.getIngredientID().longValue());

        if (!success) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle(bundle.getString("dialog.deleteFailed.title"));
            err.setHeaderText(bundle.getString("dialog.deleteFailed.header"));
            err.setContentText(bundle.getString("dialog.deleteFailed.content"));
            err.showAndWait();
            return;
        }

        refresh();
        ingredientListView.getSelectionModel().clearSelection();
        ingredientName.setText("");
    }

    public void refresh() {
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
        } else {
            recipeCounterText.setText(bundle.getString("ingredient.usedIn.noneSelected"));
        }

        showIngredientDetails(ingredientListView.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

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
            recipeCounterText.setText(bundle.getString("ingredient.usedIn.noneSelected"));
            return;
        }

        ingredientName.setText(ing.getIngredientName());
        ingredientLanguage.setText(String.valueOf(ing.getIngredientLanguage()));

        proteinLabel.setText(String.format("%.1fg", ing.getProtein()));
        fatLabel.setText(String.format("%.1fg", ing.getFat()));
        carbsLabel.setText(String.format("%.1fg", ing.getCarbs()));
        kcalLabel.setText(String.format("%.0f kcal", ing.getKcal()));

        int recipeCount = server.countRecipesUsingIngredient(ing.getIngredientID());
        if (recipeCount == 1) {
            recipeCounterText.setText(
                    MessageFormat.format(bundle.getString("ingredient.usedIn.one"), recipeCount)
            );
        } else {
            recipeCounterText.setText(
                    MessageFormat.format(bundle.getString("ingredient.usedIn.many"), recipeCount)
            );
        }
    }
}
