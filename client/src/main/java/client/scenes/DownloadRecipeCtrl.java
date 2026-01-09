package client.scenes;

import client.utils.RecipeUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadRecipeCtrl {

    @FXML private Text preview;
    private Recipe recipe;

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
        recipe = r;
        preview.setText(RecipeUtil.toMarkdown(r));
    }

    public void initialize() {
    }

    @FXML
    public void goBack() {
        pc.showRecipeOverview();
    }

    /**
     * Downloads the Markdown version of the Recipe.
     */
    @FXML
    public void downloadRecipe() {
        System.out.println("Downloading...");
        File file = new File(recipe.getRecipeName()+".md");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(RecipeUtil.toMarkdown(recipe));
            System.out.println("File created : " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
