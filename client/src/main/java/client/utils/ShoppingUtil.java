package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import commons.Recipe;
import commons.RecipeIngredient;

import java.io.File;
import java.io.IOException;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ShoppingUtil {

    private static String filePath = "src/main/resources/ShoppingList.json";

    /**
     * Resets the file path to the original one
     */
    public static void resetFilePath() {
        filePath = "src/main/resources/ShoppingList.json";
    }

    /**
     * Allows the path of the json file to be changed
     * @param filePath the new file path
     */
    public static void setFilePath(String filePath) {
        ShoppingUtil.filePath = filePath;
    }

    /**
     * Saves the current ShoppingList as a json file
     * @param listData the ShoppingList to save
     * @throws IOException handle when file cannot be read/written
     */
    public static void saveToJson(ShoppingListData listData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filePath), listData);
    }

    /**
     * Returns a new ShoppingList from the json file
     * @return the ShoppingList in the json
     */
    public static ShoppingListData loadJson() {
        File f = new File(filePath);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(f, ShoppingListData.class);
        } catch (IOException e) {
            return new ShoppingListData();
        }
    }

    /**
     * Adds a new ingredient to the shopping list
     * @param ri The ingredient to add
     * @throws IOException handle when file cannot be read/written
     */
    public static void addIngredient(RecipeIngredient ri) throws IOException {
        ShoppingListData listData = loadJson();

        listData.getIngredients().add(ri.getRecipeIngredientId());
        listData.getRecipes().add(null);
        saveToJson(listData);
    }

    /**
     * Removes an added ingredient from the json
     * @param ri The ingredient to remove
     * @throws IOException handle when file cannot be read/written
     */
    public static void removeIngredient(RecipeIngredient ri) throws IOException {
        ShoppingListData listData = loadJson();

        OptionalInt indexFirst = IntStream.range(0, listData.getIngredients().size())
                        .filter(i -> listData.getIngredients()
                                .get(i).equals(ri.getRecipeIngredientId()))
                                .findFirst();

        if (indexFirst.isPresent()) {
            listData.getIngredients().remove(indexFirst.getAsInt());
            listData.getRecipes().remove(indexFirst.getAsInt());
            saveToJson(listData);
        }
    }

    /**
     * Adds all ingredients from a recipe to the shopping list
     * @param r The recipe to retrieve ingredients from
     * @throws IOException handle when file cannot be read/written
     */
    public static void addRecipe(Recipe r) throws IOException {
        ShoppingListData listData = loadJson();

        for (RecipeIngredient ri : r.getIngredients()) {
            listData.getIngredients().add(ri.getRecipeIngredientId());
            listData.getRecipes().add(r.getRecipeID());
        }

        saveToJson(listData);
    }


}
