package commons;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "instruction")
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int instructionID;
    @Column(nullable = false)
    private String description;

    // In order for the instruction and recipe to be link JPA needs a recipe attribute
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipeID")
    private Recipe recipe;

    @Column(nullable = false)
    private int orderNumber;

    /**
     * This constructor creates an Instruction with only an id.
     */
    public Instruction() {

    }

    /**
     * Constructorfor the Instruction class
     * @param description description of the specific step
     * @param orderNumber the index of the isntruction (in which order it will get exedcuted)
     * @param recipe the recipe its linked with
     */
    public Instruction(String description, int orderNumber, Recipe recipe) {
        this.description = description;
        this.orderNumber = orderNumber;
        this.recipe = recipe;
    }

    public int getInstructionID() {
        return instructionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setInstructionID(int id) {
        this.instructionID = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

}