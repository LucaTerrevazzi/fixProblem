package server.api;

import commons.Ingredient;
import commons.Recipe;
import commons.RecipeIngredient;
import commons.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.RecipeIngredientService;
import server.service.RecipeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeIngredientControllerTest {

    private RecipeIngredientController sut;
    private RecipeIngredientService recipeIngredientServiceMock;
    private RecipeService recipeServiceMock;

    @BeforeEach
    public void setup() {
        recipeIngredientServiceMock = mock(RecipeIngredientService.class);
        recipeServiceMock = mock(RecipeService.class);
        sut = new RecipeIngredientController(recipeIngredientServiceMock, recipeServiceMock);
    }

    @Test
    public void testGetAllRecipeIngredients() {
        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setRecipeIngredientId(1L);
        ri1.setIngredient(new Ingredient("Tomato"));
        ri1.setUnit(Unit.g);
        ri1.setAmount(100);

        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.setRecipeIngredientId(2L);
        ri2.setIngredient(new Ingredient("Salt"));
        ri2.setUnit(Unit.pinch);
        ri2.setAmount(1);

        List<RecipeIngredient> ingredients = List.of(ri1, ri2);

        when(recipeIngredientServiceMock.findAll()).thenReturn(ingredients);

        var result = sut.getAllRecipeIngredients();

        assertEquals(2, result.size());
        assertEquals(ri1, result.get(0));
        assertEquals(ri2, result.get(1));
        verify(recipeIngredientServiceMock).findAll();
    }

    @Test
    public void testGetRecipeByIngredientID() {
        Recipe r1 = new Recipe();
        r1.setRecipeID(10L);
        r1.setRecipeName("Pasta");

        Recipe r2 = new Recipe();
        r2.setRecipeID(20L);
        r2.setRecipeName("Salad");

        List<Recipe> recipes = List.of(r1, r2);

        when(recipeServiceMock.findRecipeByIngredientID(5L)).thenReturn(recipes);

        var result = sut.getRecipeByIngredientID(5L);

        assertEquals(2, result.size());
        assertEquals(r1, result.get(0));
        assertEquals(r2, result.get(1));

        verify(recipeServiceMock).findRecipeByIngredientID(5L);
    }


}
