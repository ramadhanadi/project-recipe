package com.example.recipemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView recipesListView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesListView = findViewById(R.id.recipesListView);
        databaseHelper = new DatabaseHelper(this);

        // Load recipes from the database
        loadRecipes();

        // Set item click listener to view recipe details
        recipesListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(MainActivity.this, DetailRecipeActivity.class);
            intent.putExtra("RECIPE_ID", id); // Pass the selected Recipe ID
            startActivity(intent);
        });

        // Add recipe button
        findViewById(R.id.addRecipeButton).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddRecipeActivity.class));
        });

        // Set long-click listener to update or delete recipe
        recipesListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            showRecipeOptionsDialog(id);
            return true; // Indicate that the long-click was handled
        });
    }

    private void loadRecipes() {
        Cursor cursor = databaseHelper.getRecipes();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No recipes found!", Toast.LENGTH_SHORT).show();
        } else {
            String[] from = {"name", "category"};
            int[] to = {R.id.recipeName, R.id.recipeCategory};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.recipe_item,
                    cursor,
                    from,
                    to,
                    0
            );

            recipesListView.setAdapter(adapter);
        }
    }

    private void showRecipeOptionsDialog(long recipeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recipe Options");
        builder.setMessage("What do you want to do with this recipe?");
        builder.setPositiveButton("Update", (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, EditRecipeActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });
        builder.setNegativeButton("Delete", (dialog, which) -> {
            boolean isDeleted = databaseHelper.deleteRecipe(recipeId);
            if (isDeleted) {
                Toast.makeText(this, "Recipe deleted successfully!", Toast.LENGTH_SHORT).show();
                loadRecipes();
            } else {
                Toast.makeText(this, "Failed to delete recipe!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes(); // Refresh list when returning to MainActivity
    }
}
