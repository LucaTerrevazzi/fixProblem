package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class RecipeTest {

    @Test
    public void testEmptyConstructor() {
        Recipe r = new Recipe();
        assertNotNull(r.getSteps());
        assertNotNull(r.getIngredients());
    }

    @Test
    public void testConstructorWithID() {
        Recipe r = new Recipe(42);
        assertEquals(42, r.getRecipeID());
    }

    @Test
    public void testFullConstructor() {
        // Your Instruction constructor: (int id, String description, int recipeID, int orderNumber)
        List<Instruction> instr = List.of(
                new Instruction(1, "Cut", 1, 1),
                new Instruction(2, "Mix", 1, 2)
        );

        List<RecipeIngredient> ingr = new ArrayList<>();

        Recipe r = new Recipe(1, "Test", instr, ingr);

        assertEquals(1, r.getRecipeID());
        assertEquals("Test", r.getRecipeName());
        assertEquals(instr, r.getSteps());       // works because you store the reference directly
        assertEquals(ingr, r.getIngredients());  // same
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

        Instruction step = new Instruction(1,"Heat water",1,1);


        r.setSteps(new ArrayList<>(List.of(step)));

        assertEquals(1, r.getSteps().size());
        assertEquals("Heat water", r.getSteps().get(0).getDescription());
    }


    @Test
    public void testSetIngredients() {
        Recipe r = new Recipe();
        r.setIngredients(new ArrayList<>());
        assertEquals(0, r.getIngredients().size());
    }

    //@Test
    //public void testDeleteInstruction() {
    //    Recipe r = new Recipe();
    //    r.addInstruction("Step 1");
    //    r.addInstruction("Step 2");
    //
    //    r.deleteInstructionByIndex(0);
    //
    //    assertEquals(1, r.getInstructions().size());
    //    assertEquals("Step 2", r.getInstructions().get(0));
    //}

//
//    @Test
//    public void testCopyIsDeep() {
//        Recipe r = new Recipe(5, "Cake",
//                new ArrayList<>(List.of("Mix", "Bake")),
//                new ArrayList<>(List.of(new RecipeIngredient(new Ingredient("Sugar"), Unit.g, 50)))
//        );
//
//        Recipe copy = r.copy();
//
//        assertEquals(r, copy);
//    }

    @Test
    public void testEquals() {
        // Create instruction list
        List<Instruction> instr = List.of(
                new Instruction(1,"Boil water",1,1));

        // Create empty ingredients list
        List<RecipeIngredient> ingr = new ArrayList<>();

        // Use the corrected constructor
        Recipe r1 = new Recipe(1, "Tea", instr, ingr);
        Recipe r2 = new Recipe(1, "Tea", instr, ingr);

        // Compare
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testEqualsDifferent() {
        Recipe r1 = new Recipe(1);
        Recipe r2 = new Recipe(2);

        assertNotEquals(r1, r2);
    }



}
