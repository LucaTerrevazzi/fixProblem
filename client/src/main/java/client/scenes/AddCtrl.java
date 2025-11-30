package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddCtrl {
    @FXML
    private Button toOverviewButton;

    private FoodPalCtrl pc ;
    @Inject
    public AddCtrl ( FoodPalCtrl p ) {
        this.pc = p ;
    }
    public void click() {
        System.out.println( " Go to recipe overview scene " );
        pc.showRecipeOverview();
    }
}