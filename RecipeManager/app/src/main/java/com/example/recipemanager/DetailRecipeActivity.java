package com.example.recipemanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRecipeActivity extends AppCompatActivity {
    private TextView recipeNameTextView;
    private TextView recipeCategoryTextView;
    private TextView recipeIngredientsTextView;
    private TextView recipeStepsTextView;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        recipeNameTextView = findViewById(R.id.recipeNameTextView);
        recipeCategoryTextView = findViewById(R.id.recipeCategoryTextView);
        recipeIngredientsTextView = findViewById(R.id.recipeIngredientsTextView);
        recipeStepsTextView = findViewById(R.id.recipeStepsTextView);

        databaseHelper = new DatabaseHelper(this);

        // Get Recipe ID from Intent
        long recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        }
    }

    private void loadRecipeDetails(long recipeId) {
        Recipe recipe = databaseHelper.getRecipeById(recipeId);
        if (recipe != null) {
            recipeNameTextView.setText(recipe.getName());
            recipeCategoryTextView.setText(recipe.getCategory());
            recipeIngredientsTextView.setText(recipe.getIngredients());
            recipeStepsTextView.setText(recipe.getSteps());
        }
    }
}
