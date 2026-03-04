package com.example.recipe.recipe_application.integrationtests;

import com.example.recipe.recipe_application.models.Ingredient;
import com.example.recipe.recipe_application.models.MealType;
import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashSet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerTest {
    public Recipe recipe1;

    public Recipe recipe2;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        recipeRepository.deleteAll();

        recipe1 = new Recipe("Pizza", "I love pizza", new HashSet<>(), 45, MealType.DINNER);
        recipe1.addIngredient(new Ingredient("Tomato", "1kg"));
        recipe1.addIngredient(new Ingredient("Dough", "3kg"));

        recipe2 = new Recipe("Pizza Tuna", "I love pizza", new HashSet<>(), 45, MealType.DINNER);
        recipe2.addIngredient(new Ingredient("Cheese", "2L"));
        recipe2.addIngredient(new Ingredient("Olives", "2 OZ"));
    }

    @Test
    void shouldGetAllRecipes() {
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/recipes")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    void shouldGetRecipe1() {
        Recipe savedRecipe = recipeRepository.save(recipe1);
        Integer recipe1Id = savedRecipe.getId();
        recipeRepository.save(recipe2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/recipe/" + recipe1Id)
                .then()
                .statusCode(200)
                .body("id", equalTo(recipe1Id))
                .body("name", equalTo(savedRecipe.getName()))
                .body("description", equalTo(savedRecipe.getDescription()))
                .body("time", equalTo(savedRecipe.getTime()))
                .body("mealType", equalTo(savedRecipe.getMealType().toString()));
    }

    @Test
    void shouldGetRecipe2BySearch() {
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/recipes?search=Tuna")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void shouldCreateRecipe() {
        String recipe1String = """
                {
                  "name": "Pizza",
                  "description": "I love pizza",
                  "time": 45,
                  "mealType": "DINNER",
                  "ingredients": [
                    { "name": "Dough", "amount": "1kg" },
                    { "name": "Tomato", "amount": "1L" },
                    { "name": "Cheese", "amount": "3 Pounds" }
                  ]
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(recipe1String)
                .when()
                .post("/recipe")
                .then()
                .statusCode(200);

        assertEquals(recipeRepository.findAll().size(), 1);
        assertEquals(recipeRepository.findAll().get(0).getName(), "Pizza");
    }

    @Test
    void shouldGiveErrorNoName() {
        String recipe1String = """
                {
                  "description": "I love pizza",
                  "time": 45,
                  "mealType": "DINNER",
                  "ingredients": [
                    { "name": "Dough", "amount": "1kg" },
                    { "name": "Tomato", "amount": "1L" },
                    { "name": "Cheese", "amount": "3 Pounds" }
                  ]
                }""";

        given()
                .contentType(ContentType.JSON)
                .body(recipe1String)
                .when()
                .post("/recipe")
                .then()
                .statusCode(400);

        assertEquals(recipeRepository.findAll().size(), 0);
    }

    @Test
    void shouldDeleteRecipe1() {
        Integer recipe1Id = recipeRepository.save(recipe1).getId();
        recipeRepository.save(recipe2);

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/recipe/" + recipe1Id)
                .then()
                .statusCode(200);

        assertEquals(recipeRepository.findAll().size(), 1);
        assertEquals(recipeRepository.findAll().get(0).getName(), "Pizza Tuna");
    }
}
