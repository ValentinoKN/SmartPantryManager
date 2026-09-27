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

public class MainActivity extends AppCompatActivity {

    private PantryAdapter pantryAdapter;
    private TextView emptyPantryText;
    private int loadVersion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        new Thread(() -> {
            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {

                List<PantryItem> ingredients = database.getAllIngredients();

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
}