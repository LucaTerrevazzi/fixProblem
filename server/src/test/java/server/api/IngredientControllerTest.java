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
package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Person;
import commons.Ingredient;

public class IngredientControllerTest {

    private TestIngredientRepository repo;
    private IngredientController sut;

    @BeforeEach
    public void setup() {
        repo = new TestIngredientRepository();
        sut = new IngredientController(repo);
    }

    @Test
    public void cannotAddNullPerson() {
        var actual = sut.add(getIngredient(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseIsUsed() {
        sut.add(getIngredient("ing1"));
        repo.calledMethods.contains("save");
    }

    private static Ingredient getIngredient(String ing) {
        return new Ingredient(ing);
    }


}