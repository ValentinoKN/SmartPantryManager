package com.valentino.smartpantry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class RecipeSeeder {

    // Called from database creation or migration, inside the helper transaction.
    public static void seed(SQLiteDatabase db) {
        long id;

        id = addRecipe(db, "Banana peanut butter oats",
                "1. Gently cook the oats and milk in a saucepan, stirring "
                        + "until the oats soften.\n"
                        + "2. Stir in the peanut butter.\n"
                        + "3. Top with the peeled, sliced banana.");
        addIngredient(db, id, "oats", 60, "g");
        addIngredient(db, id, "milk", 200, "ml");
        addIngredient(db, id, "banana", 1, "each");
        addIngredient(db, id, "peanut butter", 15, "g");

        id = addRecipe(db, "Banana yoghurt oat bowl",
                "1. Put the yoghurt in a bowl.\n"
                        + "2. Stir in the oats.\n"
                        + "3. Add the peeled, sliced banana and serve.");
        addIngredient(db, id, "plain yoghurt", 200, "g");
        addIngredient(db, id, "oats", 40, "g");
        addIngredient(db, id, "banana", 1, "each");

        id = addRecipe(db, "Peanut butter banana toast",
                "1. Toast the bread in a toaster.\n"
                        + "2. Spread with peanut butter.\n"
                        + "3. Top with the peeled, sliced banana.");
        addIngredient(db, id, "bread", 2, "each");
        addIngredient(db, id, "peanut butter", 20, "g");
        addIngredient(db, id, "banana", 1, "each");

        id = addRecipe(db, "Scrambled eggs on toast",
                "1. Toast the bread.\n"
                        + "2. Beat the eggs in a bowl.\n"
                        + "3. Melt the butter in a frying pan over gentle heat.\n"
                        + "4. Add the eggs and stir until fully set.\n"
                        + "5. Serve on the toast.");
        addIngredient(db, id, "egg", 2, "each");
        addIngredient(db, id, "bread", 2, "each");
        addIngredient(db, id, "butter", 10, "g");

        id = addRecipe(db, "Spinach and cheese omelette",
                "1. Beat the eggs and grate the cheese.\n"
                        + "2. Heat the oil in a frying pan and wilt the spinach.\n"
                        + "3. Pour in the eggs and add the cheese.\n"
                        + "4. Cook gently until the eggs are fully set, "
                        + "then fold and serve.");
        addIngredient(db, id, "egg", 2, "each");
        addIngredient(db, id, "spinach", 50, "g");
        addIngredient(db, id, "cheese", 30, "g");
        addIngredient(db, id, "oil", 5, "ml");

        id = addRecipe(db, "Tomato scrambled eggs",
                "1. Chop the tomato and beat the eggs.\n"
                        + "2. Heat the oil in a frying pan.\n"
                        + "3. Cook the tomato until softened.\n"
                        + "4. Add the eggs and stir until fully set.");
        addIngredient(db, id, "egg", 2, "each");
        addIngredient(db, id, "tomato", 1, "each");
        addIngredient(db, id, "oil", 5, "ml");

        id = addRecipe(db, "Tuna yoghurt sandwich",
                "1. Use drained, ready-to-eat tinned tuna.\n"
                        + "2. Mix the tuna with the yoghurt.\n"
                        + "3. Slice the cucumber.\n"
                        + "4. Fill the bread with the tuna mixture and cucumber.");
        addIngredient(db, id, "tuna", 100, "g");
        addIngredient(db, id, "plain yoghurt", 30, "g");
        addIngredient(db, id, "cucumber", 50, "g");
        addIngredient(db, id, "bread", 2, "each");

        id = addRecipe(db, "Tuna sweetcorn salad",
                "1. Use drained, ready-to-eat tinned tuna and sweetcorn.\n"
                        + "2. Chop the cucumber and tomato.\n"
                        + "3. Combine everything in a bowl and serve.");
        addIngredient(db, id, "tuna", 100, "g");
        addIngredient(db, id, "sweetcorn", 80, "g");
        addIngredient(db, id, "cucumber", 100, "g");
        addIngredient(db, id, "tomato", 1, "each");

        id = addRecipe(db, "Chickpea tomato salad",
                "1. Use drained, ready-to-eat tinned chickpeas.\n"
                        + "2. Chop the tomato and cucumber.\n"
                        + "3. Combine with the chickpeas.\n"
                        + "4. Add the oil and lemon juice, then mix.");
        addIngredient(db, id, "chickpeas", 150, "g");
        addIngredient(db, id, "tomato", 1, "each");
        addIngredient(db, id, "cucumber", 100, "g");
        addIngredient(db, id, "oil", 5, "ml");
        addIngredient(db, id, "lemon juice", 10, "ml");

        id = addRecipe(db, "Bean and sweetcorn bowl",
                "1. Use drained, ready-to-eat tinned kidney beans "
                        + "and sweetcorn.\n"
                        + "2. Chop the tomato.\n"
                        + "3. Mix the beans, sweetcorn and tomato.\n"
                        + "4. Stir in the lemon juice.");
        addIngredient(db, id, "kidney beans", 150, "g");
        addIngredient(db, id, "sweetcorn", 80, "g");
        addIngredient(db, id, "tomato", 1, "each");
        addIngredient(db, id, "lemon juice", 10, "ml");

        id = addRecipe(db, "Cheese and tomato toast",
                "1. Toast the bread and slice the tomato.\n"
                        + "2. Grate the cheese and place it on the toast.\n"
                        + "3. Add the tomato and grill until the cheese melts.");
        addIngredient(db, id, "bread", 2, "each");
        addIngredient(db, id, "cheese", 40, "g");
        addIngredient(db, id, "tomato", 1, "each");

        id = addRecipe(db, "Peanut butter apple sandwich",
                "1. Core and thinly slice the apple.\n"
                        + "2. Spread the peanut butter on the bread.\n"
                        + "3. Add the apple slices and close the sandwich.");
        addIngredient(db, id, "bread", 2, "each");
        addIngredient(db, id, "peanut butter", 20, "g");
        addIngredient(db, id, "apple", 1, "each");

        id = addRecipe(db, "Apple yoghurt bowl",
                "1. Core and chop the apple.\n"
                        + "2. Put the yoghurt in a bowl.\n"
                        + "3. Add the apple and oats, then stir.");
        addIngredient(db, id, "plain yoghurt", 200, "g");
        addIngredient(db, id, "apple", 1, "each");
        addIngredient(db, id, "oats", 30, "g");

        id = addRecipe(db, "Banana oat pancakes",
                "1. Mash the peeled banana and beat in the eggs.\n"
                        + "2. Stir in the oats.\n"
                        + "3. Heat the oil in a non-stick frying pan.\n"
                        + "4. Spoon in small pancakes and cook on both sides "
                        + "until the centres are fully set.");
        addIngredient(db, id, "banana", 1, "each");
        addIngredient(db, id, "egg", 2, "each");
        addIngredient(db, id, "oats", 40, "g");
        addIngredient(db, id, "oil", 5, "ml");

        id = addRecipe(db, "Milk porridge",
                "1. Combine the oats and milk in a saucepan.\n"
                        + "2. Cook gently, stirring regularly, "
                        + "until the oats soften and the porridge thickens.");
        addIngredient(db, id, "oats", 60, "g");
        addIngredient(db, id, "milk", 250, "ml");

        id = addRecipe(db, "Peanut butter porridge",
                "1. Combine the oats and measured water in a saucepan.\n"
                        + "2. Cook gently, stirring, until the oats soften.\n"
                        + "3. Stir in the peanut butter and serve.");
        addIngredient(db, id, "oats", 60, "g");
        addIngredient(db, id, "water", 250, "ml");
        addIngredient(db, id, "peanut butter", 20, "g");

        id = addRecipe(db, "Tuna rice bowl",
                "1. Put the dry white rice and measured water in "
                        + "a small saucepan.\n"
                        + "2. Bring to a simmer, cover and cook gently "
                        + "until the rice is tender and the water is absorbed.\n"
                        + "3. Stir in drained, ready-to-eat tinned tuna "
                        + "and sweetcorn.\n"
                        + "4. Heat through and serve immediately.");
        addIngredient(db, id, "rice", 75, "g");
        addIngredient(db, id, "water", 200, "ml");
        addIngredient(db, id, "tuna", 100, "g");
        addIngredient(db, id, "sweetcorn", 80, "g");

        id = addRecipe(db, "Spinach chickpea rice",
                "1. Put the dry white rice and measured water in "
                        + "a small saucepan.\n"
                        + "2. Bring to a simmer, cover and cook gently "
                        + "until the rice is tender and the water is absorbed.\n"
                        + "3. Heat the oil in a frying pan and wilt the spinach.\n"
                        + "4. Add drained, ready-to-eat tinned chickpeas "
                        + "and heat through.\n"
                        + "5. Mix with the rice and serve immediately.");
        addIngredient(db, id, "rice", 75, "g");
        addIngredient(db, id, "water", 200, "ml");
        addIngredient(db, id, "spinach", 50, "g");
        addIngredient(db, id, "chickpeas", 150, "g");
        addIngredient(db, id, "oil", 5, "ml");

        id = addRecipe(db, "Cottage cheese cucumber toast",
                "1. Toast the bread.\n"
                        + "2. Spread the cottage cheese on the toast.\n"
                        + "3. Slice the cucumber and arrange it on top.");
        addIngredient(db, id, "bread", 2, "each");
        addIngredient(db, id, "cottage cheese", 100, "g");
        addIngredient(db, id, "cucumber", 80, "g");

        id = addRecipe(db, "Tuna jacket potato",
                "1. Prick the potato with a fork and place on "
                        + "a microwave-safe plate.\n"
                        + "2. Microwave, turning partway through, "
                        + "until tender throughout. Let it stand briefly.\n"
                        + "3. Mix drained, ready-to-eat tinned tuna "
                        + "with the yoghurt.\n"
                        + "4. Carefully split the potato and add the filling.");
        addIngredient(db, id, "potato", 1, "each");
        addIngredient(db, id, "tuna", 100, "g");
        addIngredient(db, id, "plain yoghurt", 40, "g");
    }

    private static long addRecipe(
            SQLiteDatabase db, String name, String steps) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("steps", steps);

        return db.insertOrThrow("recipes", null, values);
    }

    private static void addIngredient(
            SQLiteDatabase db,
            long recipeId,
            String name,
            double quantity,
            String unit) {

        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        db.insertOrThrow("recipe_ingredients", null, values);
    }
}