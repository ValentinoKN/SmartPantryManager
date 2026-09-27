package com.valentino.smartpantry;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.database.sqlite.SQLiteException;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IngredientActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText quantityInput;
    private Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredient);

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

        nameInput = findViewById(R.id.input_ingredient_name);
        quantityInput = findViewById(R.id.input_ingredient_quantity);
        unitSpinner = findViewById(R.id.spinner_ingredient_unit);

        findViewById(R.id.button_save_ingredient)
                .setOnClickListener(view -> validateIngredient());
    }

    private void validateIngredient() {
        nameInput.setError(null);
        quantityInput.setError(null);

        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError(getString(R.string.error_ingredient_name));
            nameInput.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            quantityInput.setError(getString(R.string.error_quantity_required));
            quantityInput.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException exception) {
            showQuantityError();
            return;
        }

        // Reject non-finite values as well as zero and negative quantities.
        if (Double.isNaN(quantity)
                || Double.isInfinite(quantity)
                || quantity <= 0) {
            showQuantityError();
            return;
        }

        Object selectedUnit = unitSpinner.getSelectedItem();
        String unit = selectedUnit == null ? "" : selectedUnit.toString();

        if (!isSupportedUnit(unit)) {
            Toast.makeText(
                    this,
                    R.string.error_unit_invalid,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        saveIngredient(name, quantity, unit);
    }

    private void showQuantityError() {
        quantityInput.setError(getString(R.string.error_quantity_invalid));
        quantityInput.requestFocus();
    }

    private boolean isSupportedUnit(String unit) {
        return unit.equals("g")
                || unit.equals("kg")
                || unit.equals("ml")
                || unit.equals("l")
                || unit.equals("each");
    }

    private void saveIngredient(String name, double quantity, String unit) {
        Button saveButton = findViewById(R.id.button_save_ingredient);

        // Prevent repeated taps from inserting the same form twice.
        saveButton.setEnabled(false);

        // Database work runs separately so the screen stays responsive.
        new Thread(() -> {
            long insertedId = -1;

            try (PantryDatabaseHelper database =
                         new PantryDatabaseHelper(getApplicationContext())) {
                insertedId = database.insertIngredient(name, quantity, unit);
            } catch (SQLiteException exception) {
                android.util.Log.e(
                        "IngredientActivity",
                        "Could not insert pantry ingredient",
                        exception);
            }

            final boolean saved = insertedId != -1;

            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                saveButton.setEnabled(true);

                if (saved) {
                    Toast.makeText(
                            IngredientActivity.this,
                            R.string.ingredient_saved,
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(
                            IngredientActivity.this,
                            R.string.ingredient_save_failed,
                            Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }
}