package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import client.ws.RecipeEvent;
import commons.Instruction;
import commons.Recipe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import commons.Language;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;

import java.net.URL;

public class RecipeOverviewCtrl implements Initializable {

    private Recipe selectedRecipe;

    @FXML
    private Button favoritesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button addRecipeButton;
    @FXML
    private Button deleteRecipeButton;
    @FXML
    private Button editRecipeButton;
    @FXML
    private Button cloneRecipeButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button downloadButton;

    @FXML
    private ToggleButton favoriteToggleButton;

    @FXML
    private TextField searchBar;

    @FXML
    private Label ingredientTitle;

    @FXML
    private MenuItem englishItem;
    @FXML
    private MenuItem dutchItem;
    @FXML
    private MenuItem frenchItem;
    @FXML
    private MenuItem turkishItem;
    @FXML
    private MenuItem greekItem;
    @FXML
    private MenuButton languageFilterMenu;

    private final Set<Language> selectedLangFilter = EnumSet.noneOf(Language.class);

    @FXML
    private ListView<String> ingredientsList;
    @FXML
    private ListView<String> instructionsList;

    @FXML
    private Label recipeLanguage;

    @FXML
    private ListView<Recipe> recipeListView;
    @FXML
    private Label wsStatusLabel;

    @FXML
    private Label recipeName;

    @FXML
    private Label errorLabel;

    @FXML
    private Label recipeListTitle;

    private ResourceBundle resources;

    private final ObservableList<Recipe> recipes = FXCollections.observableArrayList();

    private WsClient ws;

    @Inject
    private ServerUtils server;

    @Inject
    private FoodPalCtrl pc;

    @Inject
    public RecipeOverviewCtrl(FoodPalCtrl p, ServerUtils server) {
        this.pc = p;
        this.server = server;
    }

    public void setWsClient(WsClient ws) {
        this.ws = ws;
    }
    private final Set<Long> favoriteIds = new HashSet<>();
    private boolean showingFavorites = false;

