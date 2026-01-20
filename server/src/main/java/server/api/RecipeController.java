package server.api;

import commons.Recipe;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.service.RecipeService;
import server.ws.RecipePublisher;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipePublisher publisher;

    /**
     * Constructor for Spring.
     */
    @Autowired
    public RecipeController(RecipeService recipeService, RecipePublisher publisher) {
        this.recipeService = recipeService;
        this.publisher = publisher;
    }
    //Constructor for tests
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.publisher = null;
    }
    /**
     * GET /recipes
     * returns all the recipes
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
    /**
     * POST /recipes
     * creates a new recipe
     * @param recipe recipe to create
     * @return recipe
     */
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Recipe saved = recipeService.save(recipe);

        if (publisher != null) {
            publisher.recipeAdded(saved);
        }

        return saved;
    }
    /**
     * PUT /recipes/{id}
     * updates an existing recipe
     * @param id id of recipe to update
     * @param recipe new recipe values
     * @return updated recipe, or 404 if not found
     */
    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable long id, @RequestBody Recipe recipe) {
        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            Recipe updated = recipeService.update(id, recipe);

            if (publisher != null) {
                if (updated != null && updated.getRecipeName() != null) {
                    publisher.recipeTitleUpdated(id, updated.getRecipeName());
                }
                publisher.recipeUpdated(id, System.currentTimeMillis(), "content");
            }

            return updated;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * DELETE /recipes/{id}
     * deletes a recipe by its id
     * @param id id of the recipe to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable long id) {
        try {
            recipeService.findById(id);
            recipeService.delete(id);

            if (publisher != null) {
                publisher.recipeDeleted(id);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /recipes/ingredient/{id}
     * returns a list of recipes based on the ingredientID
     */
    @GetMapping("/ingredient/{ingredientId}")
    public List<Recipe> getRecipeByIngredientID(@PathVariable long ingredientId){
        return recipeService.findRecipeByIngredientID(ingredientId);
    }

    /**
     * GET /recipes/ingredient/{id}/count
     * returns the number of recipes using that ingredientId
     */
    @GetMapping("/ingredient/{ingredientId}/count")
    public Integer countRecipeByIngredientID(@PathVariable long ingredientId){
        return recipeService.countRecipeByIngredientID(ingredientId);
    }

}
