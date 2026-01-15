package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Language;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddIngredientCtrl {

    @FXML private Label errorLabel;
    @FXML private TextField nameField;
    @FXML private TextField fatField;
    @FXML private TextField proteinField;
    @FXML private TextField carbsField;

    private FoodPalCtrl pc ;
    @Inject
    public AddIngredientCtrl(FoodPalCtrl p) {
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

    @FXML
    public void createIngredient() {
        System.out.println("Creating ingredient ...");
        errorLabel.setText("");

        String name = nameField.getText().trim();
        String fat = fatField.getText().trim();
        String protein = proteinField.getText().trim();
        String carbs = carbsField.getText().trim();
        if (name.isEmpty() || fat.isEmpty() || protein.isEmpty() || carbs.isEmpty()) {
            errorLabel.setText("Please fill in all of the fields.");
            return;
        }

        try {
            double f = Double.parseDouble(fat);
            double p = Double.parseDouble(protein);
            double c = Double.parseDouble(carbs);
            if (f < 0 || p < 0 || c < 0) {
                errorLabel.setText("Only positive numbers are allowed.");
            }
            Ingredient ingredient = new Ingredient(name);
            ingredient.setFat(f);
            ingredient.setProtein(p);
            ingredient.setCarbs(c);
            ingredient.setIngredientLanguage(Language.EN);
            ServerUtils.addIngredient(ingredient);
            goBack();
        }
        catch (NumberFormatException e) {
            errorLabel.setText("Please make sure all numbers are formatted properly (e.g. 0.0 instead of 0,0).");
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}