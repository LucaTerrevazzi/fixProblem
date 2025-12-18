package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Ingredient;
import server.service.IngredientService;

public class IngredientControllerTest {

    private TestIngredientRepository repo;
    private IngredientService service;
    private IngredientController sut;

    @BeforeEach
    public void setup() {
        repo = new TestIngredientRepository();
        service = new IngredientService(repo);
        sut = new IngredientController(service);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.add(getIngredient(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getIngredient("ing1"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    private static Ingredient getIngredient(String ing) {
        return new Ingredient(ing);
    }
}
