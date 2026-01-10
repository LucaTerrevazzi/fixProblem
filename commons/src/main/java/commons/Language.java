package commons;

/**
 * The Language a Recipe can have
 */
public enum Language {
    EN("English"),
    NL("Dutch"),
    GR("German");

    private final String expandedForm;

    Language(String expandedForm) {
        this.expandedForm = expandedForm;
    }

    public String getExpandedForm() {
        return expandedForm;
    }
}
