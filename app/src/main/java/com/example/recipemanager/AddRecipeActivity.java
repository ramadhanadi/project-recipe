package com.example.recipemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText recipeNameEditText, ingredientsEditText, stepsEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private DatabaseHelper databaseHelper;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Initialize views
        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        stepsEditText = findViewById(R.id.stepsEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Setup spinner for category selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set spinner item selected listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Makanan"; // Default category
            }
        });

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String name = recipeNameEditText.getText().toString().trim();
            String ingredients = ingredientsEditText.getText().toString().trim();
            String steps = stepsEditText.getText().toString().trim();

            if (name.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
                Toast.makeText(AddRecipeActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = databaseHelper.addRecipe(name, ingredients, steps, selectedCategory);

                if (isInserted) {
                    Toast.makeText(AddRecipeActivity.this, "Recipe added successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddRecipeActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Failed to add recipe!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
