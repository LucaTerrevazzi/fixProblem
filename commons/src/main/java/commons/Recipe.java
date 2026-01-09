package commons;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeID;
    @Column(nullable = false)
    private String recipeName;
    @Column(nullable = false)
    private String recipeLanguage;

    // One recipe - many instructions => One to Many
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNumber ASC") // ascending order of instructions
    private List<Instruction> steps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();


    /**
     * This constructor creates a Recipe without any attributes set.
     */
    public Recipe() {}

    /**
     * This constructor creates an empty Recipe.
     * @param recipeID the unique ID of the recipe (e.g. 3284)
     */
    public Recipe(Long recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Constructor of a new recipe
     * @param recipeName Name of the recipe
     */
    public Recipe(String recipeName) {
        this.recipeName = recipeName;
    }


    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Instruction> getSteps() {
        return steps;
    }

    public void setSteps(List<Instruction> steps) {
        this.steps = steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipeLanguage() {
        if (recipeLanguage == null) {
            recipeLanguage = "EN";
        }
        return recipeLanguage;
    }

    public void setRecipeLanguage(String recipeLanguage) {
        this.recipeLanguage = recipeLanguage;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeID=" + recipeID +
                ", recipeName='" + recipeName + '\'' +
                ", steps=" + steps +
                ", ingredients=" + ingredients +
                ", language='" + recipeLanguage + '\'' +
                '}';
    }

    /**
     * This equals-method has to change when the attributes
     * instruction and ingredients will be added.
     * @param o   the reference object with which to compare.
     * @return a boolean telling whether the tho recipes are exactly the same (also the ID).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe other)) return false;
        if (recipeID != null && other.recipeID != null) {
            return Objects.equals(recipeID, other.recipeID);
        }
        return Objects.equals(recipeName, other.recipeName)
                && Objects.equals(recipeLanguage, other.recipeLanguage)
                && Objects.equals(steps, other.steps)
                && Objects.equals(ingredients, other.ingredients);
    }


}
