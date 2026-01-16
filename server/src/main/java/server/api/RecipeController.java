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
     * Used by Spring (publisher will be injected).
     */
    @Autowired
    public RecipeController(RecipeService recipeService, RecipePublisher publisher) {
        this.recipeService = recipeService;
        this.publisher = publisher;
    }

    // For tests only
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.publisher = null;
    }


    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.findAll();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable long id) {
        return recipeService.findById(id);
    }

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

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable long id, @RequestBody Recipe recipe) {
        if (recipe == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            // IMPORTANT: do NOT call findById() before update() (tests expect this)
            Recipe updated = recipeService.update(id, recipe);

            if (publisher != null) {
                // Title propagation (list subscribers)
                if (updated != null && updated.getRecipeName() != null) {
                    publisher.recipeTitleUpdated(id, updated.getRecipeName());
                }
                // Content propagation (recipe viewers)
                publisher.recipeUpdated(id, System.currentTimeMillis(), "content");
            }

            return updated;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable long id) {
        try {
            // Keep old behavior for tests (many tests expect findById then delete)
            recipeService.findById(id);
            recipeService.delete(id);

            if (publisher != null) {
                publisher.recipeDeleted(id);
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
