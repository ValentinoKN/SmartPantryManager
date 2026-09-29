package com.valentino.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 3;
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY, "
                        + COLUMN_NAME + " TEXT NOT NULL "
                        + "CHECK(length(trim(name)) > 0), "
                        + COLUMN_QUANTITY + " REAL NOT NULL "
                        + "CHECK(quantity > 0), "
                        + COLUMN_UNIT + " TEXT NOT NULL "
                        + "CHECK(unit IN ('g', 'kg', 'ml', 'l', 'each'))"
                        + ")";

        db.execSQL(createPantryTable);
        createRecipeTables(db);
        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add the new tables without dropping the pantry the user has already saved.
        if (oldVersion < 2) {
            createRecipeTables(db);
        }

        // Seed during this upgrade only, not every time a screen opens.
        if (oldVersion < 3) {
            RecipeSeeder.seed(db);
        }
    }
    public long insertIngredient(String name, double quantity, String unit) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name.trim());
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);

        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<PantryItem> getAllIngredients() {
        List<PantryItem> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_QUANTITY,
                COLUMN_UNIT
        };

        try (Cursor cursor = db.query(
                TABLE_PANTRY,
                columns,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " COLLATE NOCASE ASC, " + COLUMN_ID + " ASC")) {

            int idColumn = cursor.getColumnIndexOrThrow(COLUMN_ID);
            int nameColumn = cursor.getColumnIndexOrThrow(COLUMN_NAME);
            int quantityColumn = cursor.getColumnIndexOrThrow(COLUMN_QUANTITY);
            int unitColumn = cursor.getColumnIndexOrThrow(COLUMN_UNIT);

            while (cursor.moveToNext()) {
                PantryItem ingredient = new PantryItem(
                        cursor.getLong(idColumn),
                        cursor.getString(nameColumn),
                        cursor.getDouble(quantityColumn),
                        cursor.getString(unitColumn));

                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }

    public PantryItem getIngredientById(long id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_QUANTITY,
                COLUMN_UNIT
        };

        try (Cursor cursor = db.query(
                TABLE_PANTRY,
                columns,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                return new PantryItem(
                        cursor.getLong(
                                cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COLUMN_UNIT)));
            }
        }

        return null;
    }

    public int updateIngredient(
            long id, String name, double quantity, String unit) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name.trim());
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);

        // Use the ID so other entries with the same name are left alone.
        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteIngredient(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void createRecipeTables(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE recipes ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "name TEXT NOT NULL CHECK(length(trim(name)) > 0), "
                        + "steps TEXT NOT NULL CHECK(length(trim(steps)) > 0)"
                        + ")");

        db.execSQL(
                "CREATE TABLE recipe_ingredients ("
                        + "_id INTEGER PRIMARY KEY, "
                        + "recipe_id INTEGER NOT NULL, "
                        + "name TEXT NOT NULL CHECK(length(trim(name)) > 0), "
                        + "quantity REAL NOT NULL CHECK(quantity > 0), "
                        + "unit TEXT NOT NULL "
                        + "CHECK(unit IN ('g', 'kg', 'ml', 'l', 'each')), "
                        + "FOREIGN KEY(recipe_id) REFERENCES recipes(_id) "
                        + "ON DELETE CASCADE"
                        + ")");

        db.execSQL(
                "CREATE INDEX index_recipe_ingredients_recipe_id "
                        + "ON recipe_ingredients(recipe_id)");
    }

    public List<Recipe> getSuggestedRecipes() {
        SQLiteDatabase db = getReadableDatabase();
        List<PantryItem> pantry = getAllIngredients();

        return RecipeMatcher.findMatches(db, pantry);
    }

    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                "recipes",
                new String[]{"_id", "name", "steps"},
                "_id = ?",
                new String[]{String.valueOf(id)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                return new Recipe(
                        cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("steps")));
            }
        }

        return null;
    }

    public List<PantryItem> getRecipeIngredients(long recipeId) {
        List<PantryItem> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                "recipe_ingredients",
                new String[]{"_id", "name", "quantity", "unit"},
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null, null, "_id ASC")) {

            while (cursor.moveToNext()) {
                ingredients.add(new PantryItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit"))));
            }
        }

        return ingredients;
    }
}