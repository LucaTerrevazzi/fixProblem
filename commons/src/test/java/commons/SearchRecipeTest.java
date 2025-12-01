package commons;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

class SearchRecipeTest {

    @Test
    void testEmptyQueryReturnsEmpty() {
        Recipe r1 = new Recipe("Cake");
        Recipe r2 = new Recipe("Pie");
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "");
        assertTrue(result.isEmpty());
    }

    @Test
    void testQueryMatchesName() {
        Recipe r1 = new Recipe("Chocolate Cake");
        Recipe r2 = new Recipe("Vanilla Cake");
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Chocolate");
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));
    }

    @Test
    void testQueryMatchesIngredient() {
        Recipe r1 = new Recipe("Cake");
        r1.setIngredients(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 200)));
        Recipe r2 = new Recipe("Pie");
        r2.setIngredients(List.of(new RecipeIngredient(new Ingredient("milk"), Unit.L, 1)));
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Sugar");
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));
    }

    @Test
    void testQueryMatchesInstruction() {
        Recipe r1 = new Recipe("Cake");
        r1.getSteps().add(new Instruction("Mix ingredients", 0, r1));
        Recipe r2 = new Recipe("Pie");
        r2.getSteps().add(new Instruction("Bake", 0, r2));
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Mix");
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));
    }

    @Test
    void testMultipleWordsInQuery() {
        Recipe r1 = new Recipe("Chocolate Cake");
        r1.setIngredients(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 200)));
        Recipe r2 = new Recipe("Pie");
        r2.setSteps(List.of(new Instruction("Bake", 0, r2)));
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Chocolate Bake");
        assertEquals(2, result.size());
        assertEquals(r1, result.get(0));
        assertEquals(r2, result.get(1));
    }

    @Test
    void testRecipeRemovedIfScoreZero() {
        Recipe r1 = new Recipe("Cake");
        Recipe r2 = new Recipe("Pie");
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Banana");
        assertTrue(result.isEmpty());
    }

    @Test
    void testOrderByScore() {
        Recipe r1 = new Recipe("Chocolate Cake");
        Recipe r2 = new Recipe("Chocolate Pie");
        r1.setIngredients(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 200)));
        r2.setIngredients(List.of(new RecipeIngredient(new Ingredient("Honey"), Unit.g, 200)));
        List<Recipe> recipes = List.of(r1, r2);
        List<Recipe> result = Recipe.search(recipes, "Chocolate Sugar");
        assertEquals(r1, result.get(0));
        assertEquals(r2, result.get(1));
    }

    @Test
    void testSplitQueryOnMultipleSeparators() {
        Recipe r1 = new Recipe("Cake");
        r1.setIngredients(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 200)));
        List<Recipe> recipes = List.of(r1);
        List<Recipe> result = Recipe.search(recipes, "Sugar, Cake; Mix");
        assertEquals(1, result.size());
        assertEquals(r1, result.get(0));
    }
}
