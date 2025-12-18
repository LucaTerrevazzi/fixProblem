package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Recipe;

public class RecipeControllerTest {

    private RecipeController sut;

    private static final Recipe PASTA = createRecipe("Pasta");
    private static final Recipe PIZZA = createRecipe("Pizza");

    @BeforeEach
    public void setup() {
        sut = new RecipeController(new TestRecipeService());
    }

    @Test
    public void returnsAllRecipes() {
        var actual = sut.getAllRecipes();
        var expected = List.of(PASTA, PIZZA);

        assertEquals(expected, actual);
    }

    @Test
    public void returnsRecipeById() {
        var actual = sut.getRecipeById(1);
        var expected = PASTA;

        assertEquals(expected, actual);
    }

    private static Recipe createRecipe(String name) {
        Recipe r = new Recipe();
        r.setRecipeName(name);
        return r;
    }
}
