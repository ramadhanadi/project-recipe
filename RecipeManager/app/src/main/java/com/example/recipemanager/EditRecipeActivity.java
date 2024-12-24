package com.example.recipemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText editRecipeName, editRecipeIngredients, editRecipeSteps;
    private Button buttonUpdate, buttonDelete;
    private DatabaseHelper databaseHelper;
    private long recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        editRecipeName = findViewById(R.id.editRecipeName);
        editRecipeIngredients = findViewById(R.id.editRecipeIngredients);
        editRecipeSteps = findViewById(R.id.editRecipeSteps);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        databaseHelper = new DatabaseHelper(this);

        recipeId = getIntent().getLongExtra("RECIPE_ID", -1);

        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        }

        buttonUpdate.setOnClickListener(v -> {
            String name = editRecipeName.getText().toString().trim();
            String ingredients = editRecipeIngredients.getText().toString().trim();
            String steps = editRecipeSteps.getText().toString().trim();

            if (name.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
                Toast.makeText(EditRecipeActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = databaseHelper.updateRecipe(recipeId, name, ingredients, steps);
                if (updated) {
                    Toast.makeText(EditRecipeActivity.this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the main activity
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Failed to update recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(v -> {
            boolean deleted = databaseHelper.deleteRecipe((int) recipeId);
            if (deleted) {
                Toast.makeText(EditRecipeActivity.this, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the main activity
            } else {
                Toast.makeText(EditRecipeActivity.this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecipeDetails(long recipeId) {
        Recipe recipe = databaseHelper.getRecipeById(recipeId);
        if (recipe != null) {
            editRecipeName.setText(recipe.getName());
            editRecipeIngredients.setText(recipe.getIngredients());
            editRecipeSteps.setText(recipe.getSteps());
        } else {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
