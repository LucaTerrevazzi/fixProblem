package client.utils;

import commons.Instruction;
import commons.Recipe;
import commons.RecipeIngredient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for utilisation of the recipe class
 */
public class RecipeUtil {

    /**
     * Add instruction to specific index
     * @param recipe recipe
     * @param instruction new instruction
     * @param index new instruction index
     */
    public static void addInstructionAt(Recipe recipe, Instruction instruction, int index) {
        recipe.getSteps().add(index, instruction);
        updateOrderNumbers(recipe);
    }

    /**
     * Delete an instruction from the recipe by index
     */
    public static void deleteInstructionByIndex(Recipe recipe, int index) {
        recipe.getSteps().remove(index);
        updateOrderNumbers(recipe);
    }

    /**
     * Update instrution order number after step alterations
     * @param recipe recipe
     */
    private static void updateOrderNumbers(Recipe recipe) {
        List<Instruction> steps = recipe.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setOrderNumber(i + 1);
        }
    }

    /**
     * Add a RecipeIngredient to the recipe.
     */
    public static void addIngredient(Recipe recipe, RecipeIngredient newIngredient) {
        recipe.getIngredients().add(newIngredient);
    }
    /**
     * Builds the steps description using instruction.getDescription
     */
    public static String buildInstructionDescription(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        List<Instruction> steps = recipe.getSteps();

        for (int i = 0; i < steps.size(); i++) {
            Instruction step = steps.get(i);
            sb.append(i + 1)
                    .append(") ")
                    .append(step.getDescription())
                    .append("\n");
        }

        return sb.toString().trim();
    }

    /**
     * Creates a deep copy of the recipe
     */
    public static Recipe copy(Recipe original) {
        Recipe r = new Recipe();
        r.setRecipeID(original.getRecipeID());
        r.setRecipeName(original.getRecipeName());

        ArrayList<Instruction> newSteps = new ArrayList<>(original.getSteps());
        r.setSteps(newSteps);

        List<RecipeIngredient> newIngredients = new ArrayList<>();
        for (RecipeIngredient ing : original.getIngredients()) {
            newIngredients.add(ing.copy());
        }
        r.setIngredients(newIngredients);

        return r;
    }

    /**
     * Markdown export of a recipe.
     */
    public static String toMarkdown(Recipe recipe) {
        String result = "";

        if(recipe == null){
            return result;
        }

        if (recipe.getRecipeName() != null) {
            result += "### ";
            result += recipe.getRecipeName();
        }

        if (!recipe.getIngredients().isEmpty()) {
            result += "\n\n## Ingredients:\n";
            for (RecipeIngredient i : recipe.getIngredients()) {
                result += "- ";
                result += i.getAmount();
                result += i.getUnit().name();
                result += " of ";
                result += i.getIngredient().getIngredientName();
                result += ";\n";
            }
        }

        if (!recipe.getSteps().isEmpty()) {
            result += "\n\n## Instructions:\n";
            result += buildInstructionDescription(recipe);
            result += "\n\n_Good luck with the preparation!_";
        }

        return result;
    }

    /**
     * Saves the recipe to a file
     * @param os The provided OutputStream (normally FileOutputStream for outputting a file)
     */
    public static void outputRecipeStream(Recipe r, OutputStream os) {
        try {
            os.write(toMarkdown(r).getBytes());
        } catch (IOException e) {
            System.out.println("Could not write to the file provided");
        }
    }
}
