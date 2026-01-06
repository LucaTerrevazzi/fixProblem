package client.scenes;

import com.google.inject.Inject;
import commons.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddRecipeCtrl {

    @FXML
    private Button toOverviewButton;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> languageCombo;

    private final FoodPalCtrl pc;

    @Inject
    public AddRecipeCtrl(FoodPalCtrl p) {
        this.pc = p;
    }

    public void initialize() {
        languageCombo.getItems().addAll(
                "EN", "DU", "GR", "FR", "TR"
        );
        languageCombo.setValue("English");
    }

    @FXML
    public void click() {
        pc.showRecipeOverview();
    }

    @FXML
    public void createRecipe() {
        String name = nameField.getText();

        Recipe recipe = new Recipe(name);
        recipe.setRecipeLanguage(languageCombo.getValue());

        System.out.println("Created recipe: " + recipe);

        pc.showRecipeOverview();
    }
}
