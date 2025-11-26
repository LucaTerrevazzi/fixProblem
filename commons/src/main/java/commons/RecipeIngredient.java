package commons;

import java.util.Objects;

/**
 *Represents an ingredient used in a recipe, with the type of ingredient used,
 * measurement unit and amount of ingredient.
 */
public class RecipeIngredient {
    private Ingredient ingredient;
    private Unit unit;
    private int amount;

    /**
     *  RecipeIngredient Constructor
     * @param ingredient The type of ingredient
     * @param unit The measurement unit that is used
     * @param amount The amount of ingredient used in the recipe
     */
    public RecipeIngredient(Ingredient ingredient, Unit unit, int amount) {
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    /**
     * Getter for the type of ingredient
     * @return The type of ingredient
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * Setter for the type of ingredient
     * @param ingredient The new type of ingredient to be set
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * Getter for the measurement unit that is used
     * @return The measurement unit that is used
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Setter for the measurement unit that is used
     * @param unit The measurement unit to be set
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * Getter for the amount of ingredient used in the recipe
     * @return The amount of ingredient used in the recipe
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of ingredient used in the recipe
     * @param amount The amount of ingredient to be set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Creates a copy of the ingredient.
     * @return a new recipe ingredient, with exactly the same attributes.
     */
    public RecipeIngredient copy(){
        return new RecipeIngredient(ingredient, unit, amount);
    }

    @Override
    public String toString() {
        return Integer.toString(amount) + unit + " " + ingredient.getIngredientName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return amount == that.amount && Objects.equals(ingredient, that.ingredient) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, unit, amount);
    }
}
