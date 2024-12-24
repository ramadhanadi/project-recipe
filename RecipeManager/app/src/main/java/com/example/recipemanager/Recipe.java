package com.example.recipemanager;

public class Recipe {
    private long id;
    private String name;
    private String ingredients;
    private String steps;
    private String category;

    public Recipe(long id, String name, String ingredients, String steps, String category) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.category = category;
    }

    // Getters and Setters
    public long getId() { return id; }
    public String getName() { return name; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getCategory() { return category; }
}
