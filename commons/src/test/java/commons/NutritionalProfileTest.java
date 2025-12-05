package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NutritionalProfileTest {

    @Test
    void constructorAndGetterTest() {
        commons.NutritionalProfile nutritionalProfile = new commons.NutritionalProfile(3,5,7);

        assertEquals(3,nutritionalProfile.getFat());
        assertEquals(5,nutritionalProfile.getProtein());
        assertEquals(7,nutritionalProfile.getCarbs());
    }

    @Test
    void setterTest(){
        commons.NutritionalProfile nutritionalProfile = new commons.NutritionalProfile(3,5,7);

        nutritionalProfile.setFat(2.5);
        nutritionalProfile.setProtein(10);
        nutritionalProfile.setCarbs(5);

        assertEquals(2.5, nutritionalProfile.getFat());
        assertEquals(10, nutritionalProfile.getProtein());
        assertEquals(5, nutritionalProfile.getCarbs());
    }

    @Test
    void calculatesCalories() {
        commons.NutritionalProfile profile = new commons.NutritionalProfile(10, 5, 15);
        assertEquals(10 * 9 + 5 * 4 + 15 * 4, profile.getKcal());
    }

    @Test
    void scalesValues() {
        commons.NutritionalProfile profile = new commons.NutritionalProfile(10, 5, 15).scale(0.5);
        assertEquals(new commons.NutritionalProfile(5, 2.5, 7.5), profile);
    }

    @Test
    void addsProfiles() {
        commons.NutritionalProfile base = new commons.NutritionalProfile(4, 3, 2);
        commons.NutritionalProfile combined = base.add(new commons.NutritionalProfile(1, 1, 1));
        assertEquals(new commons.NutritionalProfile(5, 4, 3), combined);
    }
}
