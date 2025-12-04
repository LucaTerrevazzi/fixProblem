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
    @Test
    void testSetAndGetIngredientID() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setIngredientID(Long.valueOf(5));
        assertEquals(5, ingredient.getIngredientID());
    }

    @Test
    void testSetAndGetFat() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setFat(2.5);
        assertEquals(2.5, ingredient.getFat());
    }

    @Test
    void testSetAndGetProtein() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setProtein(1.2);
        assertEquals(1.2, ingredient.getProtein());
    }

    @Test
    void testSetAndGetCarbs() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setCarbs(3.4);
        assertEquals(3.4, ingredient.getCarbs());
    }

    @Test
    void testGetKcal() {
        Ingredient ingredient = new Ingredient("Cucumber");
        ingredient.setFat(1);
        ingredient.setProtein(2);
        ingredient.setCarbs(3);

        assertEquals(29, ingredient.getKcal());
    }

    @Test
    void testEquals() {
        Ingredient ingredient = new Ingredient("Cucumber");
        assertTrue(ingredient.equals(ingredient));
    }

    @Test
    void testHashCodeEqualObjects() {
        Ingredient i1 = new Ingredient("Cucumber");
        Ingredient i2 = new Ingredient("Cucumber");

        i1.setFat(1);
        i2.setFat(1);
        i1.setProtein(2);
        i2.setProtein(2);
        i1.setCarbs(3);
        i2.setCarbs(3);
        assertEquals(i1.hashCode(), i2.hashCode());
    }

}