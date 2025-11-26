package commons;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe {

    private int recipeID;
    private String recipeName;
    private List<String> instructions = new ArrayList<>();
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
     * This constructor creates a non-empty Recipe.
     * @param recipeID The unique ID of the recipe (e.g. 3284)
     * @param recipeName The name of the recipe (e.g. "Pizza Regina")
     * @param instructions A list of instructions (e.g. "Put it in the oven")
     */
    public Recipe(int recipeID, String recipeName, List<String> instructions, List<RecipeIngredient> ingredients) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.instructions = instructions;
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

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public void addInstruction(String instruction) {
        instructions.add(instruction);
    }

    public void deleteInstructionByIndex(int index){
        instructions.remove(index);
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(RecipeIngredient newIngredient) {
        ingredients.add(newIngredient);
    }

    //public void deleteIngredientById(int id){
    //    for(RecipeIngredient i : ingredients){
    //        if (i.id == id){
    //            ingredients.remove(i);
    //        }
    //    }
    //}

    /**
     * Copy the exact same recipe.
     * @return a new independent Recipe with the same instructions and ingredients.
     */
    public Recipe copy() {
        Recipe r = new Recipe();
        r.setRecipeID(this.recipeID);
        r.setRecipeName(this.recipeName);
        r.setInstructions(this.instructions);

        // Deep copy des ingrédients
        List<RecipeIngredient> newIngredients = new ArrayList<>();
        for (RecipeIngredient ing : this.ingredients) {
            newIngredients.add(ing.copy());
        }
        r.setIngredients(newIngredients);

        return r;
    }

    /**
     * Create a printable version of the recipe (Markdown).
     * An example of return string can be :
     *
     *
     *                 ### Soup
     *
     *                 ## Ingredients:
     *                 - 2g of sugar;
     *
     *                 ## Instructions:
     *                 1) Heat water
     *                 2) Add sugar
     *
     *                 _Good luck with the preparation!_
     *
     * @return a string containing the recipe.
     */
    public String toString(){
        String result = "### ";
        result += this.recipeName;
        result += "\n\n## Ingredients:\n";
        for(RecipeIngredient i:this.ingredients){
            result += "- ";
            result += String.valueOf(i.getAmount());
            result += i.getUnit().name();
            result += " of ";
            result += i.getIngredient().getIngredientName();
            result += ";";
            result += "\n";
        }
        result += "\n## Instructions:\n";
        for(int i = 0; i<instructions.size(); i++){
            result += String.valueOf(i+1);
            result += ") ";
            result += this.instructions.get(i);
            result += "\n";
        }
        result += "\n_Good luck with the preparation!_";
        return result;
    }




    /**
     * This equals-method has to change when the attributes instruction and ingredients will be added.
     * @param o   the reference object with which to compare.
     * @return a boolean telling whether the tho recipes are exactly the same (also the ID).
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return getRecipeID() == recipe.getRecipeID() && Objects.equals(getRecipeName(), recipe.getRecipeName()) && Objects.equals(getInstructions(), recipe.getInstructions()) && Objects.equals(getIngredients(), recipe.getIngredients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipeID(), getRecipeName(), getInstructions(), getIngredients());
    }
}
