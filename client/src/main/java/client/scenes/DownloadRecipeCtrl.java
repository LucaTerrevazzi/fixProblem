package client.scenes;

import client.utils.RecipeUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadRecipeCtrl {

    private Recipe recipe;

    @FXML
    private WebView preview;

    @FXML private Label status;

    private final Parser mdParser = Parser.builder().build();
    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();

    private final FoodPalCtrl pc;
    private final ServerUtils server;

    @Inject
    public DownloadRecipeCtrl(FoodPalCtrl pc, ServerUtils server) {
        this.pc = pc;
        this.server = server;
    }

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

    public void setRecipe(Recipe r){
        recipe = r;
        setRecipeOnUI(r);
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
        status.setText("Downloading...");
        File file = new File(recipe.getRecipeName()+".md");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(RecipeUtil.toMarkdown(recipe));
            String message = "File created : " + file.getAbsolutePath();
            System.out.println(message);
            status.setText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
