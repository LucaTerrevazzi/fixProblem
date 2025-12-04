package client;

import client.utils.FavoritesData;
import client.utils.FavoritesUtil;
import commons.Recipe;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FavoritesUtilTest {

    private static final String TEST_FILE = "src/main/resources/test_Favorites.json";

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();

        FavoritesUtil.setFilePath(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) file.delete();
    }

    @Test
    void testLoadJsonCreatesFileIfNotExists() throws IOException {
        assertFalse(new File(TEST_FILE).exists());

        FavoritesData data = FavoritesUtil.loadJson();

        assertTrue(new File(TEST_FILE).exists());
        assertNotNull(data);
        assertTrue(data.getFavorites().isEmpty());
    }

    @Test
    void testAddFavorite() throws IOException {
        Recipe r = new Recipe();
        r.setRecipeID(1L);
        FavoritesUtil.addFavorite(r);

        FavoritesData data = FavoritesUtil.loadJson();
        assertEquals(List.of(1), data.getFavorites());
    }

    @Test
    void testAddFavoriteDoesNotDuplicate() throws IOException {
        Recipe r = new Recipe();
        r.setRecipeID(2L);
        FavoritesUtil.addFavorite(r);
        FavoritesUtil.addFavorite(r); // duplicate

        FavoritesData data = FavoritesUtil.loadJson();
        assertEquals(2, data.getFavorites().size());
        assertEquals(2, data.getFavorites().getFirst());
    }

    @Test
    void testRemoveFavorite() throws IOException {
        Recipe r = new Recipe();
        Recipe r2 = new Recipe();
        r.setRecipeID(1L);
        r2.setRecipeID((long) 2L);
        FavoritesUtil.addFavorite(r);
        FavoritesUtil.addFavorite(r2);

        FavoritesUtil.removeFavorite(r);

        FavoritesData data = FavoritesUtil.loadJson();
        assertEquals(List.of(2), data.getFavorites());
    }

    @Test
    void testRemoveFavoriteNotPresent() throws IOException {
        Recipe r = new Recipe();
        Recipe r2 = new Recipe();
        r.setRecipeID(1L);
        r2.setRecipeID(2L);
        FavoritesUtil.addFavorite(r);

        FavoritesUtil.removeFavorite(r2);

        FavoritesData data = FavoritesUtil.loadJson();
        assertEquals(List.of(1), data.getFavorites());
    }
}

