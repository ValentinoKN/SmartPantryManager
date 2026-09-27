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
    private static final int DATABASE_VERSION = 1;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add a data-preserving migration before increasing DATABASE_VERSION.
        throw new IllegalStateException(
                "Missing database migration from "
                        + oldVersion + " to " + newVersion);
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

}