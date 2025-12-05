package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientTest {

    @Test
    void getIngredient() {
        Ingredient ingredient = new Ingredient("Cucumber");
        RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, Unit.g , 100);
        assertEquals(ingredient, recipeIngredient.getIngredient());
    }

    @Test
    void setIngredient() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        Ingredient ingredient = new Ingredient("Tomato");
        recipeIngredient.setIngredient(ingredient);
        assertEquals(ingredient, recipeIngredient.getIngredient());
    }

    @Test
    void getUnit() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        assertEquals(Unit.g, recipeIngredient.getUnit());
    }

    @Test
    void setUnit() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        recipeIngredient.setUnit(Unit.kg);
        assertEquals(Unit.kg, recipeIngredient.getUnit());
    }

    @Test
    void getAmount() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        assertEquals(100, recipeIngredient.getAmount());
    }

    @Test
    void setAmount() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        recipeIngredient.setAmount(200);
        assertEquals(200, recipeIngredient.getAmount());
    }

    @Test
    void testToString() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        assertEquals(recipeIngredient.toString(), "100g Cucumber");
    }


    @Test
    void testEqualsTrue() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        assertTrue(recipeIngredient1.equals(recipeIngredient2));
    }
    @Test
    void testEqualsFalse() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(new Ingredient("Tomato"), Unit.g , 100);
        assertFalse(recipeIngredient1.equals(recipeIngredient2));
    }
    @Test
    void testEqualsFalse2() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = null;
        assertFalse(recipeIngredient1.equals(recipeIngredient2));
    }
    @Test
    void testEqualsFalse3() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        Ingredient ingredient = new Ingredient("Tomato");
        assertFalse(recipeIngredient.equals(ingredient));
    }
    @Test
    void testEqualsFalse4() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.kg , 100);
        assertFalse(recipeIngredient1.equals(recipeIngredient2));
    }
    @Test
    void testEqualsFalse5() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 200);
        assertFalse(recipeIngredient1.equals(recipeIngredient2));
    }

    @Test
    void testHashCode() {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        RecipeIngredient recipeIngredient2 = new RecipeIngredient(new Ingredient("Cucumber"), Unit.g , 100);
        assertEquals(recipeIngredient1.hashCode(), recipeIngredient2.hashCode());
    }
}