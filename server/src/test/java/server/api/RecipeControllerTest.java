package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import server.service.RecipeService;

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

    @Test
    public void createRecipe_savesAndReturnsRecipe() {
        Recipe recipe = createRecipe("Lasagna");

        Recipe actual = sut.createRecipe(recipe);

        assertEquals(recipe, actual);
    }

    @Test
    public void createRecipe_nullRecipe_throwsBadRequest() {

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.createRecipe(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void updateRecipe_updatesAndReturnsRecipe() {
        Recipe updatedRecipe = createRecipe("Updated Pasta");

        Recipe actual = sut.updateRecipe(1, updatedRecipe);

        assertEquals(updatedRecipe, actual);
    }

    @Test
    public void updateRecipe_nullRecipe_throwsBadRequest() {
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.updateRecipe(1, null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void updateRecipe_recipeNotFound_throwsNotFound() {

        Recipe recipe = createRecipe("Non Existing Recipe");

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.updateRecipe(999, recipe));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void deleteRecipe_existingRecipe_deletesSuccessfully() {
        assertDoesNotThrow(() -> sut.deleteRecipe(0));
    }

    @Test
    public void deleteRecipe_recipeNotFound_throwsNotFound() {
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.deleteRecipe(999));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
