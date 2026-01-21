package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Language;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditIngredientCtrl implements Initializable {

    private long ingredientID;
    private Language language;

    @FXML
    private Label errorLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField fatField;
    @FXML
    private TextField proteinField;
    @FXML
    private TextField carbsField;

    private final FoodPalCtrl pc;

    private ResourceBundle bundle;

    @Inject
    public EditIngredientCtrl(FoodPalCtrl p) {
        this.pc = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    @FXML
    public void goBack() {
        pc.showIngredientOverview();
        clear();
    }

    private void clear() {
        nameField.setText("");
        fatField.setText("");
        proteinField.setText("");
        carbsField.setText("");
        errorLabel.setText("");
    }

    public void setIngredientToEdit(Ingredient ingredient) {
        this.ingredientID = ingredient.getIngredientID();

        nameField.setText(ingredient.getIngredientName());
        fatField.setText(String.valueOf(ingredient.getFat()));
        proteinField.setText(String.valueOf(ingredient.getProtein()));
        carbsField.setText(String.valueOf(ingredient.getCarbs()));
        this.language = ingredient.getIngredientLanguage();
    }

    public void editIngredient() {
        errorLabel.setText("");

        String name = nameField.getText().trim();
        String fatText = fatField.getText().trim();
        String proteinText = proteinField.getText().trim();
        String carbsText = carbsField.getText().trim();

        if (name.isEmpty() || fatText.isEmpty() || proteinText.isEmpty() || carbsText.isEmpty()) {
            errorLabel.setText(bundle.getString("editIngredient.error.emptyFields"));
            return;
        }

        Double fat = parseDoubleOrNull(fatText);
        Double protein = parseDoubleOrNull(proteinText);
        Double carbs = parseDoubleOrNull(carbsText);

        if (fat == null || protein == null || carbs == null) {
            errorLabel.setText(bundle.getString("editIngredient.error.invalidNumber"));
            return;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientID(this.ingredientID);
        ingredient.setIngredientName(name);
        ingredient.setFat(fat);
        ingredient.setProtein(protein);
        ingredient.setCarbs(carbs);
        ingredient.setIngredientLanguage(this.language);

        try {
            ServerUtils.editIngredient(ingredientID, ingredient);
        } catch (Exception e) {
            errorLabel.setText(bundle.getString("editIngredient.error.saveFailed"));
            return;
        }

        goBack();
    }

    private Double parseDoubleOrNull(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
