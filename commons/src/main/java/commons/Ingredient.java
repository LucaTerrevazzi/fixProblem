package commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an ingredient going to be used in a specific recipe
 */
@Entity
@Table(name = "ingredient")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientID;

    @Column(nullable = false, unique = true)
    private String ingredientName;

    @Column(nullable = false)
    private String ingredientLanguage;

    @Column(nullable = false)
    private double fat;

    @Column(nullable = false)
    private double protein;

    @Column(nullable = false)
    private double carbs;

    /**
     * No argument constructor that's required for JPA
     */
    public Ingredient() {}

    /**
     * Constructor for Ingredient
     * @param ingredientName name of the ingredient
     */
    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
        this.fat = 0;
        this.protein = 0;
        this.carbs = 0;
        this.ingredientLanguage = "No language specified";
    }

    public Long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(Long ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientLanguage() {
        return ingredientLanguage;
    }

    public void setIngredientLanguage(String ingredientLanguage) {
        this.ingredientLanguage = ingredientLanguage;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    /**
     * Calories calculator
     * @return total nutritional value/calories in 100gr
     */
    @JsonProperty(value = "kcal", access = JsonProperty.Access.READ_ONLY)
    public double getKcal() {
        return fat * 9 + protein * 4 + carbs * 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(that.fat, fat) == 0
                && Double.compare(that.protein, protein) == 0
                && Double.compare(that.carbs, carbs) == 0
                && Objects.equals(ingredientID, that.ingredientID)
                && Objects.equals(ingredientName, that.ingredientName)
                && Objects.equals(ingredientLanguage, that.ingredientLanguage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientID, ingredientName,
                fat, protein, carbs, ingredientLanguage);
    }
}
