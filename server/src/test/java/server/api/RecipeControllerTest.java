package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

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
    public void createRecipe_nullRecipe_throwsBadRequest() {

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.createRecipe(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void createRecipe_validRecipe_savesAndReturnsSavedRecipe() {
        RecipeService recipeService = mock(RecipeService.class);
        RecipeController controller = new RecipeController(recipeService);

        Recipe input = createRecipe("Pasta");
        Recipe saved = createRecipe("Pasta");
        saved.setRecipeID(123L);

        when(recipeService.save(input)).thenReturn(saved);


        Recipe result = controller.createRecipe(input);


        assertEquals(saved, result);
        verify(recipeService).save(input);
        verifyNoMoreInteractions(recipeService);
    }


    @Test
    public void updateRecipe_nullRecipe_throwsBadRequest() {
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> sut.updateRecipe(1, null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void updateRecipe_validRecipe_updatesAndReturnsUpdatedRecipe() {
        RecipeService recipeService = mock(RecipeService.class);
        RecipeController controller = new RecipeController(recipeService);

        Recipe input = createRecipe("Pasta");
        Recipe updated = createRecipe("Pasta (updated)");
        updated.setRecipeID(1L);

        when(recipeService.update(1L, input)).thenReturn(updated);

        Recipe result = controller.updateRecipe(1L, input);

        assertEquals(updated, result);
        verify(recipeService).update(1L, input);
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    public void updateRecipe_unknownId_throwsNotFound() {
        RecipeService recipeService = mock(RecipeService.class);
        RecipeController controller = new RecipeController(recipeService);

        Recipe input = createRecipe("Pasta");

        when(recipeService.update(99L, input)).thenThrow(new IllegalArgumentException("not found"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> controller.updateRecipe(99L, input));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(recipeService).update(99L, input);
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    public void deleteRecipe_existingId_callsFindByIdThenDelete() {
        RecipeService recipeService = mock(RecipeService.class);
        RecipeController controller = new RecipeController(recipeService);

        long id = 1L;
        Recipe existing = createRecipe("Pasta");

        when(recipeService.findById(id)).thenReturn(existing);

        assertDoesNotThrow(() -> controller.deleteRecipe(id));

        verify(recipeService).findById(id);
        verify(recipeService).deleteById(id);
        verifyNoMoreInteractions(recipeService);
    }

    @Test
    public void deleteRecipe_unknownId_throwsNotFound_andDoesNotDelete() {
        RecipeService recipeService = mock(RecipeService.class);
        RecipeController controller = new RecipeController(recipeService);

        long id = 999L;

        when(recipeService.findById(id)).thenThrow(new IllegalArgumentException("not found"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> controller.deleteRecipe(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(recipeService).findById(id);
        verify(recipeService, never()).deleteById(anyLong());
        verifyNoMoreInteractions(recipeService);
    }

}
