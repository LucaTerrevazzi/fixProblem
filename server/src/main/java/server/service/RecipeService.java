package server.service;
import commons.Instruction;
import commons.Recipe;
import commons.RecipeIngredient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.database.IngredientRepository;
import server.database.InstructionRepository;
import server.database.RecipeIngredientRepository;
import server.database.RecipeRepository;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepo;
    private final InstructionRepository instructionRepo;
    private final RecipeIngredientRepository recipeIngredientRepo;
    private final IngredientRepository ingredientRepo;

    /**
     * constructor for RecipeService
     * @param recipeRepo repository for saving and getting recipes
     * @param instructionRepo repository for saving and getting instructions
     * @param recipeIngredientRepo repository for saving and getting recipeIngredients
     * @param ingredientRepo repository for resolving existing ingredients
     */
    public RecipeService(RecipeRepository recipeRepo,
                         InstructionRepository instructionRepo,
                         RecipeIngredientRepository recipeIngredientRepo,
                         IngredientRepository ingredientRepo) {
        this.recipeRepo = recipeRepo;
        this.instructionRepo = instructionRepo;
        this.recipeIngredientRepo = recipeIngredientRepo;
        this.ingredientRepo = ingredientRepo;
    }

    /**
     * get all recipes.
     *
     * @return list of all recipes
     */
    public List<Recipe> findAll() {
        return recipeRepo.findAll();
    }

    /**
     * find a recipe by its id.
     * @param id the id of the recipe to look up
     * @return the found recipe
     * @throws IllegalArgumentException if no recipe exists with the id given
     */
    public Recipe findById(long id) {
        return recipeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + id));
    }

    /**
     * save a recipe with its steps and ingredients
     * @param recipe the recipe to save
     * @return the recipe
     */
    @Transactional
    public Recipe save(Recipe recipe) {
        if (recipe.getSteps() != null) {
            for (Instruction step : recipe.getSteps()) {
                step.setRecipe(recipe);
            }
        }
        if (recipe.getIngredients() != null) {
            for (RecipeIngredient ri : recipe.getIngredients()) {
                ri.setRecipe(recipe);

                if (ri.getIngredient() == null || ri.getIngredient().getIngredientID() == null) {
                    throw new IllegalArgumentException("RecipeIngredient must reference an existing Ingredient by id.");
                }
                ri.setIngredient(ingredientRepo.getReferenceById(ri.getIngredient().getIngredientID()));
            }
        }
        return recipeRepo.save(recipe);
    }

    /**
     * update an existing recipe and replace its steps and ingredients
     * @param id id of the recipe to update
     * @param updated the new state to apply
     * @return the updated recipe
     */
    @Transactional
    public Recipe update(long id, Recipe updated) {
        Recipe existing = findById(id);

        existing.setRecipeName(updated.getRecipeName());
        existing.setRecipeLanguage(updated.getRecipeLanguage());

        existing.getSteps().clear();
        if (updated.getSteps() != null) {
            for (Instruction step : updated.getSteps()) {
                step.setRecipe(existing);
                existing.getSteps().add(step);
            }
        }

        existing.getIngredients().clear();
        if (updated.getIngredients() != null) {
            for (RecipeIngredient ri : updated.getIngredients()) {
                ri.setRecipe(existing);

                if (ri.getIngredient() == null || ri.getIngredient().getIngredientID() == null) {
                    throw new IllegalArgumentException("RecipeIngredient must reference an existing Ingredient by id.");
                }

                ri.setIngredient(ingredientRepo.getReferenceById(ri.getIngredient().getIngredientID()));
                existing.getIngredients().add(ri);
            }
        }

        return recipeRepo.save(existing);
    }

    /**
     * delete a recipe by id.
     * @param id the id of the recipe to delete
     */
    public void deleteById(long id) {
        recipeRepo.deleteById(id);
    }
}
