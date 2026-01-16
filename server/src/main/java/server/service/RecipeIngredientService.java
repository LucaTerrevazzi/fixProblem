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

    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepo.findAll();
    }

    public List<RecipeIngredient> findByIngredientID(Long recipeIngredientID) {
        return recipeIngredientRepo.findByIngredientIngredientID(recipeIngredientID);
    }
}
