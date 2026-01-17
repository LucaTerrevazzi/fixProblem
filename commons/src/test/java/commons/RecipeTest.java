package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class RecipeTest {

    @Test
    public void testEmptyConstructor() {
        Recipe r = new Recipe();
        assertNotNull(r.getSteps());
        assertNotNull(r.getIngredients());
    }

    @Test
    public void testConstructorWithID() {
        Recipe r = new Recipe(Long.valueOf(42));
        assertEquals(42, r.getRecipeID());
    }

    @Test
    public void testFullConstructor() {
        Recipe recipeRef = new Recipe(Long.valueOf(1));

        Instruction i1 = new Instruction("Cut", 1, recipeRef);
        i1.setInstructionID(Long.valueOf(1));

        Instruction i2 = new Instruction("Mix", 2, recipeRef);
        i2.setInstructionID(Long.valueOf(2));

        List<Instruction> instr = List.of(i1, i2);
        List<RecipeIngredient> ingr = new ArrayList<>();

        Recipe r = new Recipe(Long.valueOf(1));
        r.setRecipeName("Test");
        r.setSteps(instr);
        r.setIngredients(ingr);
        r.setRecipeLanguage(Language.EN);

        assertEquals(1, r.getRecipeID());
        assertEquals("Test", r.getRecipeName());
        assertEquals(instr, r.getSteps());
        assertEquals(ingr, r.getIngredients());
        assertEquals(Language.EN, r.getRecipeLanguage());
    }

    @Test
    public void testSetRecipeID() {
        Recipe r = new Recipe();
        r.setRecipeID(Long.valueOf(10));
        assertEquals(10, r.getRecipeID());
    }

    @Test
    public void testSetRecipeName() {
        Recipe r = new Recipe();
        r.setRecipeName("Pasta");
        assertEquals("Pasta", r.getRecipeName());
    }

    @Test
    public void testSetInstructions() {
        Recipe recipeRef = new Recipe(Long.valueOf(1));

        Instruction step = new Instruction("Heat water", 1, recipeRef);
        step.setInstructionID(Long.valueOf(1));

        Recipe r = new Recipe();
        r.setSteps(new ArrayList<>(List.of(step)));

        assertEquals(1, r.getSteps().size());
        assertEquals("Heat water", r.getSteps().get(0).getDescription());
    }

    @Test
    public void testSetIngredients() {
        Recipe r = new Recipe();
        r.setIngredients(new ArrayList<>());
        assertEquals(0, r.getIngredients().size());
    }

    @Test
    public void testSetRecipeLanguage() {
        Recipe r = new Recipe();
        r.setRecipeLanguage(Language.EN);
        assertEquals(Language.EN, r.getRecipeLanguage());
    }

    @Test
    public void testSetRecipeServings(){
        Recipe r = new Recipe();
        r.setServings(3);
        assertEquals(3, r.getServings());
    }

    @Test
    public void testEquals() {
        Recipe recipeRef = new Recipe(Long.valueOf(1));

        Instruction step = new Instruction("Boil water", 1, recipeRef);
        step.setInstructionID(Long.valueOf(1));

        List<Instruction> instr = List.of(step);
        List<RecipeIngredient> ingr = new ArrayList<>();

        Recipe r1 = new Recipe(Long.valueOf(1));
        r1.setRecipeName("Tea");
        r1.setSteps(instr);
        r1.setIngredients(ingr);
        r1.setServings(3);

        Recipe r2 = new Recipe(Long.valueOf(1));
        r2.setRecipeName("Tea");
        r2.setSteps(instr);
        r2.setIngredients(ingr);
        r2.setServings(3);

        assertEquals(r1, r2);
    }

    @Test
    public void testEqualsDifferent() {
        Recipe r1 = new Recipe(Long.valueOf(1));
        Recipe r2 = new Recipe(Long.valueOf(2));

        assertNotEquals(r1, r2);
    }
    //Used AI to alter the tests sto that they fit the new Classes compatible with jakarta annotations
}
