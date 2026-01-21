package client.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the selected recipe languages for filtering.
 * Languages are stored as enum names, e.g. "EN", "NL", "FR".
 */
public class LanguageFilterData {

    private List<String> selectedLanguages = new ArrayList<>();

    public LanguageFilterData() {
    }

    public List<String> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<String> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }
}
