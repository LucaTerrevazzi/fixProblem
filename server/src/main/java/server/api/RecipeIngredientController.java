package server.api;

import commons.Recipe;
import commons.RecipeIngredient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    /**
     * GET /recipeIngredients
     * returns all the recipeIngredients
     */
    @GetMapping("/{id}")
    public List<RecipeIngredient> getAllRecipeIngredients(){
        return recipeIngredientService.findAll();
    }

    /**
     * GET /recipes/{id}
     * returns a recipe based on the ingredientID
     */
    @GetMapping("/ingredient/{id}")
    public List<Recipe> getRecipeByIngredientID(@PathVariable long id){
        return recipeService.findRecipeByIngredientID(id);
    }

    /**
     * DELETE /recipes/{id}
     * deletes a recipeIngredient by its IngredientID
     * @param id id of the recipeIngredient to delete
     */
    @DeleteMapping("/ingredient/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipeIngredient(@PathVariable long id) {
        try {
            recipeIngredientService.findByIngredientID(id);
            recipeIngredientService.delete(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
