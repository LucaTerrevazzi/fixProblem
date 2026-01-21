package client.utils;

import java.util.Locale;

public enum UiLanguage {

    EN("en", Locale.ENGLISH, "flags/gb.png"),
    NL("nl", new Locale("nl"), "flags/nl.png"),
    FR("fr", Locale.FRENCH, "flags/fr.png"),
    TR("tr", new Locale("tr"), "flags/tr.png"),
    EL("el", new Locale("el"), "flags/el.png");

    private final String code;
    private final Locale locale;
    private final String flagPath;

    UiLanguage(String code, Locale locale, String flagPath) {
        this.code = code;
        this.locale = locale;
        this.flagPath = flagPath;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFlagPath() {
        return flagPath;
    }

    public String getCode() {
        return code;
    }
}
