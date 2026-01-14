package client.scenes;

import client.utils.RecipeHolder;
import client.utils.RecipeUtil;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import commons.Language;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeCtrl {

    @FXML private TextField nameField;
    @FXML private ComboBox<Language> languageCombo;

    @FXML private ComboBox<Ingredient> ingredientCombo;
    @FXML private TextField ingredientAmountField;
    @FXML private ComboBox<Unit> unitCombo;
    @FXML private ListView<RecipeIngredient> ingredientsList;

    @FXML private TextArea instructionArea;
    @FXML private ListView<String> instructionsList;

    @FXML private Label errorLabel;

    private final FoodPalCtrl pc;
    private final ServerUtils server;

    @Inject
    public AddRecipeCtrl(FoodPalCtrl pc, ServerUtils server) {
        this.pc = pc;
        this.server = server;
    }

    public void initialize() {
        languageCombo.getItems().setAll(Language.values());
        languageCombo.setValue(Language.EN);

        unitCombo.getItems().setAll(Unit.values());
        unitCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Unit u, boolean empty) {
                super.updateItem(u, empty);

                if (empty || u == null) {
                    setText("Unit");   // ton prompt visuel
                } else {
                    setText(u.name());   // ou u.toString()
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

        ingredientCombo.getItems().setAll(server.getIngredients());
    }

    public void cloneRecipe() {
        System.out.println("Checking for clonable recipe...");

        // checking if a recipe was selected prior
        RecipeHolder holder = RecipeHolder.getInstance();

        if (holder.getRecipe() != null) {
            Recipe r = RecipeUtil.deepCopy(holder.getRecipe().getRecipeName() + " copy", holder.getRecipe());
            nameField.setText(r.getRecipeName());
            languageCombo.setValue(r.getRecipeLanguage());

            ingredientsList.getItems().clear();
            for (RecipeIngredient ri : r.getIngredients()) {
                ingredientsList.getItems().add(ri);
            }

            instructionsList.getItems().clear();
            for (Instruction i : r.getSteps()) {
                instructionsList.getItems().add(i.getDescription());
            }

            System.out.println("Cloned recipe " + holder.getRecipe().getRecipeName());
        } else {
            System.out.println("No recipe to clone");
        }
    }

    private void clear(){
        nameField.setText("");
        languageCombo.setValue(Language.EN);
        ingredientAmountField.setText("");
        ingredientCombo.getSelectionModel().clearSelection();
        ingredientCombo.setValue(null);
        unitCombo.getSelectionModel().clearSelection();
        unitCombo.setValue(null);
        clearIngredients();
        instructionArea.clear();
        clearInstructions();
        errorLabel.setText("");
        RecipeHolder.getInstance().setRecipe(null);
        System.out.println("Clear Recipe name, ingredient and instructions");
    }

    @FXML
    public void updateIngredients() {
        ingredientCombo.getItems().setAll(server.getIngredients());
        System.out.println("updated ingredients");
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
    public void createRecipe() {
        errorLabel.setText("");

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Recipe name required.");
            return;
        }

        Recipe recipe = new Recipe(name);
        recipe.setRecipeLanguage(languageCombo.getValue());


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
            ServerUtils.addRecipe(recipe);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        clear();
        pc.showRecipeOverview();
    }

    @FXML
    public void goBack() {
        clear();
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

}
