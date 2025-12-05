package commons;

/**
 * Provides basic conversions between supported units so that nutritional
 * values can be calculated even when ingredients are measured in informal
 * units such as tablespoons or pinches.
 */
public final class UnitConversion {

    private static final double MILLILITRES_PER_LITRE = 1000;
    private static final double MILLILITRES_PER_TABLESPOON = 15;
    private static final double MILLILITRES_PER_TEASPOON = 5;

    // Assuming a pinch is roughly 1/64 teaspoon (about 0.31–0.36 g).
    private static final double GRAMS_PER_PINCH = 0.36;

    private UnitConversion() {
        // utility class
    }

    /**
     * Converts the provided amount to grams, using common kitchen
     * approximations for volume-based measurements. Volume is converted
     * to millilitres assuming water-like density (1 g = 1 mL).
     *
     * @param unit   the unit of the amount
     * @param amount the amount expressed in the supplied unit
     * @return the amount expressed in grams
     */
    public static double toGrams(Unit unit, int amount) {
        switch (unit) {
            case g:
                return amount;
            case kg:
                return amount * 1000d;
            case L:
                return MILLILITRES_PER_LITRE * amount; // 1 mL == 1 gram
            case mL:
                return amount;
            case tbsp:
                return MILLILITRES_PER_TABLESPOON * amount;
            case tsp:
                return MILLILITRES_PER_TEASPOON * amount;
            case pinch:
                return GRAMS_PER_PINCH * amount;
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    /**
     * Picks a sensible unit for displaying a weight based on its size.
     * This is useful when surfacing nutritional information to the user.
     *
     * @param grams amount in grams
     * @return the unit that best expresses the amount (kg for 1000g+, otherwise g)
     */
    public static Unit preferredMassUnit(double grams) {
        return grams >= 1000 ? Unit.kg : Unit.g;
    }
}