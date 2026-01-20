package client.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Stores the currently selected UI locale and provides the corresponding
 * ResourceBundle for JavaFX internationalization.
 */
public final class LanguageManager {

    /**
     * The currently active locale for UI translations.
     */
    private static Locale locale = Locale.ENGLISH;

    private LanguageManager() {
        // Utility class
    }

    /**
     * Returns the currently selected locale.
     *
     * @return the current locale
     */
    public static Locale getLocale() {
        return locale;
    }

    /**
     * Sets the current locale.
     *
     * @param newLocale the new locale to use
     */
    public static void setLocale(Locale newLocale) {
        if (newLocale != null) {
            locale = newLocale;
        }
    }

    /**
     * Returns the resource bundle for the current locale.
     *
     * @return the resource bundle
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("i18n.messages", locale);
    }
}
