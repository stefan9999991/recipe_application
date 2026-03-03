package com.example.recipe.recipe_application.controllers;

import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Integer id) {
        return recipeService.getRecipe(id);
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(@RequestParam(defaultValue = "") String search) {
        return recipeService.getAllRecipes(search);
    }

    @PostMapping("/recipe")
    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @DeleteMapping("/recipe/{id}")
    public void deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);
    }
}
