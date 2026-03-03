package com.example.recipe.recipe_application.repositories;

import com.example.recipe.recipe_application.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {}