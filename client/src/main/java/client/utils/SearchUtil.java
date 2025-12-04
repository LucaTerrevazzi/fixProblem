package client.utils;

import commons.Instruction;
import commons.Recipe;
import commons.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class SearchUtil {

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
}
