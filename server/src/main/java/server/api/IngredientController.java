package server.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Ingredient;
import org.springframework.web.server.ResponseStatusException;
import server.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Creates a new IngredientController. This is only used by Spring.
     *
     * @param service repository used to persist and retrieve ingredients
     */
    public IngredientController(IngredientService service) {
        this.ingredientService = service;
    }

    /**
     * Retrieves all ingredients stored in the database.
     *
     * @return a list containing all ingredients
     */
    @GetMapping(path = { "", "/" })
    public List<Ingredient> getAll() {
        return ingredientService.findAll();
    }

    /**
     * Retrieves an ingredient by its identifier.
     *
     * @param id identifier of the ingredient
     * @return the ingredient if it exists, or a bad request response otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable("id") long id) {
        try {
            return ResponseEntity.ok(ingredientService.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Adds a new ingredient to the database.
     *
     * @param ingredient ingredient to be added
     * @return the saved ingredient, or a bad request response if validation fails
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Ingredient> add(@RequestBody Ingredient ingredient) {

        if (isNullOrEmpty(ingredient.getIngredientName())
                || ingredient.getIngredientLanguage() == null) {
            return ResponseEntity.badRequest().build();
        }

        Ingredient saved = ingredientService.save(ingredient);
        return ResponseEntity.ok(saved);
    }

    /**
     * PUT api/ingredient/{id}
     * updates an existing ingredient
     * @param id id of ingredient to update
     * @param ingredient new ingredient values
     * @return updated ingredient, or 404 if not found
     */
    @PutMapping("/{id}")
    public Ingredient updateIngredient(@PathVariable long id, @RequestBody Ingredient ingredient) {
        if (ingredient == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            return ingredientService.update(id, ingredient);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes an ingredient by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        ingredientService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Checks whether a string is null or empty.
     *
     * @param s string to check
     * @return true if the string is null or empty, false otherwise
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
