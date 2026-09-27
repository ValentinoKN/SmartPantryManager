package com.valentino.smartpantry;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.NumberFormat;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private TextView nameText;
    private TextView ingredientsText;
    private TextView stepsText;
    private View recipeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                });

        nameText = findViewById(R.id.text_recipe_name);
        ingredientsText = findViewById(R.id.text_recipe_ingredients);
        stepsText = findViewById(R.id.text_recipe_steps);
        recipeContent = findViewById(R.id.recipe_content);

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);

        if (recipeId == -1) {
            nameText.setText(R.string.recipe_detail_failed);
            return;
        }

        loadRecipe(recipeId);
    }

    private void loadRecipe(long recipeId) {
        new Thread(() -> {
            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {

                Recipe recipe = database.getRecipeById(recipeId);
                List<PantryItem> requirements =
                        database.getRecipeIngredients(recipeId);

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (recipe == null || requirements.isEmpty()) {
                        nameText.setText(R.string.recipe_detail_failed);
                        return;
                    }

                    NumberFormat format = NumberFormat.getNumberInstance();
                    format.setMaximumFractionDigits(6);

                    StringBuilder ingredientLines = new StringBuilder();

                    for (PantryItem requirement : requirements) {
                        if (ingredientLines.length() > 0) {
                            ingredientLines.append("\n");
                        }

                        ingredientLines.append(getString(
                                R.string.recipe_ingredient_line,
                                format.format(requirement.getQuantity()),
                                requirement.getUnit(),
                                requirement.getName()));
                    }

                    nameText.setText(recipe.getName());
                    ingredientsText.setText(ingredientLines.toString());
                    stepsText.setText(recipe.getSteps());
                    recipeContent.setVisibility(View.VISIBLE);
                });

            } catch (RuntimeException exception) {
                Log.e("RecipeDetail", "Could not load recipe", exception);

                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        nameText.setText(R.string.recipe_detail_failed);
                    }
                });
            }
        }).start();
    }
}