package com.example.recipe.recipe_application.controllers;

import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return recipe;
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(@RequestParam(defaultValue = "") String search) {
        return recipeRepository.findByNameContaining(search);
    }

    @PostMapping("/recipe")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        if (recipe.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipe Id not allowed");
        }
        recipe.getIngredients().forEach(ingredient -> ingredient.setId(null));
        recipe.getIngredients().forEach(recipe::addIngredient);
        return recipeRepository.save(recipe);
    }

    @DeleteMapping("/recipe/{id}")
    public void deleteRecipe(@PathVariable Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        recipeRepository.delete(recipe);
    }
}
