package client.scenes;

import client.utils.ServerUtils;
import client.utils.WsClient;
import client.ws.RecipeEvent;
import com.google.inject.Inject;
import commons.Ingredient;
import commons.Instruction;
import commons.Language;
import commons.Recipe;
import commons.RecipeIngredient;
import commons.Unit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditRecipeCtrl implements Initializable {

    private long recipeId;
    private long currentRecipeId = -1;

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<Language> languageCombo;
    @FXML
    private TextField servingsNumber;

    @FXML
    private ComboBox<Ingredient> ingredientCombo;
    @FXML
    private TextField ingredientAmountField;
    @FXML
    private ComboBox<Unit> unitCombo;
    @FXML
    private ListView<RecipeIngredient> ingredientsList;

    @FXML
    private TextArea instructionArea;
    @FXML
    private ListView<String> instructionsList;

    @FXML
    private Label errorLabel;

    @FXML
    private Button editStepButton;
    @FXML
    private Button addInstructionButton;

    private WsClient ws;

    private final FoodPalCtrl pc;
    private final ServerUtils server;

    private ResourceBundle bundle;
    private boolean editingInstruction = false;

    @Inject
    public EditRecipeCtrl(FoodPalCtrl pc, ServerUtils server) {
        this.pc = pc;
        this.server = server;
    }

    public void setWsClient(WsClient ws) {
        this.ws = ws;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;

        languageCombo.getItems().setAll(Language.values());
        languageCombo.setValue(Language.EN);

        unitCombo.getItems().setAll(Unit.values());

        unitCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Unit u, boolean empty) {
                super.updateItem(u, empty);
                if (empty || u == null) {
                    setText(bundle.getString("editRecipe.unitPrompt"));
                } else {
                    setText(u.name());
                }
            }
        });
        unitCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Unit u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? "" : u.name());
            }
        });

        ingredientCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Ingredient i, boolean empty) {
                super.updateItem(i, empty);
                setText(empty || i == null ? "" : i.getIngredientName());
            }
        });
        ingredientCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Ingredient i, boolean empty) {
                super.updateItem(i, empty);
                if (empty || i == null) {
                    setText(bundle.getString("editRecipe.ingredientPrompt"));
                } else {
                    setText(i.getIngredientName());
                }
            }
        });

        ingredientsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(RecipeIngredient ri, boolean empty) {
                super.updateItem(ri, empty);
                if (empty || ri == null) {
                    setText("");
                    return;
                }
                setText(ri.getAmount() + " " + ri.getUnit() + " " + ri.getIngredient().getIngredientName());
            }
        });

        updateIngredients();
        updateEditingUi(false);
    }

    @FXML
    public void updateIngredients() {
        ingredientCombo.getItems().setAll(
                server.getIngredients().stream()
                        .sorted(java.util.Comparator.comparing(i -> i.getIngredientName().toLowerCase()))
                        .toList()
        );
    }

    public void setRecipeToEdit(Recipe recipe) {
        this.recipeId = recipe.getRecipeID();
        this.currentRecipeId = recipe.getRecipeID();

        loadRecipeIntoFields(recipe);
        errorLabel.setText("");
        updateEditingUi(false);
    }

    @FXML
    public void addIngredient() {
        errorLabel.setText("");

        Ingredient ing = ingredientCombo.getValue();
        String amountText = ingredientAmountField.getText();
        Unit unit = unitCombo.getValue();

        if (ing == null || unit == null || amountText == null || amountText.isBlank()) {
            errorLabel.setText(bundle.getString("editRecipe.error.fillIngredientAmountUnit"));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (NumberFormatException e) {
            errorLabel.setText(bundle.getString("editRecipe.error.amountNumber"));
            return;
        }

        if (amount <= 0) {
            errorLabel.setText(bundle.getString("editRecipe.error.amountPositive"));
            return;
        }

        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(ing);
        ri.setAmount((int) amount);
        ri.setUnit(unit);

        ingredientsList.getItems().add(ri);
        ingredientAmountField.clear();
    }

    @FXML
    public void removeSelectedIngredient() {
        int idx = ingredientsList.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            ingredientsList.getItems().remove(idx);
        }
    }

    @FXML
    public void clearIngredients() {
        ingredientsList.getItems().clear();
    }

    @FXML
    public void addInstruction() {
        errorLabel.setText("");

        String text = instructionArea.getText() == null ? "" : instructionArea.getText().trim();
        if (text.isEmpty()) {
            errorLabel.setText(bundle.getString("editRecipe.error.instructionEmpty"));
            return;
        }

        instructionsList.getItems().add(text);
        instructionArea.clear();
    }

    @FXML
    public void moveInstructionUp() {
        int idx = instructionsList.getSelectionModel().getSelectedIndex();
        if (idx > 0) {
            var item = instructionsList.getItems().remove(idx);
            instructionsList.getItems().add(idx - 1, item);
            instructionsList.getSelectionModel().select(idx - 1);
        }
    }

    @FXML
    public void moveInstructionDown() {
        int idx = instructionsList.getSelectionModel().getSelectedIndex();
        if (idx >= 0 && idx < instructionsList.getItems().size() - 1) {
            var item = instructionsList.getItems().remove(idx);
            instructionsList.getItems().add(idx + 1, item);
            instructionsList.getSelectionModel().select(idx + 1);
        }
    }

    @FXML
    public void removeSelectedInstruction() {
        int idx = instructionsList.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            instructionsList.getItems().remove(idx);
        }
    }

    @FXML
    public void clearInstructions() {
        instructionsList.getItems().clear();
    }

    @FXML
    public void editSelectedInstruction() {
        int idx = instructionsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            return;
        }

        if (!editingInstruction) {
            instructionArea.setText(instructionsList.getSelectionModel().getSelectedItem());
            updateEditingUi(true);
            return;
        }

        String newText = instructionArea.getText() == null ? "" : instructionArea.getText().trim();
        if (newText.isEmpty()) {
            errorLabel.setText(bundle.getString("editRecipe.error.instructionEmpty"));
            return;
        }

        instructionsList.getItems().set(idx, newText);
        instructionsList.getSelectionModel().select(idx);
        instructionArea.clear();

        updateEditingUi(false);
    }

    private void updateEditingUi(boolean editing) {
        this.editingInstruction = editing;

        editStepButton.setText(
                editing
                        ? bundle.getString("button.saveChanges")
                        : bundle.getString("button.editSelected")
        );

        addInstructionButton.setDisable(editing);
        instructionsList.setDisable(editing);
    }

    @FXML
    public void editRecipe() {
        errorLabel.setText("");

        if (currentRecipeId == -1) {
            errorLabel.setText(bundle.getString("editRecipe.error.noRecipeId"));
            return;
        }

        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText(bundle.getString("editRecipe.error.nameRequired"));
            return;
        }

        String servingsStr = servingsNumber.getText() == null ? "" : servingsNumber.getText().trim();
        if (servingsStr.isBlank()) {
            errorLabel.setText(bundle.getString("editRecipe.error.servingsRequired"));
            return;
        }

        int servings;
        try {
            servings = Integer.parseInt(servingsStr);
        } catch (NumberFormatException e) {
            errorLabel.setText(bundle.getString("editRecipe.error.servingsInteger"));
            return;
        }

        if (servings <= 0) {
            errorLabel.setText(bundle.getString("editRecipe.error.servingsAtLeast1"));
            return;
        }

        Recipe recipe = new Recipe(name);
        recipe.setRecipeLanguage(languageCombo.getValue());
        recipe.setServings(servings);

        List<Instruction> steps = new ArrayList<>();
        for (int i = 0; i < instructionsList.getItems().size(); i++) {
            Instruction ins = new Instruction();
            ins.setDescription(instructionsList.getItems().get(i));
            ins.setOrderNumber(i + 1);
            ins.setRecipe(recipe);
            steps.add(ins);
        }
        recipe.setSteps(steps);

        for (RecipeIngredient ri : ingredientsList.getItems()) {
            ri.setRecipe(recipe);
        }
        recipe.setIngredients(new ArrayList<>(ingredientsList.getItems()));

        try {
            ServerUtils.editRecipe(currentRecipeId, recipe);
        } catch (Exception e) {
            errorLabel.setText(bundle.getString("editRecipe.error.saveFailed"));
            throw new RuntimeException(e);
        }

        if (ws != null && currentRecipeId != -1) {
            ws.unsubscribeRecipe(currentRecipeId);
        }
        pc.showRecipeOverview();
    }

    @FXML
    public void goBack() {
        if (ws != null && currentRecipeId != -1) {
            ws.unsubscribeRecipe(currentRecipeId);
        }
        pc.showRecipeOverview();
    }

    private void loadRecipeIntoFields(Recipe recipe) {
        nameField.setText(recipe.getRecipeName());
        languageCombo.setValue(recipe.getRecipeLanguage());
        servingsNumber.setText(Integer.toString(recipe.getServings()));

        ingredientsList.getItems().setAll(recipe.getIngredients());

        instructionsList.getItems().setAll(
                recipe.getSteps().stream()
                        .sorted((a, b) -> Integer.compare(a.getOrderNumber(), b.getOrderNumber()))
                        .map(Instruction::getDescription)
                        .toList()
        );

        instructionArea.setText("");
        ingredientAmountField.setText("");

        ingredientCombo.getSelectionModel().clearSelection();
        ingredientCombo.setValue(null);

        unitCombo.getSelectionModel().clearSelection();
        unitCombo.setValue(null);
    }

    private void handleRecipeEvent(RecipeEvent ev) {
        if (ev == null || ev.type == null) {
            return;
        }

        if (ev.type == RecipeEvent.Type.RECIPE_UPDATED && ev.id == currentRecipeId) {
            boolean userTyping = nameField.isFocused() || instructionArea.isFocused();
            if (userTyping) {
                errorLabel.setText(bundle.getString("editRecipe.warn.changedElsewhere"));
                return;
            }

            Recipe fresh = ServerUtils.getRecipeById(currentRecipeId);
            loadRecipeIntoFields(fresh);
            errorLabel.setText(bundle.getString("editRecipe.info.synced"));
        }
    }
}
