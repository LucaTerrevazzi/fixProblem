package server.service;

import commons.Ingredient;
import org.springframework.stereotype.Service;
import server.database.IngredientRepository;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepo;

    /**
     * constructor for IngredientService
     * @param ingredientRepo repository for saving and getting ingredients
     */
    public IngredientService(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    /**
     * get all ingredients.
     * @return list of all ingredients
     */
    public List<Ingredient> findAll() {
        return ingredientRepo.findAll();
    }

    /**
     * find an ingredient by its id.
     * @param id the id of the ingredient to look up
     * @return the found ingredient
     * @throws IllegalArgumentException if no ingredient exists with the id given
     */
    public Ingredient findById(long id) {
        return ingredientRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Ingredient not found: " + id));
    }

    /**
     * save an ingredient.
     * @param ingredient the ingredient to save
     * @return the persisted ingredient (possibly with generated id)
     */
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepo.save(ingredient);
    }

    /**
     * update an existing ingredient.
     * @param id id of the ingredient to update
     * @param updated the new state to apply
     * @return the updated ingredient
     */
    public Ingredient update(long id, Ingredient updated) {
        Ingredient existing = findById(id);
        existing.setIngredientName(updated.getIngredientName());
        existing.setFat(updated.getFat());
        existing.setCarbs(updated.getCarbs());
        existing.setProtein(updated.getProtein());
        existing.setIngredientLanguage(updated.getIngredientLanguage());
        return ingredientRepo.save(existing);
    }

    /**
     * delete an ingredient by id.
     * @param id the id of the ingredient to delete
     */
    public void delete(long id) {
        ingredientRepo.deleteById(id);
    }
}