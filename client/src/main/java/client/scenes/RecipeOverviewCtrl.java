package client.scenes;

import com.google.inject.Inject;
import commons.Recipe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


import java.net.URL;
import java.util.ResourceBundle;

public class RecipeOverviewCtrl implements Initializable {

    @FXML
    private Button addRecipeButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button deleteRecipeButton;
    @FXML
    private Button refreshButton;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Label recipeName;

    private FoodPalCtrl pc ;
    @Inject
    public RecipeOverviewCtrl ( FoodPalCtrl p ) {
        this.pc = p ;
    }
    public void goToAddScene() {
        System.out.println( " Go to add scene " );
        pc.showAdd();
    }

    public void goToFavorites(){
        System.out.println("Go to the Favorites scene *not functional yet*");
    }
    public void goToIngredients(){
        System.out.println("Go to the Ingredients scene *not functional yet*");
    }
    public void goToDeleteScene(){
        System.out.println("Go to the Delete recipe scene *not functional yet*");
    }
    public void refresh(){
        System.out.println("Refresh button clicked!");
    }

        Recipe recipe1 = new Recipe("Pizza");
        Recipe recipe2 = new Recipe("Pasta");
        String[] recipeList = {recipe1.getRecipeName(), recipe2.getRecipeName()};
        String currentRecipeName;


    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeListView.getItems().addAll(recipeList);
        recipeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                currentRecipeName = recipeListView.getSelectionModel().getSelectedItem();

                recipeName.setText(currentRecipeName);

            }
        });
    }
}
