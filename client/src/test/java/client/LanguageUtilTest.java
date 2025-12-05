package client;

import client.utils.LanguageData;
import client.utils.LanguageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LanguageUtilTest {
    private static final String TEST_FILE = "src/main/resources/Language.json";

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();

        LanguageUtil.setFilePath(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void testLoadJsonCreatesFileIfNotExists() throws IOException {
        assertFalse(new File(TEST_FILE).exists());

        LanguageData data = LanguageUtil.loadJson();

        assertTrue(new File(TEST_FILE).exists());
        assertNotNull(data);
        assertTrue(data.getLanguages().isEmpty());
    }

    @Test
    void testAddLanguage() throws IOException {
        LanguageUtil.addLanguage("English");
        LanguageData data = LanguageUtil.loadJson();
        assertEquals(List.of("English"), data.getLanguages());
    }

    @Test
    void testRemoveLanguage() throws IOException {
        LanguageUtil.addLanguage("English");
        LanguageUtil.removeLanguage("English");
        LanguageData data = LanguageUtil.loadJson();
        assertTrue(data.getLanguages().isEmpty());
    }
}
