package server.api;

import commons.Recipe;
import org.springframework.web.bind.annotation.*;
import server.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * constructor for RecipeController
     * @param recipeService service for getting and saving recipes
     */
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * GET /recipes
     * returns all of the recipes
     */
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.findAll();
    }

    /**
     * GET /recipes/{id}
     * returns a recipe based on its ID
     */
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable long id) {
        return recipeService.findById(id);
    }


}
