package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void testEnglishExpandedForm() {
        assertEquals("English", Language.EN.getExpandedForm(),
                "EN should expand to English");
    }

    @Test
    void testDutchExpandedForm() {
        assertEquals("Dutch", Language.NL.getExpandedForm(),
                "NL should expand to Dutch");
    }

    @Test
    void testGreekExpandedForm() {
        assertEquals("Greek", Language.GR.getExpandedForm(),
                "GR should expand to Greek");
    }

    @Test
    void testFrenchExpandedForm() {
        assertEquals("French", Language.FR.getExpandedForm(),
                "FR should expand to French");
    }

    @Test
    void testTurkishExpandedForm() {
        assertEquals("Turkish", Language.TR.getExpandedForm(),
                "TR should expand to Turkish");
    }

    @Test
    void testAllValuesNotNull() {
        for (Language lang : Language.values()) {
            assertNotNull(lang.getExpandedForm(),
                    lang + " expanded form should not be null");
        }
    }

    @Test
    void testEnumContainsAllExpected() {
        Language[] expected = {
                Language.EN,
                Language.NL,
                Language.GR,
                Language.FR,
                Language.TR
        };

        assertArrayEquals(expected, Language.values(),
                "Language enum should contain EN, NL, GR, FR, TR");
    }
}
