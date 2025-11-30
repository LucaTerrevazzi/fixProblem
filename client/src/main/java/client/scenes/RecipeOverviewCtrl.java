package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class RecipeOverviewCtrl {

    @FXML
    private Button addRecipeButton;

    @FXML
    private ListView<String> recipeListView;

    private FoodPalCtrl pc ;
    @Inject
    public RecipeOverviewCtrl ( FoodPalCtrl p ) {
        this.pc = p ;
    }
    public void click() {
        System.out.println( " Go to add scene " );
        pc.showAdd();
    }
}
