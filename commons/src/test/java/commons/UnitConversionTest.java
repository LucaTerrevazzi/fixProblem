package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnitConversionTest {

    @Test
    void convertsZeroToGrams() {
        assertEquals(0, commons.UnitConversion.toGrams(Unit.g, 0));
        assertEquals(0, commons.UnitConversion.toGrams(Unit.kg, 0));
        assertEquals(0, commons.UnitConversion.toGrams(Unit.L, 0));
        assertEquals(0, commons.UnitConversion.toGrams(Unit.tsp, 0));
        assertEquals(0, commons.UnitConversion.toGrams(Unit.pinch, 0));
    }

    @Test
    void convertsTeaspoonsPrecisely() {
        assertEquals(25, commons.UnitConversion.toGrams(Unit.tsp, 5)); // 5 * 5 mL
    }

    @Test
    void convertsTablespoonsPrecisely() {
        assertEquals(45, commons.UnitConversion.toGrams(Unit.tbsp, 3)); // 3 * 15 mL
    }

    @Test
    void convertsPinchPrecisely() {
        assertEquals(0.36 * 7, commons.UnitConversion.toGrams(Unit.pinch, 7), 1e-9);
    }

    @Test
    void convertsMassUnitsToGrams() {
        assertEquals(1000, commons.UnitConversion.toGrams(Unit.g, 1000));
        assertEquals(2000, commons.UnitConversion.toGrams(Unit.kg, 2));
    }

    @Test
    void convertsVolumeUnitsToGramsUsingWaterDensity() {
        assertEquals(1000, commons.UnitConversion.toGrams(Unit.L, 1));
        assertEquals(250, commons.UnitConversion.toGrams(Unit.mL, 250));
        assertEquals(30 * 15, commons.UnitConversion.toGrams(Unit.tbsp, 30));
        assertEquals(10 * 5, commons.UnitConversion.toGrams(Unit.tsp, 10));
    }

    @Test
    void pinchConversionHasExpectedTolerance() {
        double grams = commons.UnitConversion.toGrams(Unit.pinch, 1);
        // Should stay within realistic bounds (0.31–0.36 g)
        assertTrue(grams >= 0.31 && grams <= 0.36);
    }

    @Test
    void choosesPreferredMassUnit() {
        assertEquals(Unit.kg, commons.UnitConversion.preferredMassUnit(1500));
        assertEquals(Unit.g, commons.UnitConversion.preferredMassUnit(999));
    }

}