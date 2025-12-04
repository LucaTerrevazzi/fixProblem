package client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class LanguageUtil {
    private static String FILE_PATH = "src/main/resources/Language.json";

    /**
     * function to change the path of the Language file for testing purposes
     * @param path path to set the path to
     */
    public static void setFilePath(String path) {
        FILE_PATH = path;
    }

    /**
     * load the languages from the Languages.json file
     * @return returns the languages as a LanguageData object
     * @throws IOException can throw an IOException
     */
    public static LanguageData loadJson() throws IOException {
        File file = new File(FILE_PATH);
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        if (!file.exists()) {
            LanguageData data = new LanguageData();
            saveJson(data);
            return data;
        }

        return mapper.readValue(file, LanguageData.class);
    }

    /**
     * save the languages to the Language.json file
     * @param data LanguageData object to save
     * @throws IOException can throw an IOException
     */
    protected static void saveJson(LanguageData data) throws IOException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(FILE_PATH), data);
    }

    /**
     * add a language to all the languages
     * @param l language to add
     * @throws IOException can throw an IOException
     */
    public static void addLanguage(String l) throws IOException {
        LanguageData data = loadJson();
        data.getLanguages().add(l);
        saveJson(data);
    }

    /**
     * removes a language from all the languages
     * @param l language to remove
     * @throws IOException can throw an IOException
     */
    public static void removeLanguage(String l) throws IOException {
        LanguageData data = loadJson();
        data.getLanguages().remove(l);
        saveJson(data);
        }
}
