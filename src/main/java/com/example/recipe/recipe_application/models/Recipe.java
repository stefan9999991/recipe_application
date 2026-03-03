package com.example.recipe.recipe_application.models;

import lombok.*;

import java.util.List;

@Getter
public class Recipe {
    private Integer id;
    private String name;
    private String description;
    private List<String> ingredients;

    // Time required to cook in minutes
    private Integer time;
    private MealType mealType;

    public Recipe() {}

    public Recipe(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
