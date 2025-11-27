package client.utils;

import commons.Instruction;
import commons.Recipe;
import commons.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for utilisation of the recipe class
 */
public class RecipeUtil {

    /**
     * Add instruction to specific place
     * @param recipe recipe
     * @param instruction new instruction
     * @param index new instruction index
     */
    public static void addInstructionAt(Recipe recipe, Instruction instruction, int index) {
        recipe.getSteps().add(index, instruction);
        updateOrderNumbers(recipe);
    }

    /**
     * Delete an instruction from the recipe by index.
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
     * Equivalent of original addIngredient()
     */
    public static void addIngredient(Recipe recipe, RecipeIngredient newIngredient) {
        recipe.getIngredients().add(newIngredient);
    }
    /**
     * Builds a plain-text description of the recipe instructions,
     * using instruction.getDescription().
     *
     * Example:
     * 1) Heat water
     * 2) Add vegetables
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
     * Creates a deep copy of the recipe.
     * EXACT same behavior you originally wrote.
     */
    public static Recipe copy(Recipe original) {
        Recipe r = new Recipe();
        r.setRecipeID(original.getRecipeID());
        r.setRecipeName(original.getRecipeName());

        // SAME behavior as original: shallow copy of steps
        ArrayList<Instruction> newSteps = new ArrayList<>(original.getSteps());
        r.setSteps(newSteps);

        // Deep copy of ingredients
        List<RecipeIngredient> newIngredients = new ArrayList<>();
        for (RecipeIngredient ing : original.getIngredients()) {
            newIngredients.add(ing.copy());
        }
        r.setIngredients(newIngredients);

        return r;
    }

    /**
     * Markdown export of a recipe.
     * EXACT logic of your original toString().
     */
    public static String toMarkdown(Recipe recipe) {
        String result = "### ";
        result += recipe.getRecipeName();
        result += "\n\n## Ingredients:\n";

        for (RecipeIngredient i : recipe.getIngredients()) {
            result += "- ";
            result += i.getAmount();
            result += i.getUnit().name();
            result += " of ";
            result += i.getIngredient().getIngredientName();
            result += ";\n";
        }

        result += "\n## Instructions:\n";

        for (int i = 0; i < recipe.getSteps().size(); i++) {
            result += (i + 1) + ") ";
            result += recipe.getSteps().get(i).toString();
            result += "\n";
        }

        result += "\n_Good luck with the preparation!_";
        return result;
    }
}
