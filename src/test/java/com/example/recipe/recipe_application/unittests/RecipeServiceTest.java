package com.example.recipe.recipe_application.unittests;

import com.example.recipe.recipe_application.models.Ingredient;
import com.example.recipe.recipe_application.models.MealType;
import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import com.example.recipe.recipe_application.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe recipe1;

    private Recipe recipe2;

    @BeforeEach
    void setUp() {
        recipe1 = new Recipe("Pizza", "I love pizza", new HashSet<>(), 45, MealType.DINNER);
        recipe1.setId(1);

        recipe2 = new Recipe("Pizza Tuna", "I love pizza", new HashSet<>(), 45, MealType.DINNER);
        recipe2.setId(2);
    }

    @Test
    void getRecipe_whenExists() {
        when(recipeRepository.findById(1))
                .thenReturn(Optional.of(recipe1));

        Recipe result = recipeService.getRecipe(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Pizza", result.getName());
    }

    @Test
    void getRecipe_shouldThrowNotFound_whenNotExists() {
        when(recipeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> recipeService.getRecipe(1));
    }

    // ---------------- GET ALL ----------------

    @Test
    void getAllRecipes_shouldReturnList() {
        List<Recipe> recipes = List.of(recipe1, recipe2);

        when(recipeRepository.findByNameContaining(""))
                .thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes("");

        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    @Test
    void getAllRecipes_withSearchPizza_shouldReturnList() {
        List<Recipe> recipes = List.of(recipe1, recipe2);

        when(recipeRepository.findByNameContaining("Pizza"))
                .thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes("Pizza");

        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    @Test
    void getAllRecipes_withSearchTuna_shouldReturnList() {
        List<Recipe> recipes = List.of(recipe2);

        when(recipeRepository.findByNameContaining("Tuna"))
                .thenReturn(recipes);

        List<Recipe> result = recipeService.getAllRecipes("Tuna");

        assertEquals(1, result.size());
        assertEquals("Pizza Tuna", result.get(0).getName());
    }

    // ---------------- CREATE ----------------

    @Test
    void createRecipe_shouldSave_whenValid() {
        recipe1.setId(null);

        Ingredient ingredient = new Ingredient("Tomato", "1kg");
        recipe1.addIngredient(ingredient);

        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(recipe1);

        Recipe result = recipeService.createRecipe(recipe1);

        assertNotNull(result);
        verify(recipeRepository).save(recipe1);
    }

    @Test
    void createRecipe_shouldThrowBadRequest_whenIdProvided() {
        assertThrows(ResponseStatusException.class,
                () -> recipeService.createRecipe(recipe1));
    }

    // ---------------- DELETE ----------------

    @Test
    void deleteRecipe_shouldDelete_whenExists() {
        when(recipeRepository.findById(1))
                .thenReturn(Optional.of(recipe1));

        recipeService.deleteRecipe(1);

        verify(recipeRepository).delete(recipe1);
    }

    @Test
    void deleteRecipe_shouldThrowNotFound_whenNotExists() {
        when(recipeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> recipeService.deleteRecipe(1));
    }
}
