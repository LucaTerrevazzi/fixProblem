package commons;

import java.util.Objects;

/**
 * Represents the nutritional profile for a given amount of food.
 * Values are expressed in grams and calories and can be freely
 * combined or scaled to match portion sizes.
 */
public class NutritionalProfile {

    private double fat;
    private double carbs;
    private double protein;

    public NutritionalProfile() {
        //for JPA
    }

    /**
     * Creates a new nutritional profile.
     *
     * @param fat     fat content in grams
     * @param protein protein content in grams
     * @param carbs   carbohydrate content in grams
     */
    public NutritionalProfile(double fat, double protein, double carbs) {
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }


    public double getFat() {return fat;}
    public double getProtein() {return protein;}
    public double getCarbs() {return carbs;}

    public void setCarbs(double carbs) {this.carbs = carbs;}
    public void setProtein(double protein) {this.protein = protein;}
    public void setFat(double fat) {this.fat = fat;}

    /**
     * Calories calculated using the standard formula 9*fat + 4*protein + 4*carbs.
     *
     * @return kcal for this profile
     */
    public double getKcal() {
        return fat * 9 + protein * 4 + carbs * 4;
    }

    /**
     * Adds the macronutrients of another profile to this one.
     *
     * @param other profile to add
     * @return combined profile
     */
    public NutritionalProfile add(NutritionalProfile other) {
        return new NutritionalProfile(
                fat + other.fat,
                protein + other.protein,
                carbs + other.carbs
        );
    }

    /**
     * Scales all nutrients by the provided factor.
     *
     * @param factor scaling factor (e.g. 0.5 for half a portion)
     * @return scaled profile
     */
    public NutritionalProfile scale(double factor) {
        return new NutritionalProfile(fat * factor, protein * factor, carbs * factor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NutritionalProfile that = (NutritionalProfile) o;
        return Double.compare(fat, that.fat) == 0
                && Double.compare(protein, that.protein) == 0
                && Double.compare(carbs, that.carbs) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fat, protein, carbs);
    }
}
