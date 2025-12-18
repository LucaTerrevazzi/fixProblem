package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import commons.Ingredient;
import server.service.IngredientService;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService service;

    /**
     * Creates a new IngredientController. This is only used by Spring.
     *
     * @param service repository used to persist and retrieve ingredients
     */
    public IngredientController(IngredientService service) {
        this.service = service;
    }

    /**
     * Retrieves all ingredients stored in the database.
     *
     * @return a list containing all ingredients
     */
    @GetMapping(path = { "", "/" })
    public List<Ingredient> getAll() {
        return service.findAll();
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
            return ResponseEntity.ok(service.findById(id));
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
                || isNullOrEmpty(ingredient.getIngredientLanguage())) {
            return ResponseEntity.badRequest().build();
        }

        Ingredient saved = service.save(ingredient);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes an ingredient by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
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
