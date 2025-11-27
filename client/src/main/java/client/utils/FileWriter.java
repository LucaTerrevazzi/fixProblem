package client.utils;

import commons.Recipe;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Separate class to use dependency injection (acts as class that passes a FileOutputStream)
 */
public class FileWriter {

    /**
     * A method that writes the selected recipe to the specified path
     * @param r The selected recipe
     * @param filePath The programmed file path
     */
    public void downloadRecipe(Recipe r, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            RecipeUtil.outputRecipeStream(r, fos);
        } catch (IOException e) {
            System.out.println("Cannot write to file");
        }
    }

}
