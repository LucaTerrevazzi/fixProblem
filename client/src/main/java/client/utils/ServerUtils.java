/*
    * Copyright 2021 Delft University of Technology
    *
    * Licensed under the Apache License, Version 2.0 (the "License");
    * you may not use this file except in compliance with the License.
    * You may obtain a copy of the License at
    *
    *    http://www.apache.org/licenses/LICENSE-2.0
    *
    * Unless required by applicable law or agreed to in writing, software
    * distributed under the License is distributed on an "AS IS" BASIS,
    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    * See the License for the specific language governing permissions and
    * limitations under the License.
    */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.ConnectException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import commons.Ingredient;
import commons.Recipe;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {
    private static final String SERVER = "http://localhost:8080/";
    public boolean isServerAvailable() {
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER)
                    .request(APPLICATION_JSON)
                    .get();
        } catch (ProcessingException e) {
            if (e.getCause() instanceof ConnectException) {
                return false;
            }
        }
        return true;
    }
    public List<Recipe> getRecipes() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("recipes")
                .request(APPLICATION_JSON)
                .get(new GenericType<List<Recipe>>() {});
    }

    public List<Ingredient> getIngredients() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/ingredients")
                .request(APPLICATION_JSON)
                .get(new GenericType<List<Ingredient>>() {});
    }
    public Recipe getRecipeById(long id) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER)
                .path("recipes/" + id)
                .request(APPLICATION_JSON)
                .get(Recipe.class);
    }

    public static Recipe addRecipe(Recipe recipe) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(recipe);
        System.out.println("JSON being sent:");
        System.out.println(json);
        return ClientBuilder.newClient()
                .target(SERVER)
                .path("recipes")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(recipe, MediaType.APPLICATION_JSON),
                        Recipe.class);
    }

    public static Ingredient addIngredient(Ingredient ingredient) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ingredient);
        System.out.println("JSON being sent:");
        System.out.println(json);
        return ClientBuilder.newClient()
                .target(SERVER)
                .path("api/ingredients")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(ingredient, MediaType.APPLICATION_JSON),
                        Ingredient.class);
    }

}