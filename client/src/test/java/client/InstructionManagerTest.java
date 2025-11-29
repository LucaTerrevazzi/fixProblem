package client;

import client.utils.InstructionManager;
import commons.Instruction;
import commons.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InstructionManagerTest {

    private InstructionManager instructionManager;

    @BeforeEach
    public void setUp() {
        instructionManager = new InstructionManager();
    }

    private Instruction makeInstr(int id, String text, int recipeId, int order) {
        Recipe r = new Recipe(recipeId);
        Instruction instr = new Instruction(text, order, r);
        instr.setInstructionID(id);
        return instr;
    }

    @Test
    public void testAddInstruction() {
        Instruction instr1 = makeInstr(1, "Instruction 1", 1, 1);
        instructionManager.addInstruction(instr1);

        List<Instruction> instructionList = instructionManager.getInstructions();
        assertEquals(1, instructionList.size());
        assertEquals("Instruction 1", instructionList.get(0).getDescription());
    }

    @Test
    public void testRemoveInstruction() {
        Instruction instr1 = makeInstr(1, "Instruction 1", 1, 1);
        Instruction instr2 = makeInstr(2, "Instruction 2", 2, 2);

        instructionManager.addInstruction(instr1);
        instructionManager.addInstruction(instr2);

        boolean deleted = instructionManager.deleteInstruction(1);
        List<Instruction> instructionList = instructionManager.getInstructions();

        assertTrue(deleted);
        assertEquals(1, instructionList.size());
        assertEquals(1, instructionList.get(0).getOrderNumber());
        assertEquals(2, instructionList.get(0).getInstructionID());
    }

    @Test
    public void testEditInstruction() {
        Instruction instr1 = makeInstr(1, "Instruction 1", 1, 1);
        instructionManager.addInstruction(instr1);

        boolean edited = instructionManager.editInstruction(1, "Instruction 2");
        List<Instruction> instructionList = instructionManager.getInstructions();

        assertTrue(edited);
        assertEquals("Instruction 2", instructionList.get(0).getDescription());
    }

    @Test
    public void testOrderNumbersAfterAddAndDelete() {
        Instruction instr1 = makeInstr(1, "Instruction 1", 1, 1);
        Instruction instr2 = makeInstr(2, "Instruction 2", 2, 2);
        Instruction instr3 = makeInstr(3, "Instruction 3", 3, 3);

        instructionManager.addInstruction(instr1);
        instructionManager.addInstruction(instr2);
        instructionManager.addInstruction(instr3);

        List<Instruction> instructionList = instructionManager.getInstructions();
        assertEquals(1, instructionList.get(0).getOrderNumber());
        assertEquals(2, instructionList.get(1).getOrderNumber());
        assertEquals(3, instructionList.get(2).getOrderNumber());

        instructionManager.deleteInstruction(2);

        assertEquals(1, instructionList.get(0).getOrderNumber());
        assertEquals(2, instructionList.get(1).getOrderNumber());
    }
    //Used AI to alter the tests sto that they fit the new Classes compatible with jakarta annotations

}
