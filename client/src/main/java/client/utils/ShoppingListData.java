package client.utils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListData {
    private List<Long> ingredients = new ArrayList<>();
    private List<Long> recipesForIngredients = new ArrayList<>();

    public List<Long> getIngredients() {
        return ingredients;
    }

    public List<Long> getRecipes() {
        return recipesForIngredients;
    }

}
