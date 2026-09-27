package com.valentino.smartpantry;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private ArrayAdapter<Recipe> adapter;
    private TextView emptyText;
    private int loadVersion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                });

        ListView list = findViewById(R.id.list_recipes);
        emptyText = findViewById(R.id.text_empty_recipes);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<Recipe>());

        list.setAdapter(adapter);
        list.setEmptyView(emptyText);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = adapter.getItem(position);

            if (recipe != null) {
                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        RecipeDetailActivity.class);

                intent.putExtra(
                        RecipeDetailActivity.EXTRA_RECIPE_ID,
                        recipe.getId());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadVersion++;
    }

    private void loadRecipes() {
        final int requestVersion = ++loadVersion;

        emptyText.setText(R.string.recipes_loading);
        adapter.clear();

        new Thread(() -> {
            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {

                List<Recipe> matches = database.getSuggestedRecipes();

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()
                            || requestVersion != loadVersion) {
                        return;
                    }

                    emptyText.setText(R.string.recipes_empty);
                    adapter.addAll(matches);
                });

            } catch (RuntimeException exception) {
                Log.e(
                        "SuggestedRecipes",
                        "Could not calculate suggestions",
                        exception);

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()
                            || requestVersion != loadVersion) {
                        return;
                    }

                    emptyText.setText(R.string.recipes_load_failed);
                });
            }
        }).start();
    }
}