package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Ingredient;
import server.database.IngredientRepository;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientRepository repo;

    /**
     * Creates a new IngredientController. This is only used by Spring.
     *
     * @param repo repository used to persist and retrieve ingredients
     */
    public IngredientController(IngredientRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all ingredients stored in the database.
     *
     * @return a list containing all ingredients
     */
    @GetMapping(path = { "", "/" })
    public List<Ingredient> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves an ingredient by its identifier.
     *
     * @param id identifier of the ingredient
     * @return the ingredient if it exists, or a bad request response otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
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

        Ingredient saved = repo.save(ingredient);
        return ResponseEntity.ok(saved);
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
