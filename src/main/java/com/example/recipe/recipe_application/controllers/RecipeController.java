package com.example.recipe.recipe_application.controllers;

import com.example.recipe.recipe_application.models.Recipe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {
    @GetMapping("/recipe")
    public Recipe getRecipe(@RequestParam Integer id) {
        Recipe recipe = new Recipe(id, "TempName");
        return recipe;
    }
}
