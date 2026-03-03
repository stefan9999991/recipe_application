package com.example.recipe.recipe_application.repositories;

import com.example.recipe.recipe_application.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByNameContaining(String keyword);
}