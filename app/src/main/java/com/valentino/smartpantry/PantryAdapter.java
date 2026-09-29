package com.valentino.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class PantryAdapter extends ArrayAdapter<PantryItem> {

    private final LayoutInflater inflater;
    private final NumberFormat quantityFormat;

    public PantryAdapter(Context context, List<PantryItem> ingredients) {
        super(context, R.layout.item_pantry, ingredients);

        inflater = LayoutInflater.from(context);
        quantityFormat = NumberFormat.getNumberInstance();
        quantityFormat.setMaximumFractionDigits(6);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // Reuse an existing row when ListView provides one.
        if (row == null) {
            row = inflater.inflate(R.layout.item_pantry, parent, false);
        }

        TextView nameText = row.findViewById(R.id.text_ingredient_name);
        TextView amountText = row.findViewById(R.id.text_ingredient_amount);

        // Fill both fields again because this row may previously have shown another item.
        PantryItem ingredient = getItem(position);

        if (ingredient != null) {
            nameText.setText(ingredient.getName());

            String quantity = quantityFormat.format(
                    ingredient.getQuantity());

            amountText.setText(getContext().getString(
                    R.string.pantry_item_amount,
                    quantity,
                    ingredient.getUnit()));
        } else {
            nameText.setText("");
            amountText.setText("");
        }

        return row;
    }
}