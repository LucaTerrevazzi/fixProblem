package server.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.RecipeIngredientRepository;
import server.service.RecipeIngredientService;

@RestController
@RequestMapping("/recipeIngredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    /**
     * Constructor for RecipeIngredientController
     * @param recipeIngredientService service for getting and sending recipeIngredients
     */
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }
}
