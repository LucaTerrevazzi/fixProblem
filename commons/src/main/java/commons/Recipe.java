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
    private int recipeID;
    @Column(nullable = false)
    private String recipeName;

    // One recipe - many instructions => One to Many
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNumber ASC") // ascending order of instructions
    private List<Instruction> steps = new ArrayList<>();

    // one instruction - many ingredients => One to Many
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
    public Recipe(int recipeID) {
        this.recipeID = recipeID;
    }

    /**
     * Constructor of a new recipe
     * @param recipeName Name of the recipe
     */
    public Recipe( String recipeName) {
        this.recipeName = recipeName;
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
     * Search for recipes with the most relevance.
     * The name of the recipe is 100 times more important than the ingredients.
     * Ingredients are 2 times more important than instructions,
     * because a user will not search for "Mix", "Add" or "Bake".
     * @param recipes all the recipes that exist.
     * @param query the text we are looking for
     * @return the recipes that fit the query, in the correct order.
     */
    public static ArrayList<Recipe> search(List<Recipe> recipes, String query){
        if (query == null || query.isBlank()) {
            return new ArrayList<>();
        }
        String[] words = query.split("[ ,;.?]+"); //Will split "a b,c;d.e?f-g" into "a", "b", "c", "d", "e", "f-g"
        int[] scores = new int[recipes.size()];
        for (String w: words){
            for (int i = 0; i<recipes.size(); i++){
                Recipe recipe = recipes.get(i);
                if (recipe.getRecipeName().contains(w)){
                    scores[i] += 200;
                }
                for (RecipeIngredient ing: recipe.getIngredients()){
                    if(ing.getIngredient().getIngredientName().contains(w)){
                        scores[i] += 2;
                    }
                }
                for (Instruction instr: recipe.getSteps()){
                    if(instr.getDescription().contains(w)){
                        scores[i] += 1;
                    }
                }
            }
        }
        ArrayList<Recipe> result = new ArrayList<Recipe>(recipes);
        for(int i = recipes.size()-1; i>=0; i--){
            if(scores[i] == 0){
                result.remove(i);
            }
        }
        result.sort((r1, r2) -> Integer.compare(
                scores[recipes.indexOf(r2)], scores[recipes.indexOf(r1)]
        ));
        return result;
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
