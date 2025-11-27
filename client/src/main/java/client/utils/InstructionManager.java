package client.utils;

import commons.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionManager {
    private List<Instruction> instructions = new ArrayList<>();

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    /**
     * methode to add an instruction
     * @param instr the new instruction
     */
    public void addInstruction(Instruction instr) {
        instructions.add(instr);
        updateOrderNumbers();
    }

    /**
     * methode to delete a instruction, it checks to see it the id exists first
     * @param instructionID the id of the instruction to be deleted
     * @return if it is deleted or not
     */
    @SuppressWarnings("checkstyle:WhitespaceAround")
    public boolean deleteInstruction(int instructionID){
        for(int i = 0; i < instructions.size(); i++){
            if(instructions.get(i).getInstructionID() == instructionID){
                instructions.remove(i);
                updateOrderNumbers();
                //System.out.println("Deleted successfully!");
                return true;
            }
        }
        //System.out.println("Couldn't delete instruction!");
        return false;
    }

    /**
     * methode to edit an instruction
     * @param instructionID the id of the instruction to be edited
     * @param newDescription the new description
     * @return boolean to check if the id of instruction exists
     */
    public boolean editInstruction(int instructionID, String newDescription){
        for(Instruction instruction : instructions){
            if(instruction.getInstructionID() == instructionID){
                instruction.setDescription(newDescription);
                updateOrderNumbers();
                return true;
            }
        }
        return false;
    }

    /**
     * private methode so that the order is being tracked and updated
     */
    private void updateOrderNumbers(){
        instructions.sort((a,b) -> a.getOrderNumber() - b.getOrderNumber());
        for(int i = 0; i < instructions.size(); i++){
            instructions.get(i).setOrderNumber(i + 1);
        }
    }
}