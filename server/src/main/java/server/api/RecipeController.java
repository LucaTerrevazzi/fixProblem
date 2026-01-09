package server.api;

import commons.Recipe;
import org.springframework.http.ResponseEntity;
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

    /**
     * POST /recipes
     * @param recipe recipe to create
     * @return newly created recipe
     */
    @PostMapping
    public Recipe create(@RequestBody Recipe recipe) {
        System.out.println(recipe.toString());
        return recipeService.save(recipe);
    }

    /**
     * DELETE /recipes/delete/{id}
     * @param id id of the recipe to delete
     * @return HTTP response
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id){
        try {
            recipeService.deleteById(id);
            System.out.println("Recipe deleted successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Recipe delete failed");
            return ResponseEntity.badRequest().build();
        }
    }
}

