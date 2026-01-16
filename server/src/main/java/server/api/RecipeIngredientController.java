package server.api;

import commons.Recipe;
import commons.RecipeIngredient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.RecipeIngredientRepository;
import server.service.RecipeIngredientService;
import server.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipeIngredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;
    private final RecipeService recipeService;

    /**
     * Constructor for RecipeIngredientController
     * @param recipeIngredientService service for getting and sending recipeIngredients
     */
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService,  RecipeService recipeService) {
        this.recipeIngredientService = recipeIngredientService;
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public List<RecipeIngredient> getAllRecipeIngredients(){
        return recipeIngredientService.findAll();
    }

    @GetMapping("/ingredient/{id}")
    public List<Recipe> getRecipeByIngredientID(@PathVariable long id){
        return recipeService.findRecipeByIngredientID(id);
    }
}
