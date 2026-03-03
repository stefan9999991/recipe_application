package com.example.recipe.recipe_application.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Entity
@Table(name="recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "recipe",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Ingredient> ingredients;

    // Time required to cook in minutes
    private Integer time;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    public Recipe() {}

    public Recipe(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Recipe(String name, String description, Set<Ingredient> ingredients, Integer time, MealType mealType) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.time = time;
        this.mealType = mealType;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }
}
