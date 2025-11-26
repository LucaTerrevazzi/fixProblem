package commons;

import java.util.Objects;

/**
 * Represents an ingredient type to be used in many recipes via RecipeIngredients
 */
public class Ingredient {
    private String ingredientName;

    /**
     * Constructor for Ingredient
     * @param ingredientName A String name of the ingredient
     */
    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * Getter for name of the ingredient
     * @return name of the ingredient
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * Setter for name of the ingredient
     * @param ingredientName Name of the ingredient to be set
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(ingredientName, that.ingredientName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ingredientName);
    }
}
