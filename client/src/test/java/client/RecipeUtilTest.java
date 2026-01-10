package client;

import client.utils.RecipeUtil;
import commons.Ingredient;
import commons.Recipe;
import commons.RecipeIngredient;
import commons.Unit;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeUtilTest {

    @Test
    public void testAddInstruction() {
        Recipe r = new Recipe();
        var i = new commons.Instruction();
        i.setDescription("Chop onions");
        RecipeUtil.addInstructionAt(r,i, 0);

        assertEquals(1, r.getSteps().size());
        assertEquals("Chop onions", r.getSteps().getFirst().getDescription());
    }

    @Test
    public void testAddIngredient() {
        Recipe r = new Recipe();
        var ing = new Ingredient("Flour");
        RecipeUtil.addIngredient(r, new RecipeIngredient(ing, Unit.g,  100));

        assertEquals(1, r.getIngredients().size());
        assertEquals(ing, r.getIngredients().getFirst().getIngredient());
    }

    @Test
    public void testEmptyInputToOutput() {
        Recipe r = new Recipe();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        RecipeUtil.outputRecipeStream(r, baos);
        String s = baos.toString();

        assertEquals("", s);
    }

    @Test
    public void testRecipeToOutput() {
        Recipe r = new Recipe();
        r.setRecipeName("Soup");

        var i = new commons.Instruction();
        i.setDescription("Heat water");
        RecipeUtil.addInstructionAt(r,i, 0);

        var i2 = new commons.Instruction();
        i2.setDescription("Add vegetables");
        RecipeUtil.addInstructionAt(r,i2, 1);

        RecipeUtil.addIngredient(r, new RecipeIngredient(
                new Ingredient("Carrot"), Unit.g,  200));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        RecipeUtil.outputRecipeStream(r, baos);

        assertEquals("""
                ### Soup
                
                ## Ingredients:
                - 200g of Carrot;
                
                
                ## Instructions:
                1) Heat water
                2) Add vegetables
                
                _Good luck with the preparation!_""", baos.toString());
    }

    @Test
    public void testToStringOnlyName() {
        Recipe r = new Recipe();
        r.setRecipeName("Soup");

        assertEquals("""
                ### Soup""", RecipeUtil.toMarkdown(r));
    }

    @Test
    public void testEmptyToString() {
        Recipe r = new Recipe();

        assertEquals("", RecipeUtil.toMarkdown(r));
    }

    @Test
    public void testToStringNoInstructions() {
        Recipe r = new Recipe();
        r.setRecipeName("Soup");

        RecipeUtil.addIngredient(r, new RecipeIngredient(
                new Ingredient("Carrot"), Unit.g,  100));

        assertEquals("""
                ### Soup

                ## Ingredients:
                - 100g of Carrot;
                """, RecipeUtil.toMarkdown(r));
    }

    @Test
    public void testToString() {
        Recipe r = new Recipe();
        r.setRecipeName("Soup");

        var i = new commons.Instruction();
        i.setDescription("Heat water");
        RecipeUtil.addInstructionAt(r,i, 0);

        var i2 = new commons.Instruction();
        i2.setDescription("Add vegetables");
        RecipeUtil.addInstructionAt(r,i2, 1);

        RecipeUtil.addIngredient(r, new RecipeIngredient(
                new Ingredient("Carrot"), Unit.pinch,  2));

        assertEquals("""
                ### Soup

                ## Ingredients:
                - 2pinch of Carrot;
                

                ## Instructions:
                1) Heat water
                2) Add vegetables

                _Good luck with the preparation!_""", RecipeUtil.toMarkdown(r));
    }

}
