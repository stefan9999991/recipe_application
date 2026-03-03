package com.example.recipe.recipe_application.services;

import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe getRecipe(Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return recipe;
    }

    public List<Recipe> getAllRecipes(String search) {
        return recipeRepository.findByNameContaining(search);
    }

    public Recipe createRecipe(Recipe recipe) {
        if (recipe.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipe Id not allowed");
        }
        recipe.getIngredients().forEach(ingredient -> ingredient.setId(null));
        recipe.getIngredients().forEach(recipe::addIngredient);
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        recipeRepository.delete(recipe);
    }
}
