package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * Represents an ingredient going to be used in a specific recipe
 */
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ingredientID;

    private String ingredientName;
    private double fat;
    private double protein;
    private double carbs;

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

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public double getFat() {
        return fat;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    /**
     * Setter for name of the ingredient
     * @param ingredientName Name of the ingredient to be set
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * Calories calculator
     * @return total nutritional value/calories
     */
    public double getKcal() {
        return fat*9 + protein*4 + carbs*4;
    }

    /**
     * Equals method for ingredient
     * @param o   the reference object with which to compare.
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return ingredientID == that.ingredientID
                && Double.compare(fat, that.fat) == 0
                && Double.compare(protein, that.protein) == 0
                && Double.compare(carbs, that.carbs) == 0
                && Objects.equals(ingredientName, that.ingredientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientID, ingredientName,
                fat, protein, carbs);
    }
}
