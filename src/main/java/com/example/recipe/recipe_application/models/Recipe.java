package com.example.recipe.recipe_application.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Entity
@Setter
@Table(name="recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "Recipe name", example = "Pizza")
    @NotBlank
    private String name;

    @Schema(description = "Recipe description", example = "Pizza is a Italian cuisine. Put in oven for 10m")
    @NotBlank
    private String description;

    @Schema(description = "List of ingredients", example = "[\n" +
            "    { \"name\": \"Dough\", \"amount\": \"1kg\" },\n" +
            "    { \"name\": \"Tomato\", \"amount\": \"1L\" }]")
    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "recipe",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<Ingredient> ingredients;

    @Schema(description = "Recipe cooking time in minutes", example = "45")
    @NotNull
    private Integer time;

    @Schema(description = "Recipe Type. Options are: BREAKFAST, LUNCH, DINNER", example = "DINNER")
    @NotNull
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
