package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InstructionTest {

    @Test
    public void testConstructor() {
        Recipe recipe = new Recipe(2);
        Instruction instruction = new Instruction("flip", 1, recipe);
        instruction.setInstructionID(3);

        assertEquals(3, instruction.getInstructionID());
        assertEquals("flip", instruction.getDescription());
        assertEquals(2, instruction.getRecipe().getRecipeID());
        assertEquals(1, instruction.getOrderNumber());
    }

    @Test
    public void testSetRecipeID() {
        Recipe recipe = new Recipe(2);
        Instruction instruction = new Instruction("flip", 1, recipe);
        instruction.setInstructionID(3);

        instruction.getRecipe().setRecipeID(1);
        assertEquals(1, instruction.getRecipe().getRecipeID());
    }

    @Test
    public void testSetDescription() {
        Recipe recipe = new Recipe(2);
        Instruction instruction = new Instruction("flip", 1, recipe);
        instruction.setInstructionID(3);

        instruction.setDescription("bake");
        assertEquals("bake", instruction.getDescription());
    }

    @Test
    public void testSetOrderNumber() {
        Recipe recipe = new Recipe(2);
        Instruction instruction = new Instruction("flip", 1, recipe);
        instruction.setInstructionID(3);

        instruction.setOrderNumber(4);
        assertEquals(4, instruction.getOrderNumber());
    }

    @Test
    public void testEquals() {
        Recipe recipe1 = new Recipe(2);
        Recipe recipe2 = new Recipe(2);

        Instruction instruction1 = new Instruction("flip", 1, recipe1);
        Instruction instruction2 = new Instruction("flip", 1, recipe2);

        instruction1.setInstructionID(3);
        instruction2.setInstructionID(3);

        assertEquals(instruction1, instruction2);
        assertEquals(instruction1.hashCode(), instruction2.hashCode());
    }
    //Used AI to adapt the tests to the new
}
