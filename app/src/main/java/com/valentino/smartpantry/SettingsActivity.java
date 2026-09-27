package com.valentino.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFERENCES_NAME = "pantry_settings";
    public static final String KEY_NEWEST_FIRST = "newest_first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                });

        SharedPreferences preferences = getSharedPreferences(
                PREFERENCES_NAME, MODE_PRIVATE);

        SwitchCompat newestFirstSwitch =
                findViewById(R.id.switch_newest_first);

        newestFirstSwitch.setChecked(
                preferences.getBoolean(KEY_NEWEST_FIRST, false));

        newestFirstSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    preferences.edit()
                            .putBoolean(KEY_NEWEST_FIRST, isChecked)
                            .apply();
                });
    }
}