package com.example.recipe.recipe_application.controllers;

import com.example.recipe.recipe_application.models.Ingredient;
import com.example.recipe.recipe_application.models.MealType;
import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.repositories.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class RecipeController {
    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/recipe")
    public Recipe getRecipe(@RequestParam Integer id) {
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        return recipe;
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @PostMapping("/recipe")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        recipe.getIngredients().forEach(recipe::addIngredient);
        return recipeRepository.save(recipe);
    }

//    @DeleteMapping("/recipe")

}
