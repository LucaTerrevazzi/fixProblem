package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import commons.Recipe;

import java.io.File;
import java.io.IOException;

public class FavoritesUtil {

    private static String filePath = "src/main/resources/Favorites.json";

    /**
     * function to change the path of the favorites file for testing purposes
     * @param path path to set the path to
     */
    public static void setFilePath(String path) {
        filePath = path;
    }

    /**
     * load the favorites from the Favorites.json file
     * @return returns the favorites as a FavoritesData object
     * @throws IOException can throw an IOException
     */
    public static FavoritesData loadJson() throws IOException {
        File file = new File(filePath);
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        if (!file.exists()) {
            FavoritesData data = new FavoritesData();
            saveJson(data);
            return data;
        }

        return mapper.readValue(file, FavoritesData.class);
    }

    /**
     * save the favorites to the Favorites.json file
     * @param data FavoritesData object to save
     * @throws IOException can throw an IOException
     */
    protected static void saveJson(FavoritesData data) throws IOException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.writeValue(new File(filePath), data);
    }

    /**
     * add a recipe to favorites
     * @param r recipe to add
     * @throws IOException can throw an IOException
     */
    public static void addFavorite(Recipe r) throws IOException {
        Long id = r.getRecipeID();
        FavoritesData data = loadJson();

        if (!data.getFavorites().contains(id)) {
            data.getFavorites().add(Math.toIntExact(id));
            saveJson(data);
        }
    }

    /**
     * removes a recipe from favorites
     * @param r recipe to remove
     * @throws IOException can throw an IOException
     */
    public static void removeFavorite(Recipe r) throws IOException {
        Long id = r.getRecipeID();
        FavoritesData data = loadJson();

        if (data.getFavorites().remove(Integer.valueOf(String.valueOf(id)))) {
            saveJson(data);
        }
    }
}


