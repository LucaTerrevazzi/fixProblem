package commons;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "instruction")
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructionID;
    @Column(nullable = false)
    private String description;

    // In order for the instruction and recipe to be linked JPA needs a recipe attribute
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipeID")
    private Recipe recipe;

    @Column(nullable = false)
    private int orderNumber;

    /**
     * This constructor creates an Instruction with only an id.
     */
    public Instruction() {}

    /**
     * Constructor for the Instruction class
     * @param description description of the specific step
     * @param orderNumber the index of the instruction (in which order it will get executed)
     * @param recipe the recipe it's linked with
     */
    public Instruction(String description, int orderNumber, Recipe recipe) {
        this.description = description;
        this.orderNumber = orderNumber;
        this.recipe = recipe;
    }

    public Long getInstructionID() {
        return instructionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setInstructionID(Long id) {
        this.instructionID = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return instructionID == that.instructionID && orderNumber == that.orderNumber && Objects.equals(description, that.description) && Objects.equals(recipe, that.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionID, description, recipe, orderNumber);
    }
}