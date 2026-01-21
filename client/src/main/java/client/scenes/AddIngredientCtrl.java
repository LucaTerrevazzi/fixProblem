package client.scenes;

import client.utils.LanguageManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Language;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddIngredientCtrl implements Initializable {

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

    private static boolean fromAddRecipe = false;

    private final FoodPalCtrl pc;

    private ResourceBundle bundle;

    @Inject
    public AddIngredientCtrl(FoodPalCtrl p) {
        this.pc = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    @FXML
    public void goBack() {
        if (fromAddRecipe) {
            fromAddRecipe = false;
            pc.showAddRecipe();
            clear();
            return;
        }
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

    @FXML
    public void createIngredient() {
        errorLabel.setText("");

        String name = nameField.getText().trim();
        String fat = fatField.getText().trim();
        String protein = proteinField.getText().trim();
        String carbs = carbsField.getText().trim();

        if (name.isEmpty() || fat.isEmpty() || protein.isEmpty() || carbs.isEmpty()) {
            errorLabel.setText(bundle.getString("addIngredient.error.fillAll"));
            return;
        }

        try {
            double f = Double.parseDouble(fat);
            double p = Double.parseDouble(protein);
            double c = Double.parseDouble(carbs);

            if (f < 0 || p < 0 || c < 0) {
                errorLabel.setText(bundle.getString("addIngredient.error.positiveOnly"));
                return;
            }

            Ingredient ingredient = new Ingredient(name);
            ingredient.setFat(f);
            ingredient.setProtein(p);
            ingredient.setCarbs(c);

            ingredient.setIngredientLanguage(mapLocaleToLanguage(LanguageManager.getLocale()));

            try {
                ServerUtils.addIngredient(ingredient);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            goBack();

        } catch (NumberFormatException e) {
            errorLabel.setText(bundle.getString("addIngredient.error.numberFormat"));
        }
    }

    private Language mapLocaleToLanguage(Locale locale) {
        if (locale == null) {
            return Language.EN;
        }
        return switch (locale.getLanguage()) {
            case "nl" -> Language.NL;
            case "fr" -> Language.FR;
            case "tr" -> Language.TR;
            case "el" -> Language.GR;
            default -> Language.EN;
        };
    }

    public static void setFromAddRecipe(boolean b) {
        fromAddRecipe = b;
    }
}
