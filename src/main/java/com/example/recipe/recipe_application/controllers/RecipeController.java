package com.example.recipe.recipe_application.controllers;

import com.example.recipe.recipe_application.models.Recipe;
import com.example.recipe.recipe_application.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary="retrieves recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe returned"),
            @ApiResponse(responseCode = "404", description = "Recipe not found", content=@Content)
    })
    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Integer id) {
        return recipeService.getRecipe(id);
    }

    @Operation(summary="retrieves all recipes by name. Leave blank for all recipes")
    @ApiResponse(responseCode = "200", description = "Recipes returned")
    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(@RequestParam(defaultValue = "") String search) {
        return recipeService.getAllRecipes(search);
    }

    @Operation(summary="creates a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe created"),
            @ApiResponse(responseCode = "400",
                    description = "Bad recipe create request. All fields are required except id",
                    content=@Content)
    })
    @PostMapping("/recipe")
    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @Operation(summary="deletes a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe to delete not found", content=@Content)
    })
    @DeleteMapping("/recipe/{id}")
    public void deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);
    }
}
