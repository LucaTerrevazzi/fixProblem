package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditIngredientCtrl {

    private long ingredientID;
    private Language language;

    @FXML private Label errorLabel;
    @FXML private TextField nameField;
    @FXML private TextField fatField;
    @FXML private TextField proteinField;
    @FXML private TextField carbsField;

    private FoodPalCtrl pc ;

    @Inject
    public EditIngredientCtrl(FoodPalCtrl p) {
        this.pc = p ;
    }

    @FXML
    public void goBack() {
        pc.showIngredientOverview();
        clear();
    }

    private void clear(){
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
        String fat = fatField.getText().trim();
        String protein = proteinField.getText().trim();
        String carbs = carbsField.getText().trim();
        if (name.isEmpty() || fat.isEmpty() || protein.isEmpty() || carbs.isEmpty()) {
            errorLabel.setText("Please enter a value in all of the fields");
            return;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientID(this.ingredientID);
        ingredient.setIngredientName(name);
        ingredient.setFat(Double.parseDouble(fat));
        ingredient.setProtein(Double.parseDouble(protein));
        ingredient.setCarbs(Double.parseDouble(carbs));
        ingredient.setIngredientLanguage(this.language);
        System.out.println(ingredient.toString());
        try {
            ServerUtils.editIngredient(ingredientID, ingredient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        goBack();
    }

}
