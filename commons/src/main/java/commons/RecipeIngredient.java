package commons;

import java.util.Objects;

public class RecipeIngredient {
    private Ingredient ingredient;
    private Unit unit;
    private int amount;

    public RecipeIngredient(Ingredient ingredient, Unit unit, int amount) {
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
