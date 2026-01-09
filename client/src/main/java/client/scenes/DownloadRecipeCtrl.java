package client.scenes;

import client.utils.RecipeUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DownloadRecipeCtrl {

    @FXML private Text preview;

    private final FoodPalCtrl pc;
    private final ServerUtils server;

    @Inject
    public DownloadRecipeCtrl(FoodPalCtrl pc, ServerUtils server) {
        this.pc = pc;
        this.server = server;
    }

    public void setRecipeOnUI(Recipe recipe) {
        preview.setText(RecipeUtil.toMarkdown(recipe));
    }

    public void setRecipe(Recipe r){
        preview.setText(RecipeUtil.toMarkdown(r));
    }

    public void initialize() {
    }

    @FXML
    public void goBack() {
        pc.showRecipeOverview();
    }

    @FXML
    public void downloadRecipe() {
        System.out.println("Downloading...");
    }
}