    public void goToEditScene() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        Recipe fullRecipe = server.getRecipeById(selected.getRecipeID());
        pc.showEditRecipe(fullRecipe);
    }

    public void goToAddScene() {
        pc.showAddRecipe();
    }

    public void goToDownloadRecipe() {
        if (selectedRecipe != null) {
            pc.showDownloadRecipe(selectedRecipe);
        }
    }

    public void goToFavorites() {
        System.out.println("To favorites");
        showingFavorites = !showingFavorites;
        recipeListTitle.setText(showingFavorites ? "Favorite Recipes" : "All Recipes");
        favoritesButton.setText(showingFavorites ? "All Recipes" : "Favorites");
        applyFilters();
    }

    public void goToIngredients() {
        pc.showIngredientOverview();
    }

    public void deleteRecipeWarning() {
        if (selectedRecipe == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(getText("recipe.delete.title", "Recipe deletion"));
        alert.setContentText(
                getText("recipe.delete.confirmPrefix", "Are you sure you want to delete the recipe ")
                        + selectedRecipe.getRecipeName()
                        + getText("recipe.delete.confirmSuffix", "?\nThis action cannot be undone")
        );

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        server.deleteRecipe(selectedRecipe);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void cloneRecipe() {
        if (selectedRecipe != null) {
            RecipeHolder holder = RecipeHolder.getInstance();
            holder.setRecipe(selectedRecipe);
        }
        goToAddScene();
    }

    public void refresh() {
        Recipe previouslySelected = recipeListView.getSelectionModel().getSelectedItem();
        Long prevId = previouslySelected != null ? previouslySelected.getRecipeID() : null;

        if (showingFavorites) {
            applyFavoriteFilters();
            return;
        }

        // Re-fetch + re-apply filters so language filter is respected after returning from Add/Edit
        applyFilters();

        if (prevId != null) {
            for (Recipe r : recipes) {
                if (prevId.equals(r.getRecipeID())) {
                    recipeListView.getSelectionModel().select(r);
                    break;
                }
            }
        }

        if (recipeListView.getSelectionModel().getSelectedItem() == null && !recipes.isEmpty()) {
            recipeListView.getSelectionModel().selectFirst();
        }

        Recipe selectedInList = recipeListView.getSelectionModel().getSelectedItem();
        if (selectedInList == null) {
            selectedRecipe = null;

            deleteRecipeButton.setDisable(true);
            editRecipeButton.setDisable(true);
            downloadButton.setDisable(true);
            cloneRecipeButton.setDisable(true);

            return;
        }

        selectedRecipe = server.getRecipeById(selectedInList.getRecipeID());

        boolean hasSelection = selectedRecipe != null;
        deleteRecipeButton.setDisable(!hasSelection);
        editRecipeButton.setDisable(!hasSelection);
        downloadButton.setDisable(!hasSelection);
        cloneRecipeButton.setDisable(!hasSelection);

        if (hasSelection) {
            showRecipeDetails(selectedRecipe);
        }
    }

    public void applyFilters() {
        if (showingFavorites) {
            applyFavoriteFilters();
            return;
        }
        String query = searchBar.getText();
        var all = server.getRecipes();

        var afterLang = all.stream()
                .filter(r -> selectedLangFilter.isEmpty()
                        || selectedLangFilter.contains(r.getRecipeLanguage()))
                .toList();

        if (query != null && !query.isBlank()) {
            recipes.setAll(SearchUtil.search(afterLang, query));
        } else {
            recipes.setAll(
                    afterLang.stream()
                            .sorted(java.util.Comparator.comparing(r -> r.getRecipeName().toLowerCase()))
                            .toList()
            );
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        recipeListView.setItems(recipes);

        recipeListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Recipe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    boolean isFavorite = favoriteIds.contains(item.getRecipeID());
                    if (isFavorite) {
                        setText("★ " + item.getRecipeName());
                    } else {
                        setText(item.getRecipeName());
                    }
                }
            }
        });

        recipeListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldRecipe, newRecipe) -> {
                    boolean hasSelection = newRecipe != null;
                    deleteRecipeButton.setDisable(!hasSelection);
                    editRecipeButton.setDisable(!hasSelection);
                    downloadButton.setDisable(!hasSelection);
                    cloneRecipeButton.setDisable(!hasSelection);
                    if (newRecipe != null) {
                        Recipe fullRecipe = server.getRecipeById(newRecipe.getRecipeID());
                        showRecipeDetails(fullRecipe);
                    } else {
                        selectedRecipe = null;
                        recipeName.setText("");
                        ingredientsList.setItems(FXCollections.observableArrayList());
                        instructionsList.setItems(FXCollections.observableArrayList());
                        recipeLanguage.setText("");
                    }
                    updateFavoriteToggle(newRecipe);
                });

        initLanguageFilterMenu();
        setupFlagItems();

        // Ensure initial load respects stored language filter + empty search
        applyFilters();
        if (!recipes.isEmpty()) {
            recipeListView.getSelectionModel().selectFirst();
        }
    }
    private void setupFlagItems() {
        setFlag(englishItem, "flags/gb.png");
        setFlag(dutchItem, "flags/nl.png");
        setFlag(frenchItem, "flags/fr.png");
        setFlag(turkishItem, "flags/tr.png");
        setFlag(greekItem, "flags/gr.png");
    }

    private void setFlag(MenuItem item, String path) {
        if (item == null) return;

        item.setText(" ");
        ImageView icon = flagIcon(path);
        if (icon != null) {
            item.setGraphic(icon);
        }
    }

    private void handleRecipeListEvent(RecipeEvent ev) {
        // IMPORTANT: do NOT directly add/remove to recipes list here,
        // because that bypasses the active language/search filters.
        // Always refresh via applyFilters() so the filters are respected.

        switch (ev.type) {
            case RECIPE_ADDED, RECIPE_TITLE_UPDATED -> applyFilters();
            case RECIPE_DELETED -> {
                try {
                    Set<Long> favoriteIds = FavoritesStorage.loadFavoriteIds();
                    boolean wasFavorite = favoriteIds.contains(ev.id);
                    if (wasFavorite) {
                        favoriteIds.remove(ev.id);
                        FavoritesStorage.removeFavorite(new Recipe(ev.id));

                        Platform.runLater(() -> {
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setTitle(getText("favorites.deleted.title", "A favorite was deleted"));
                            a.setHeaderText(getText("favorites.deleted.header", "A favorite was deleted"));
                            a.setContentText(getText(
                                    "favorites.deleted.content",
                                    "One of your favorite recipes was deleted by someone else.\n"
                                            + "Take a moment to mourn its loss."
                            ));
                            a.show();
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                applyFilters();
            }
            default -> {
            }
        }
    }

    public void onAppStart() {
        if (ws != null) {
            ws.subscribeRecipeListStored(this::handleRecipeListEvent);
        }
        loadFavorites();
        refresh();
    }

    private void showRecipeDetails(Recipe recipe) {
        selectedRecipe = recipe;

        recipeName.setText(recipe.getRecipeName());

        if (recipe.getServings() == 0) {
            ingredientTitle.setText(getText("recipe.ingredients.servingsUnknown", "Ingredients (Servings unknown)"));
        } else {
            ingredientTitle.setText(
                    getText("recipe.ingredients.forPeoplePrefix", "Ingredients (for ")
                            + recipe.getServings()
                            + getText("recipe.ingredients.forPeopleSuffix", " people)")
            );
        }

        ingredientsList.setItems(
                FXCollections.observableArrayList(
                        recipe.getIngredients().stream()
                                .map(ri -> ri.getAmount() + " " + ri.getUnit() + " " + ri.getIngredient().getIngredientName())
                                .toList()
                )
        );

        instructionsList.setItems(
                FXCollections.observableArrayList(
                        recipe.getSteps().stream()
                                .map(Instruction::getDescription)
                                .toList()
                )
        );

        recipeLanguage.setText(
                getText("recipe.languagePrefix", "Language: ")
                        + recipe.getRecipeLanguage()
        );
    }

    public void bindWsStatus() {
        if (ws == null) return;

        ws.addStatusListener(status -> wsStatusLabel.setText(switch (status) {
            case CONNECTED -> "Live ● Connected";
            case CONNECTING -> "Connecting…";
            case RECONNECTING -> "Reconnecting…";
            case DISCONNECTED -> "Offline";
        }));
    }

    public void setEnglish() {
        LanguageManager.setLocale(Locale.ENGLISH);
        pc.reloadUI();
    }

    public void setDutch() {
        LanguageManager.setLocale(new Locale("nl"));
        pc.reloadUI();
    }

    public void setFrench() {
        LanguageManager.setLocale(Locale.FRENCH);
        pc.reloadUI();
    }

    public void setTurkish() {
        LanguageManager.setLocale(new Locale("tr"));
        pc.reloadUI();
    }

    public void setGreek() {
        LanguageManager.setLocale(new Locale("el"));
        pc.reloadUI();
    }

    private ImageView flagIcon(String path) {
        var stream = getClass().getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            System.out.println("Missing flag image: " + path);
            return null;
        }

        Image img = new Image(stream);
        ImageView view = new ImageView(img);
        view.setFitHeight(16);
        view.setPreserveRatio(true);
        return view;
    }

    private String getText(String key, String fallback) {
        if (resources == null) return fallback;
        try {
            return resources.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void initLanguageFilterMenu() {
        languageFilterMenu.getItems().clear();
        selectedLangFilter.clear();

        try {
            LanguageFilterData data = LanguageFilterUtil.loadJson();
            for (String s : data.getSelectedLanguages()) {
                try {
                    selectedLangFilter.add(Language.valueOf(s));
                } catch (IllegalArgumentException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Language lang : Language.values()) {
            CheckMenuItem item = new CheckMenuItem(lang.name());
            item.setSelected(selectedLangFilter.contains(lang));

            item.setOnAction(e -> {
                if (item.isSelected()) {
                    selectedLangFilter.add(lang);
                } else {
                    selectedLangFilter.remove(lang);
                }

                LanguageFilterData data = new LanguageFilterData();
                data.setSelectedLanguages(
                        selectedLangFilter.stream()
                                .map(Enum::name)
                                .sorted()
                                .collect(Collectors.toList())
                );

                try {
                    LanguageFilterUtil.saveJson(data);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                applyFilters();
            });

            languageFilterMenu.getItems().add(item);
        }
    }
    public void toggleFavorite() {
        if (selectedRecipe == null) {
            return;
        }
        boolean isFavorite = favoriteIds.contains(selectedRecipe.getRecipeID());
        try {
            if (isFavorite) {
                FavoritesStorage.removeFavorite(selectedRecipe);
                favoriteIds.remove(selectedRecipe.getRecipeID());
            } else {
                FavoritesStorage.addFavorite(selectedRecipe);
                favoriteIds.add(selectedRecipe.getRecipeID());
            }
        } catch (IOException e) {
            errorLabel.setText("Unable to update Favorites");
            return;
        }
        updateFavoriteToggle(selectedRecipe);
        recipeListView.refresh();
    }

    private void updateFavoriteToggle(Recipe recipe) {
        if (recipe == null) {
            favoriteToggleButton.setDisable(true);
            favoriteToggleButton.setSelected(false);
            favoriteToggleButton.setText("☆ Favorite");
            return;
        }
        favoriteToggleButton.setDisable(false);
        boolean isFavorite = favoriteIds.contains(recipe.getRecipeID());
        favoriteToggleButton.setSelected(isFavorite);
        if (isFavorite) {
            favoriteToggleButton.setText("★ Favorite");
        } else {
            favoriteToggleButton.setText("☆ Favorite");
        }

    }

    private void applyFavoriteFilters() {
        List<Recipe> favoriteRecipes = server.getRecipes().stream()
                .filter(r -> favoriteIds.contains(r.getRecipeID()))
                .sorted(java.util.Comparator.comparing(r -> r.getRecipeName().toLowerCase()))
                .toList();

        String query = searchBar.getText();
        if (query != null && !query.isBlank()) {
            favoriteRecipes = SearchUtil.search(favoriteRecipes, query);
        }

        updateRecipes(favoriteRecipes);
    }

    private void updateRecipes(List<Recipe> newRecipes) {
        Recipe previouslySelected = recipeListView.getSelectionModel().getSelectedItem();
        Long prevId = previouslySelected != null ? previouslySelected.getRecipeID() : null;

        recipes.setAll(newRecipes);

        updateSelection(prevId);
    }

    private void updateSelection(Long prevId) {
        if (prevId != null) {
            for (Recipe r : recipes) {
                if (prevId.equals(r.getRecipeID())) {
                    recipeListView.getSelectionModel().select(r);
                    break;
                }
            }
        }

        if (recipeListView.getSelectionModel().getSelectedItem() == null && !recipes.isEmpty()) {
            recipeListView.getSelectionModel().selectFirst();
        }

        Recipe selectedInList = recipeListView.getSelectionModel().getSelectedItem();
        if (selectedInList == null) {
            selectedRecipe = null;

            deleteRecipeButton.setDisable(true);
            editRecipeButton.setDisable(true);
            downloadButton.setDisable(true);
            cloneRecipeButton.setDisable(true);
            updateFavoriteToggle(null);

            recipeName.setText("");
            ingredientsList.setItems(FXCollections.observableArrayList());
            instructionsList.setItems(FXCollections.observableArrayList());
            recipeLanguage.setText("");

            return;
        }

        selectedRecipe = server.getRecipeById(selectedInList.getRecipeID());

        boolean hasSelection = selectedRecipe != null;

        deleteRecipeButton.setDisable(!hasSelection);
        editRecipeButton.setDisable(!hasSelection);
        downloadButton.setDisable(!hasSelection);
        cloneRecipeButton.setDisable(!hasSelection);

        if (hasSelection) {
            System.out.println(selectedRecipe);
            showRecipeDetails(selectedRecipe);
        }
    }

    private void loadFavorites() {
        try {
            favoriteIds.clear();
            favoriteIds.addAll(FavoritesStorage.loadFavoriteIds());
            recipeListView.refresh();
        } catch (IOException e) {
            if (errorLabel != null) {
                errorLabel.setText("Unable to load Favorites");
            }
        }
    }
}
