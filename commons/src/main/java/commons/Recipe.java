package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recipeID;
    private String recipeName;
    private List<Instruction> steps = new ArrayList<>();
    private List<RecipeIngredient> ingredients = new ArrayList<>();


    /**
     * This constructor creates a Recipe without any attributes set.
     */
    public Recipe() {}

    /**
     * This constructor creates an empty Recipe.
     * @param recipeID the unique ID of the recipe (e.g. 3284)
     */
    public Recipe(int recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Constructor of a new recipe
     * @param recipeID ID of recipe
     * @param recipeName Name of the recipe
     * @param steps list of steps
     * @param ingredients ingredients of recipe
     */
    public Recipe(int recipeID, String recipeName, List<Instruction> steps, List<RecipeIngredient> ingredients) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.steps = steps;
        this.ingredients = ingredients;
    }


    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
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

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeID=" + recipeID +
                ", recipeName='" + recipeName + '\'' +
                ", steps=" + steps +
                ", ingredients=" + ingredients +
                '}';
    }

    /**
     * This equals-method has to change when the attributes instruction and ingredients will be added.
     * @param o   the reference object with which to compare.
     * @return a boolean telling whether the tho recipes are exactly the same (also the ID).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return recipeID == recipe.recipeID &&
                Objects.equals(recipeName, recipe.recipeName) &&
                Objects.equals(steps, recipe.steps) &&
                Objects.equals(ingredients, recipe.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeID, recipeName, steps, ingredients);
    }
}
