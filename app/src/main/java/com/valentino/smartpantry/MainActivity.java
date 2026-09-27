package com.valentino.smartpantry;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private PantryAdapter pantryAdapter;
    private TextView emptyPantryText;
    private int loadVersion = 0;
    private boolean deleteInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);
                    return insets;
                });

        ListView pantryList = findViewById(R.id.list_pantry);
        emptyPantryText = findViewById(R.id.text_empty_pantry);

        pantryAdapter = new PantryAdapter(this, new ArrayList<>());
        pantryList.setAdapter(pantryAdapter);
        pantryList.setEmptyView(emptyPantryText);

        pantryList.setOnItemClickListener((parent, view, position, id) -> {
            PantryItem ingredient = pantryAdapter.getItem(position);


            if (ingredient != null) {
                Intent intent = new Intent(
                        MainActivity.this, IngredientActivity.class);

                intent.putExtra(
                        IngredientActivity.EXTRA_INGREDIENT_ID,
                        ingredient.getId());

                startActivity(intent);
            }
        });

        pantryList.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    PantryItem ingredient = pantryAdapter.getItem(position);

                    if (ingredient != null && !deleteInProgress) {
                        confirmDelete(ingredient);
                    }

                    return true;
                });

        findViewById(R.id.button_add_ingredient)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(
                            MainActivity.this, IngredientActivity.class);
                    startActivity(intent);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Ignore results from a load started before leaving this screen.
        loadVersion++;
    }

    private void loadPantry() {
        final int requestVersion = ++loadVersion;

        emptyPantryText.setText(R.string.pantry_loading);
        pantryAdapter.clear();

        final boolean newestFirst = getSharedPreferences(
                SettingsActivity.PREFERENCES_NAME, MODE_PRIVATE)
                .getBoolean(SettingsActivity.KEY_NEWEST_FIRST, false);

        new Thread(() -> {
            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {

                List<PantryItem> ingredients = database.getAllIngredients();
                if (newestFirst) {
                    Collections.sort(
                            ingredients,
                            (first, second) -> Long.compare(
                                    second.getId(), first.getId()));
                }

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()
                            || requestVersion != loadVersion) {
                        return;
                    }

                    emptyPantryText.setText(R.string.empty_pantry);
                    pantryAdapter.addAll(ingredients);
                });

            } catch (SQLiteException exception) {
                Log.e("MainActivity", "Could not load pantry", exception);

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()
                            || requestVersion != loadVersion) {
                        return;
                    }

                    emptyPantryText.setText(R.string.pantry_load_failed);
                });
            }
        }).start();
    }

    private void confirmDelete(PantryItem ingredient) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_ingredient_title)
                .setMessage(getString(
                        R.string.delete_ingredient_message,
                        ingredient.getName()))
                .setNegativeButton(R.string.cancel_action, null)
                .setPositiveButton(
                        R.string.delete_action,
                        (dialog, which) -> deleteIngredient(ingredient.getId()))
                .show();
    }

    private void deleteIngredient(long ingredientId) {
        if (deleteInProgress) {
            return;
        }

        deleteInProgress = true;

        new Thread(() -> {
            boolean successful = false;

            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {
                successful = database.deleteIngredient(ingredientId) == 1;
            } catch (SQLiteException exception) {
                Log.e("MainActivity", "Could not delete ingredient", exception);
            }

            final boolean deleted = successful;

            runOnUiThread(() -> {
                deleteInProgress = false;

                if (isFinishing() || isDestroyed()) {
                    return;
                }

                Toast.makeText(
                        MainActivity.this,
                        deleted
                                ? R.string.ingredient_deleted
                                : R.string.ingredient_delete_failed,
                        Toast.LENGTH_SHORT).show();

                loadPantry();
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pantry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        if (selectedId == R.id.action_suggested_recipes) {
            Intent intent = new Intent(
                    MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
            return true;
        }

        if (selectedId == R.id.action_settings) {
            Intent intent = new Intent(
                    MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}