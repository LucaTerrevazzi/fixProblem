package server.service;

import commons.RecipeIngredient;
import org.springframework.stereotype.Service;
import server.database.RecipeIngredientRepository;

import java.util.List;

@Service
public class RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepo;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepo = recipeIngredientRepository;
    }

    /**
     * finds all recipeIngredients
     * @return list of all recipeIngredients in database
     */
    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepo.findAll();
    }

    /**
     * finds all recipeIngredients by ingredientID
     * @param ingredientId the ingredientId of the recipeIngredient
     * @return list of recipeIngredients by that certain ingredientID
     */
    public List<RecipeIngredient> findByIngredientID(Long ingredientId) {
        return recipeIngredientRepo.findByIngredientIngredientID(ingredientId);
    }

    /**
     * finds all recipeIngredients by ingredientID
     * @param recipeId the recipeId of the recipeIngredient
     * @return list of recipeIngredients by that certain ingredientID
     */
    public List<RecipeIngredient> findByRecipeID(Long recipeId) {
        return recipeIngredientRepo.findByRecipeRecipeID(recipeId);
    }

    /**
     * Count the recipeIngredients of a certain ingredient by id
     * @param ingredientId the ingredientId of the recipeIngredient
     * @return number of times an ingredient has been used
     */
    public Integer countByIngredientID(Long ingredientId) {
        return recipeIngredientRepo.countByIngredientIngredientID(ingredientId);
    }
    /**
     * delete a recipeIngredient by id.
     * @param ingredientId the ingredientId of the recipeIngredient to delete
     */
    public void deleteByIngredient(long ingredientId) {
        recipeIngredientRepo.deleteById(ingredientId);
    }
    public void deleteByRecipe(long recipeId){
        recipeIngredientRepo.deleteById(recipeId);
    }

}
