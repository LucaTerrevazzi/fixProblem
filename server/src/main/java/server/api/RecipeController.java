package server.api;

import commons.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
        return recipeService.save(recipe);
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
            return recipeService.update(id, recipe);
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
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

