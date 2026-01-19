package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import client.utils.WsClient;
import client.ws.RecipeEvent;
import java.util.ArrayList;
import java.util.List;

public class EditRecipeCtrl {
    private long recipeId;
    @FXML
    private TextField nameField;
    @FXML private ComboBox<String> languageCombo;

    @FXML private ComboBox<Ingredient> ingredientCombo;
    @FXML private TextField ingredientAmountField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private ListView<RecipeIngredient> ingredientsList;

    @FXML private TextArea instructionArea;
    @FXML private ListView<String> instructionsList;

    @FXML private Label errorLabel;

    @FXML private Button editStepButton;
    @FXML private Button addInstructionButton;
    private WsClient ws;
    private long currentRecipeId = -1;
    private final FoodPalCtrl pc;
    private final ServerUtils server;

    @Inject
    public EditRecipeCtrl(FoodPalCtrl pc, ServerUtils server) {
        this.pc = pc;
        this.server = server;
    }

    public void initialize() {
        languageCombo.getItems().setAll("EN", "NL", "GR");
        languageCombo.setValue("EN");

        unitCombo.getItems().setAll(Unit.values());

        unitCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Unit u, boolean empty) {
                super.updateItem(u, empty);

                if (empty || u == null) {
                    setText("Unit");
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
                    setText("Ingredient");   // ← ton prompt visuel
                } else {
                    setText(i.getIngredientName());
                }
            }
        });

        ingredientsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(RecipeIngredient ri, boolean empty) {
                super.updateItem(ri, empty);
                setText(empty || ri == null ? ""
                        : ri.getAmount() + " " + ri.getUnit() + " " + ri.getIngredient().getIngredientName());
            }
        });

        ingredientCombo.getItems().setAll(server.getIngredients().stream()
                .sorted(java.util.Comparator.comparing(
                        i -> i.getIngredientName().toLowerCase()))
                .toList());
    }

    public void setRecipeToEdit(Recipe recipe) {
        this.recipeId = recipe.getRecipeID();
        this.currentRecipeId = recipe.getRecipeID();

        nameField.setText(recipe.getRecipeName());
        languageCombo.setValue(languageToCode(recipe.getRecipeLanguage()));

        ingredientsList.getItems().setAll(recipe.getIngredients());

        instructionsList.getItems().setAll(
                recipe.getSteps().stream()
                        .sorted((a, b) -> Integer.compare(a.getOrderNumber(), b.getOrderNumber()))
                        .map(Instruction::getDescription)
                        .toList()
        );
        instructionsList.setDisable(false);
        instructionArea.setText("");
        editStepButton.setText("Edit Selected");
        addInstructionButton.setDisable(false);
        ingredientAmountField.setText("");
        ingredientCombo.getSelectionModel().clearSelection();
        ingredientCombo.setValue(null);
        unitCombo.getSelectionModel().clearSelection();
        unitCombo.setValue(null);
    }

    public void setWsClient(WsClient ws) {
        this.ws = ws;
    }


    @FXML
    public void addIngredient() {
        errorLabel.setText("");

        Ingredient ing = ingredientCombo.getValue();
        String amountText = ingredientAmountField.getText();
        Unit unit = unitCombo.getValue();

        if (ing == null || unit == null || amountText.isBlank()) {
            errorLabel.setText("Fill ingredient, amount and unit.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            errorLabel.setText("Amount must be a number.");
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
    public void addInstruction() {
        String text = instructionArea.getText().trim();
        if (text.isEmpty()) {
            errorLabel.setText("Instruction cannot be empty.");
            return;
        }
        instructionsList.getItems().add(text);
        instructionArea.clear();
    }

    @FXML
    public void editRecipe() {
        errorLabel.setText("");

        if (currentRecipeId == -1) {
            errorLabel.setText("No recipe selected to edit (missing id).");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Recipe name required.");
            return;
        }

        Recipe recipe = new Recipe(name);
        recipe.setRecipeLanguage(codeToLanguage(languageCombo.getValue()));


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
        System.out.println(recipe.toString());
        try {
            ServerUtils.editRecipe(currentRecipeId, recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (ws != null && currentRecipeId != -1) {
            ws.unsubscribeRecipe(currentRecipeId);
        }
        pc.showRecipeOverview();
    }

    @FXML
    public void removeSelectedIngredient() {
        int idx = ingredientsList.getSelectionModel().getSelectedIndex();
        if (idx >= 0) ingredientsList.getItems().remove(idx);
    }

    @FXML
    public void clearIngredients() {
        ingredientsList.getItems().clear();
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
        if (idx >= 0) instructionsList.getItems().remove(idx);
    }

    @FXML
    public void clearInstructions() {
        instructionsList.getItems().clear();
    }

    @FXML
    public void editSelectedInstruction() {
        int idx = instructionsList.getSelectionModel().getSelectedIndex();
        boolean editingInstruction = false;
        if (idx >= 0 && editStepButton.getText().equals("Edit Selected")) {
            editStepButton.setText("Save Changes");
            instructionArea.setText(instructionsList.getSelectionModel().getSelectedItem());
            editingInstruction = true;
        }
        else if (idx >= 0 && editStepButton.getText().equals("Save Changes")) {
            editStepButton.setText("Edit Selected");
            var item = instructionArea.getText();
            instructionsList.getItems().remove(idx);
            instructionsList.getItems().add(idx , item);
            instructionsList.getSelectionModel().select(idx );
            instructionArea.setText("");
        }
        addInstructionButton.setDisable(editingInstruction);
        instructionsList.setDisable(editingInstruction);
    }


    private Language codeToLanguage(String code) {
        return switch (code) {
            case "EN" -> Language.EN;
            case "NL" -> Language.NL;
            case "GR" -> Language.GR;
            default -> Language.EN;
        };
    }

    private String languageToCode(Language lang) {
        return switch (lang) {
            case Language.EN -> "EN";
            case Language.NL -> "NL";
            case Language.GR -> "GR";
            default -> "EN";
        };
    }
    private void loadRecipeIntoFields(Recipe recipe) {
        nameField.setText(recipe.getRecipeName());
        languageCombo.setValue(languageToCode(recipe.getRecipeLanguage()));

        ingredientsList.getItems().setAll(recipe.getIngredients());

        instructionsList.getItems().setAll(
                recipe.getSteps().stream()
                        .sorted((a, b) -> Integer.compare(a.getOrderNumber(), b.getOrderNumber()))
                        .map(Instruction::getDescription)
                        .toList()
        );

        instructionsList.setDisable(false);
        instructionArea.setText("");
        editStepButton.setText("Edit Selected");
        addInstructionButton.setDisable(false);

        ingredientAmountField.setText("");
        ingredientCombo.getSelectionModel().clearSelection();
        ingredientCombo.setValue(null);
        unitCombo.getSelectionModel().clearSelection();
        unitCombo.setValue(null);
    }

    private void handleRecipeEvent(RecipeEvent ev) {
        if (ev == null || ev.type == null) return;

        if (ev.type == RecipeEvent.Type.RECIPE_UPDATED && ev.id == currentRecipeId) {

            boolean userTyping = nameField.isFocused() || instructionArea.isFocused();
            if (userTyping) {
                errorLabel.setText("This recipe was changed in another client. Saving may overwrite changes.");
                return;
            }
            Recipe fresh = ServerUtils.getRecipeById(currentRecipeId);
            loadRecipeIntoFields(fresh);
            errorLabel.setText("Synced latest changes.");
        }
    }
    @FXML
    public void goBack() {
        if (ws != null && currentRecipeId != -1) {
            ws.unsubscribeRecipe(currentRecipeId);
        }
        pc.showRecipeOverview();
    }
}
