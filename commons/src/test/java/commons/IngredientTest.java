package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void getIngredientName() {
        Ingredient ingredient = new Ingredient("Cucumber");
        assertEquals( "Cucumber", ingredient.getIngredientName());
    }

    @Test
    void setIngredientName() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setIngredientName("Tomato");
        assertEquals("Tomato", ingredient.getIngredientName());
    }
    @Test
    void testEqualsTrue(){
        Ingredient ingredient1 = new Ingredient("Cucumber");
        Ingredient ingredient2 = new Ingredient("Cucumber");
        assertTrue(ingredient1.equals(ingredient2));
    }
    @Test
    void testEqualsFalse(){
        Ingredient ingredient1 = new Ingredient("Cucumber");
        Ingredient ingredient2 = null;
        assertFalse(ingredient1.equals(ingredient2));
    }
    @Test
    void testEqualsFalse2(){
        Ingredient ingredient = new Ingredient("Cucumber");
        String stringIngredient = "Cucumber";
        assertFalse(ingredient.equals(stringIngredient));
    }
    @Test
    void testHashCode(){
        Ingredient ingredient1 = new Ingredient("Cucumber");
        Ingredient ingredient2 = new Ingredient("Tomato");
        assertNotEquals(ingredient1.hashCode(), ingredient2.hashCode());
    }

}