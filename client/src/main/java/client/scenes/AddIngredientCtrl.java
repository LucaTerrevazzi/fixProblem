package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddIngredientCtrl {
    @FXML
    private Button toOverviewButton;

    private FoodPalCtrl pc ;
    @Inject
    public AddIngredientCtrl(FoodPalCtrl p) {
        this.pc = p ;
    }
    public void click() {
        System.out.println(" Go to Ingredient overview scene ");
        pc.showIngredientOverview();
    }
}