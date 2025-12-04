package commons;
import jakarta.persistence.*;
import java.util.Objects;
/**
 * Represents an ingredient going to be used in a specific recipe
 */
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientID;

    @Column(nullable = false, unique = true) //column that cannot be null => its a required field
    private String ingredientName;

    @Column(nullable = false)
    private double fat;
    @Column(nullable = false)
    private double protein;

    @Column(nullable = false)
    private double carbs;

    /**
     * No arguement constructor that's required for JPA
     */
    public Ingredient() {}

    /**
     * Constructor for Inrgedient
     * @param ingredientName name of the ingredient
     */
    public Ingredient( String ingredientName ) {
        this.ingredientName = ingredientName;
        this.fat = 0; // set to 0 so we dont have problems with null database
        this.protein = 0;
        this.carbs = 0;
    }


    /**
     * Getter for name of the ingredient
     * @return name of the ingredient
     */
    public String getIngredientName() {
        return ingredientName;
    }

    public Long getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(Long ingredientID) {
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
     * @return total nutritional value/calories in 100gr (formula found in backlog)
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
