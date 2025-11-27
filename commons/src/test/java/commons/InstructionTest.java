package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstructionTest {

    @Test
    public void testConstructor() {
        Instruction instruction = new Instruction(3, "flip", 2,1);
        assertEquals(instruction.getInstructionID(), 3);
        assertEquals(instruction.getDescription(), "flip");
        assertEquals(instruction.getRecipeID(), 2);
        assertEquals(instruction.getOrderNumber(), 1);
    }

    @Test
    public void testsetRecipeID() {
        Instruction instruction = new Instruction(3, "flip", 2,1);
        instruction.setRecipeID(1);
        assertEquals(instruction.getRecipeID(), 1);
    }

    @Test
    public void testsetDescription() {
        Instruction instruction = new Instruction(3, "flip", 2,1);
        instruction.setDescription("bake");
        assertEquals(instruction.getDescription(), "bake");
    }

    @Test
    public void testsetOrderNumber() {
        Instruction instruction = new Instruction(3, "flip", 2,1);
        instruction.setOrderNumber(4);
        assertEquals(instruction.getOrderNumber(), 4);
    }

    @Test
    public void testEquals() {
        Instruction instruction1 = new Instruction(3, "flip", 2,1);
        Instruction instruction2 = new Instruction(3, "flip", 2,1);

        assertEquals(instruction1, instruction2);
        assertEquals(instruction1.hashCode(), instruction2.hashCode());
    }
}
