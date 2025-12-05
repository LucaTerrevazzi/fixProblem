package client;

import client.utils.ShoppingListData;
import client.utils.ShoppingUtil;
import commons.Recipe;
import commons.RecipeIngredient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingUtilTest {

    private static final String TEST_FILE = "src/main/resources/test_Shopping.json";

    @BeforeEach
    void setUp() {
        File file = new File("src/main/resources/test_Shopping.json");
        if (file.exists()) file.delete();

        ShoppingUtil.setFilePath(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();

        ShoppingUtil.resetFilePath();
    }

    @Test
    void testAddIngredient() throws IOException {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipeIngredientId(2835);
        ShoppingUtil.addIngredient(ri);

        ShoppingListData data = ShoppingUtil.loadJson();
        assertEquals(ri.getRecipeIngredientId(), data.getIngredients().getFirst());
    }

    @Test
    void testAddRecipe() throws IOException {
        Recipe r = new Recipe(4583L);
        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipeIngredientId(2835);
        r.setIngredients(List.of(ri));
        ShoppingUtil.addRecipe(r);

        ShoppingListData data = ShoppingUtil.loadJson();
        assertEquals(r.getIngredients().getFirst().getRecipeIngredientId(), data.getIngredients().getFirst());
        assertEquals(r.getRecipeID(), data.getRecipes().getFirst());
    }

    @Test
    void testRemoveIngredient() throws IOException {
        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setRecipeIngredientId(29374);
        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.setRecipeIngredientId(2835);

        ShoppingUtil.addIngredient(ri1);
        ShoppingUtil.addIngredient(ri2);
        ShoppingUtil.removeIngredient(ri1);
        ShoppingListData data = ShoppingUtil.loadJson();

        assertEquals(ri2.getRecipeIngredientId(), data.getIngredients().getFirst());
    }

}
