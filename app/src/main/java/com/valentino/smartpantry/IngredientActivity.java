package com.valentino.smartpantry;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

        Toast.makeText(
                this,
                R.string.ingredient_validation_passed,
                Toast.LENGTH_LONG).show();
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
}