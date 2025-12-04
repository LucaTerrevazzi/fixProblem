package server.service;
import commons.Recipe;
import commons.Instruction;
import commons.RecipeIngredient;
import org.springframework.stereotype.Service;
import server.database.RecipeRepository;

import java.util.List;
import java.util.Optional;
@Service
public class RecipeService {
    private final RecipeService recipeService;
    private final RecipeRepository repo;

}
