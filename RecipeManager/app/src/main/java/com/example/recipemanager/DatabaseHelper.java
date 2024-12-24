package com.example.recipemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RecipeManager.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_RECIPES = "recipes";

    // Users Table Columns
    private static final String COLUMN_USER_ID = "_id";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Recipes Table Columns
    private static final String COLUMN_RECIPE_ID = "_id";
    private static final String COLUMN_RECIPE_NAME = "name";
    private static final String COLUMN_RECIPE_CATEGORY = "category";
    private static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    private static final String COLUMN_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Recipes Table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RECIPE_CATEGORY + " TEXT, " +
                COLUMN_RECIPE_INGREDIENTS + " TEXT, " +
                COLUMN_RECIPE_STEPS + " TEXT)";
        db.execSQL(createRecipesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // User Methods
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USER_EMAIL + " = ? AND " +
                COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Recipe Methods
    // Add Recipe
    public boolean addRecipe(String name, String ingredients, String steps, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        values.put(COLUMN_RECIPE_STEPS, steps);
        values.put(COLUMN_RECIPE_CATEGORY, category);

        long result = db.insert(TABLE_RECIPES, null, values);
        return result != -1;
    }


    /*public Cursor getRecipesByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE " +
                COLUMN_RECIPE_CATEGORY + " = ? ORDER BY " + COLUMN_RECIPE_NAME, new String[]{category});
    }*/

    // Get Recipe by ID
    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM recipes WHERE _id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            Recipe recipe = new Recipe(
                    cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ingredients")),
                    cursor.getString(cursor.getColumnIndexOrThrow("steps")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category"))
            );
            cursor.close();
            return recipe;
        }
        return null;
    }



    // Update Recipe
    public boolean updateRecipe(long id, String name, String ingredients, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("steps", steps);

        int result = db.update("recipes", values, "_id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Delete Recipe
    public boolean deleteRecipe(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("recipes", "_id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    public Cursor getRecipesByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _id, name, ingredients, steps FROM recipes WHERE category = ? ORDER BY name", new String[]{category});
    }

    public Cursor getRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT _id, name, category FROM recipes ORDER BY category, name";
        return db.rawQuery(query, null);
    }


}
