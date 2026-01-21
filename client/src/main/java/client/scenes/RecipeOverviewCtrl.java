package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import commons.Instruction;
import commons.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import client.ws.RecipeEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class RecipeOverviewCtrl implements Initializable {

    private Recipe selectedRecipe = null;

    @FXML
    private Button favoritesButton;
    @FXML
    private Button ingredientsButton;
    @FXML
    private Button downloadRecipeButton;
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
    private TextField searchBar;

    @FXML
    private Label ingredientTitle;
    @FXML private MenuButton languageMenu;

    @FXML private MenuItem englishItem;
    @FXML private MenuItem dutchItem;
    @FXML private MenuItem frenchItem;
    @FXML private MenuItem turkishItem;
    @FXML private MenuItem greekItem;


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
    private ResourceBundle resources;


    private final ObservableList<Recipe> recipes =
            FXCollections.observableArrayList();
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

    public void applyFilters() {
        String query = searchBar.getText();
        if (!query.isBlank()) {
            recipes.setAll(
                    SearchUtil.search(server.getRecipes(), query)
            );
        } else {
            refresh();
        }
    }

    public void setWsClient(WsClient ws) {
        this.ws = ws;
    }

    public void goToEditScene() {
        System.out.println(" Go to edit scene ");
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No recipe selected for editing");
            return;
        }

        Recipe fullRecipe = server.getRecipeById(selected.getRecipeID());

        pc.showEditRecipe(fullRecipe);

    }

    public void goToAddScene() {
        System.out.println("Go to add scene");
        pc.showAddRecipe();
    }

    public void goToDownloadRecipe() {
        if (selectedRecipe != null) {
            System.out.println(" Go to download Recipe ");
            pc.showDownloadRecipe(selectedRecipe);
        } else {
            System.out.println(" No Recipe Selected ");
        }
    }

    public void goToFavorites() {
        System.out.println("Go to the Favorites scene *not functional yet*");
    }

    public void goToIngredients() {
        System.out.println("Go to the Ingredients scene");
        pc.showIngredientOverview();
    }

    public void deleteRecipeWarning() {
        System.out.println("Go to the Delete recipe warning");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("recipe.delete.title"));
        alert.setContentText(
                resources.getString("recipe.delete.confirmPrefix")
                        + selectedRecipe.getRecipeName()
                        + resources.getString("recipe.delete.confirmSuffix")
        );

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        server.deleteRecipe(selectedRecipe);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Recipe " + selectedRecipe.getRecipeName() + " deleted");
                });
    }

    public void cloneRecipe() {
        // storing properties of selected recipes (for cloning)
        if (selectedRecipe != null) {
            RecipeHolder holder = RecipeHolder.getInstance();
            holder.setRecipe(selectedRecipe);
        }

        goToAddScene();
    }

    public void refresh() {
        System.out.println("Refresh ! (Refresh button clicked or else)");

        Recipe previouslySelected = recipeListView.getSelectionModel().getSelectedItem();
        Long prevId = previouslySelected != null ? previouslySelected.getRecipeID() : null;

        recipes.setAll(
                server.getRecipes().stream()
                        .sorted(java.util.Comparator.comparing(r -> r.getRecipeName().toLowerCase()))
                        .toList()
        );

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
            System.out.println(selectedRecipe);
            showRecipeDetails(selectedRecipe);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("RecipeOverviewCtrl initialized");
        this.resources = resourceBundle;

        recipeListView.setItems(recipes);

        recipeListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Recipe item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getRecipeName());
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
                });
        //refresh();
        englishItem.setText(" ");
        englishItem.setGraphic(flagIcon("flags/gb.png"));
        System.out.println(englishItem.getGraphic());

        dutchItem.setText(" ");
        dutchItem.setGraphic(flagIcon("flags/nl.png"));

        frenchItem.setText(" ");
        frenchItem.setGraphic(flagIcon("flags/fr.png"));

        turkishItem.setText(" ");
        turkishItem.setGraphic(flagIcon("flags/tr.png"));

        greekItem.setText(" ");
        greekItem.setGraphic(flagIcon("flags/gr.png"));


    }

    private void handleRecipeListEvent(RecipeEvent ev) {
        switch (ev.type) {
            case RECIPE_ADDED -> {
                Recipe r = new Recipe(ev.title);
                r.setRecipeID(ev.id);
                recipes.add(r);
                recipes.sort(java.util.Comparator.comparing(x -> x.getRecipeName().toLowerCase()));
            }

            case RECIPE_DELETED -> {
                recipes.removeIf(r -> r.getRecipeID() == ev.id);

                if (selectedRecipe != null && selectedRecipe.getRecipeID() == ev.id) {
                    selectedRecipe = null;
                    recipeName.setText("");
                    ingredientsList.setItems(FXCollections.observableArrayList());
                    instructionsList.setItems(FXCollections.observableArrayList());
                    recipeLanguage.setText("");
                }
            }
            case RECIPE_TITLE_UPDATED -> {
                for (int i = 0; i < recipes.size(); i++) {
                    Recipe r = recipes.get(i);
                    if (r.getRecipeID() == ev.id) {
                        r.setRecipeName(ev.title);
                        recipes.set(i, r);
                        break;
                    }
                }
                if (selectedRecipe != null && selectedRecipe.getRecipeID() == ev.id) {
                    recipeName.setText(ev.title);
                }
            }
            default -> {
            }
        }
    }

    public void onAppStart() {
        if (ws != null) {
            ws.subscribeRecipeListStored(this::handleRecipeListEvent);
        }
        refresh();
    }

    private void showRecipeDetails(Recipe recipe) {
        selectedRecipe = recipe;
        System.out.println(
                "DEBUG → recipe id=" + recipe.getRecipeID()
                        + " steps=" + (recipe.getSteps() == null ? "NULL" : recipe.getSteps().size())
        );
        recipeName.setText(recipe.getRecipeName());

        if (recipe.getServings() == 0) {
            ingredientTitle.setText(resources.getString("recipe.ingredients.servingsUnknown"));
        } else {
            ingredientTitle.setText(
                    resources.getString("recipe.ingredients.forPeoplePrefix")
                            + recipe.getServings()
                            + resources.getString("recipe.ingredients.forPeopleSuffix")
            );
        }

        recipeLanguage.setText(
                resources.getString("recipe.languagePrefix") + recipe.getRecipeLanguage()
        );

    }

    public void bindWsStatus() {
        if (ws == null) return;

        ws.addStatusListener(status -> {
            wsStatusLabel.setText(switch (status) {
                case CONNECTED -> "Live ● Connected";
                case CONNECTING -> "Connecting…";
                case RECONNECTING -> "Reconnecting…";
                case DISCONNECTED -> "Offline";
            });
        });
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

}