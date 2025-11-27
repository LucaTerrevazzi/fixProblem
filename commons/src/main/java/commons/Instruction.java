package commons;

import java.util.Objects;

public class Instruction {


    private final int InstructionID;
    private String description;
    private int recipeID;
    private int orderNumber;

    /**
     * constructor
     * @param instructionID the unique instruction id number
     * @param description the discription on what to do for the step
     * @param recipeID the unique id for which recipe the step is
     * @param orderNumber the order of the steps
     */
    public Instruction(int instructionID, String description, int recipeID, int orderNumber) {
        InstructionID = instructionID;
        this.description = description;
        this.recipeID = recipeID;
        this.orderNumber = orderNumber;
    }

    public int getInstructionID() {
        return InstructionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return InstructionID == that.InstructionID
                && recipeID == that.recipeID
                && orderNumber == that.orderNumber
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(InstructionID, description, recipeID, orderNumber);
    }
}