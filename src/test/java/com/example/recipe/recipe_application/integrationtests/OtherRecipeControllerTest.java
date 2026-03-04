package com.example.recipe.recipe_application.integrationtests;

import com.example.recipe.recipe_application.models.Ingredient;
import com.example.recipe.recipe_application.models.MealType;
import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
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
public class OtherRecipeControllerTest {
    public Recipe recipe1;

    public Recipe recipe2;

    public String recipe1String;
    public String recipe2String;

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

        recipe1String = """
                {
                  "name": "Pizza",
                  "description": "I love pizza",
                  "time": 45,
                  "mealType": "DINNER",
                  "ingredients": [
                    { "name": "Tomato", "amount": "1kg" },
                    { "name": "Dough", "amount": "3kg" }
                  ]
                }""";

        recipe2String = """
                {
                  "name": "Pizza Tuna",
                  "description": "I love pizza",
                  "time": 45,
                  "mealType": "DINNER",
                  "ingredients": [
                    { "name": "Cheese", "amount": "2L" },
                    { "name": "Olives", "amount": "2 OZ" }
                  ]
                }""";
    }

    private JsonPath saveRecipe(String jsonRecipe) {
        return given()
                .contentType(ContentType.JSON)
                .body(jsonRecipe)
                .when()
                .post("/recipe")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .jsonPath();
    }

    @Test
    void shouldGetAllRecipes() {
        saveRecipe(recipe1String);
        saveRecipe(recipe2String);

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
        Integer recipe1Id = saveRecipe(recipe1String).get("id");
        saveRecipe(recipe2String);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/recipe/" + recipe1Id)
                .then()
                .statusCode(200)
                .body("id", equalTo(recipe1Id))
                .body("name", equalTo(recipe1.getName()))
                .body("description", equalTo(recipe1.getDescription()))
                .body("time", equalTo(recipe1.getTime()))
                .body("mealType", equalTo(recipe1.getMealType().toString()));
    }

    @Test
    void shouldGetRecipe2BySearch() {
        saveRecipe(recipe1String);
        saveRecipe(recipe2String);

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
        given()
                .contentType(ContentType.JSON)
                .body(recipe1String)
                .when()
                .post("/recipe")
                .then()
                .statusCode(200);

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals("Pizza", recipeRepository.findAll().get(0).getName());
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

        assertEquals(0, recipeRepository.findAll().size());
    }

    @Test
    void shouldDeleteRecipe1() {
        Integer recipe1Id = saveRecipe(recipe1String).get("id");
        Integer recipe2Id = saveRecipe(recipe2String).get("id");

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/recipe/" + recipe1Id)
                .then()
                .statusCode(200);

        assertEquals(1, recipeRepository.findAll().size());
        assertEquals("Pizza Tuna", recipeRepository.findAll().get(0).getName());
    }
}
