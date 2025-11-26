package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class RecipeTest {

    @Test
    public void testEmptyConstructor() {
        Recipe r = new Recipe();
        assertNotNull(r.getInstructions());
        assertNotNull(r.getIngredients());
    }

    @Test
    public void testConstructorWithID() {
        Recipe r = new Recipe(42);
        assertEquals(42, r.getRecipeID());
    }

    @Test
    public void testFullConstructor() {
        List<String> instr = List.of("Cut", "Mix");
        List<RecipeIngredient> ingr = new ArrayList<>();

        Recipe r = new Recipe(1, "Test", instr, ingr);

        assertEquals(1, r.getRecipeID());
        assertEquals("Test", r.getRecipeName());
        assertEquals(instr, r.getInstructions());
        assertEquals(ingr, r.getIngredients());
    }

    @Test
    public void testSetRecipeID() {
        Recipe r = new Recipe();
        r.setRecipeID(10);
        assertEquals(10, r.getRecipeID());
    }

    @Test
    public void testSetRecipeName() {
        Recipe r = new Recipe();
        r.setRecipeName("Pasta");
        assertEquals("Pasta", r.getRecipeName());
    }

    @Test
    public void testSetInstructions() {
        Recipe r = new Recipe();
        r.setInstructions(List.of("Boil water"));
        assertEquals(1, r.getInstructions().size());
        assertEquals("Boil water", r.getInstructions().get(0));
    }

    @Test
    public void testSetIngredients() {
        Recipe r = new Recipe();
        r.setIngredients(new ArrayList<>());
        assertEquals(0, r.getIngredients().size());
    }

    @Test
    public void testAddInstruction() {
        Recipe r = new Recipe();
        r.addInstruction("Chop onions");

        assertEquals(1, r.getInstructions().size());
        assertEquals("Chop onions", r.getInstructions().get(0));
    }

    @Test
    public void testDeleteInstruction() {
        Recipe r = new Recipe();
        r.addInstruction("Step 1");
        r.addInstruction("Step 2");

        r.deleteInstructionByIndex(0);

        assertEquals(1, r.getInstructions().size());
        assertEquals("Step 2", r.getInstructions().get(0));
    }

    @Test
    public void testAddIngredient() {
        Recipe r = new Recipe();
        RecipeIngredient ing = new RecipeIngredient(new Ingredient("Flour"), Unit.g, 100);

        r.addIngredient(ing);

        assertEquals(1, r.getIngredients().size());
        assertEquals(ing, r.getIngredients().get(0));
    }

    @Test
    public void testCopyIsDeep() {
        Recipe r = new Recipe(5, "Cake",
                new ArrayList<>(List.of("Mix", "Bake")),
                new ArrayList<>(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 50)))
        );

        Recipe copy = r.copy();

        assertEquals(r, copy);
    }

    @Test
    public void testEquals() {
        Recipe r1 = new Recipe(1, "Tea", List.of("Boil water"), new ArrayList<>());
        Recipe r2 = new Recipe(1, "Tea", List.of("Boil water"), new ArrayList<>());

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testEqualsDifferent() {
        Recipe r1 = new Recipe(1);
        Recipe r2 = new Recipe(2);

        assertNotEquals(r1, r2);
    }

    @Test
    public void testToString() {
        Recipe r = new Recipe();
        r.setRecipeName("Soup");
        r.addInstruction("Heat water");
        r.addInstruction("Add vegetables");

        r.addIngredient(new RecipeIngredient(new Ingredient("Carrot"), Unit.pinch,  2));

        String s = r.toString();

        assertEquals("""
                ### Soup
                
                ## Ingredients:
                - 2pinch of Carrot;
                
                ## Instructions:
                1) Heat water
                2) Add vegetables
                
                _Good luck with the preparation!_""", s);
    }
}
