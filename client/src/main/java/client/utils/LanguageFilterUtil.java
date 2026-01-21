package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for loading/saving the language filter config as JSON.
 */
public class LanguageFilterUtil {

    private static String filePath = getDefaultPath();

    private LanguageFilterUtil() {
        // utility class
    }

    /**
     * Change the file path (useful for testing).
     *
     * @param path new file path
     */
    public static void setFilePath(String path) {
        filePath = path;
    }

    private static String getDefaultPath() {
        String home = System.getProperty("user.home");
        return home + File.separator + ".foodpal" + File.separator + "LanguageFilter.json";
    }

    /**
     * Loads the language filter data from JSON.
     * If the file does not exist, it creates a default file.
     *
     * @return loaded LanguageFilterData
     * @throws IOException if reading fails
     */
    public static LanguageFilterData loadJson() throws IOException {
        File file = new File(filePath);
        ensureParentFolderExists(file);

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        if (!file.exists()) {
            LanguageFilterData data = new LanguageFilterData();
            saveJson(data);
            return data;
        }

        return mapper.readValue(file, LanguageFilterData.class);
    }

    /**
     * Saves the language filter data to JSON.
     *
     * @param data LanguageFilterData to save
     * @throws IOException if writing fails
     */
    public static void saveJson(LanguageFilterData data) throws IOException {
        File file = new File(filePath);
        ensureParentFolderExists(file);

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.writeValue(file, data);
    }

    private static void ensureParentFolderExists(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
