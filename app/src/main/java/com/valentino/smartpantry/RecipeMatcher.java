package com.valentino.smartpantry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeMatcher {

    private static String normaliseName(String name) {
        String cleaned = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        // Keep aliases explicit. Removing the last s from every word would break names.
        switch (cleaned) {
            case "eggs":
                return "egg";
            case "bananas":
                return "banana";
            case "apples":
                return "apple";
            case "tomatoes":
                return "tomato";
            case "potatoes":
                return "potato";
            case "cucumbers":
                return "cucumber";
            case "chickpeas":
                return "chickpea";
            case "kidney beans":
                return "kidney bean";
            case "plain yogurt":
                return "plain yoghurt";
            default:
                return cleaned;
        }
    }

    private static String baseUnit(String unit) {
        switch (unit) {
            case "g":
            case "kg":
                return "g";
            case "ml":
            case "l":
                return "ml";
            case "each":
                return "each";
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    private static BigDecimal baseQuantity(double quantity, String unit) {
        BigDecimal amount = BigDecimal.valueOf(quantity);

        if (unit.equals("kg") || unit.equals("l")) {
            return amount.multiply(BigDecimal.valueOf(1000));
        }

        return amount;
    }

    private static void addAmount(
            Map<String, BigDecimal> totals,
            String name,
            double quantity,
            String unit) {

        // The unit is part of the key: grams must never satisfy a millilitre requirement.
        String key = normaliseName(name) + "|" + baseUnit(unit);
        BigDecimal previous = totals.get(key);

        if (previous == null) {
            previous = BigDecimal.ZERO;
        }

        // Separate pantry entries still contribute to the same compatible total.
        totals.put(key, previous.add(baseQuantity(quantity, unit)));
    }

    public static List<Recipe> findMatches(
            SQLiteDatabase db, List<PantryItem> pantry) {

        Map<String, BigDecimal> available = new HashMap<>();

        for (PantryItem item : pantry) {
            addAmount(
                    available,
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit());
        }

        List<Recipe> matches = new ArrayList<>();

        try (Cursor recipes = db.query(
                "recipes",
                new String[]{"_id", "name", "steps"},
                null, null, null, null,
                "name COLLATE NOCASE ASC")) {

            while (recipes.moveToNext()) {
                long recipeId = recipes.getLong(
                        recipes.getColumnIndexOrThrow("_id"));

                Map<String, BigDecimal> required = new HashMap<>();

                try (Cursor ingredients = db.query(
                        "recipe_ingredients",
                        new String[]{"name", "quantity", "unit"},
                        "recipe_id = ?",
                        new String[]{String.valueOf(recipeId)},
                        null, null, null)) {

                    while (ingredients.moveToNext()) {
                        addAmount(
                                required,
                                ingredients.getString(
                                        ingredients.getColumnIndexOrThrow("name")),
                                ingredients.getDouble(
                                        ingredients.getColumnIndexOrThrow("quantity")),
                                ingredients.getString(
                                        ingredients.getColumnIndexOrThrow("unit")));
                    }
                }

                // A recipe with no requirements must not match everything.
                boolean canMake = !required.isEmpty();

                for (Map.Entry<String, BigDecimal> entry
                        : required.entrySet()) {

                    BigDecimal owned = available.get(entry.getKey());

                    // One missing ingredient or short quantity rules out the whole recipe.
                    if (owned == null
                            || owned.compareTo(entry.getValue()) < 0) {
                        canMake = false;
                        break;
                    }
                }

                if (canMake) {
                    matches.add(new Recipe(
                            recipeId,
                            recipes.getString(
                                    recipes.getColumnIndexOrThrow("name")),
                            recipes.getString(
                                    recipes.getColumnIndexOrThrow("steps"))));
                }
            }
        }

        return matches;
    }
}