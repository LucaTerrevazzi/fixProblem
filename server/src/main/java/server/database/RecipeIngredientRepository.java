package server.database;

import commons.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository
        extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findByIngredientIngredientID(Long recipeId);
    Integer countByIngredientIngredientID(Long recipeId);
    List<RecipeIngredient> findByRecipeRecipeID(Long recipeId);
}


