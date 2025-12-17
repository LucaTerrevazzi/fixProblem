package server.api;

import java.util.List;

import commons.Recipe;
import server.service.RecipeService;

public class TestRecipeService extends RecipeService {

    public TestRecipeService() {
        super(null,null,null);
    }

    @Override
    public List<Recipe> findAll() {
        Recipe pasta = new Recipe();
        pasta.setRecipeName("Pasta");

        Recipe pizza = new Recipe();
        pizza.setRecipeName("Pizza");

        return List.of(pasta, pizza);
    }

    @Override
    public Recipe findById(long id) {
        Recipe pasta = new Recipe();
        pasta.setRecipeName("Pasta");
        return pasta;
    }
}
