package client.scenes;

import client.utils.RecipeUtil;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DownloadRecipeCtrl {

    /**
     * The Recipe that is currently shown as a preview to download.
     * This Recipe will be downloaded if we press the download button.
     */
    private Recipe recipe;

    /**
     * This is a FXML-component that contains the Recipe preview.
     * This allows to use HTML to format the MD in a nice way.
     */
    @FXML
    private WebView preview;

    /**
     * This label will show information to the user .
     * e.g. "Downloading..."
     */
    @FXML private Label status;

    /**
     * Parser for converting Markdown to HTML.
     */
    private final Parser mdParser = Parser.builder().build();

    /**
     * Renderer to convert parsed Markdown into HTML.
     */
    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    /**
     * Reference to the main controller for navigation.
     */
    private final FoodPalCtrl pc;

    /**
     * Constructor with dependency injection.
     * @param pc main controller for navigation.
     */
    @Inject
    public DownloadRecipeCtrl(FoodPalCtrl pc) {
        this.pc = pc;
    }

    /**
     * Sets the Recipe on the UI for preview.
     * Converts the Recipe to Markdown and then to HTML for WebView.
     * @param recipe the Recipe to display
     */
    public void setRecipeOnUI(Recipe recipe) {
        if (recipe == null) {
            preview.getEngine().loadContent("<i>No recipe to display</i>");
            return;
        }

        String markdown = RecipeUtil.toMarkdown(recipe);

        Node doc = mdParser.parse(markdown);
        String htmlBody = htmlRenderer.render(doc);

        String html = """
            <html>
              <head>
                <style>
                  body {
                    font-family: Arial, sans-serif;
                    padding: 10px;
                    line-height: 1.5;
                  }
                  h2, h3 { color: #333; }
                  ul { margin-left: 20px; }
                  em { color: #555; }
                </style>
              </head>
              <body>
            """ + htmlBody + """
              </body>
            </html>
            """;

        preview.getEngine().loadContent(html);
    }

    /**
     * Sets the Recipe internally and updates the UI.
     * @param r the Recipe to set
     */
    public void setRecipe(Recipe r){
        recipe = r;
        setRecipeOnUI(r);
    }

    /**
     * Initialize method called after FXML loading.
     */
    public void initialize() {
    }

    /**
     * Navigates back to the Recipe overview scene.
     */
    @FXML
    public void goBack() {
        status.setText("");
        pc.showRecipeOverview();
    }

    /**
     * Downloads the Markdown version of the Recipe.
     * Creates a .md file with the Recipe's name.
     * Updates the status label on success or failure.
     */
    @FXML
    public void downloadRecipe() {
        System.out.println("Downloading...");
        status.setText("Downloading...");

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose a folder to save the recipe");

        Stage stage = (Stage) status.getScene().getWindow();
        File selectedDir = chooser.showDialog(stage);

        if (selectedDir == null) {
            status.setText("Download cancelled.");
            return;
        }

        File file = new File(selectedDir, recipe.getRecipeName() + ".md");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(RecipeUtil.toMarkdown(recipe));
            String message = "File created : " + file.getAbsolutePath();
            System.out.println(message);
            status.setText(message);
        } catch (IOException e) {
            e.printStackTrace();
            status.setText("Error while saving file.");
        }

    }
}
