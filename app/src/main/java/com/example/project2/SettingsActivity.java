package com.example.project2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_ROWS = "rows";
    public static final String EXTRA_COLUMNS = "columns";
    public static final String EXTRA_MINE_PERCENT = "minePercent";
    public static final String EXTRA_COVERED_COLOR = "coveredColor";
    public static final String EXTRA_UNCOVERED_COLOR = "uncoveredColor";
    public static final String EXTRA_SUSPECTED_COLOR = "suspectedColor";
    public static final String EXTRA_MINE_COLOR = "mineColor";


    private Spinner spinnerRows;
    private Spinner spinnerColumns;
    private Spinner spinnerMinePercent;
    private Spinner spinnerCoveredColor;
    private Spinner spinnerUncoveredColor;
    private Spinner spinnerSuspectedColor;
    private Spinner spinnerMineColor;
    private Button btnSaveSettings;
    private Button btnBackToMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // find views
        spinnerRows = findViewById(R.id.spinnerRows);
        spinnerColumns = findViewById(R.id.spinnerColumns);
        spinnerMinePercent = findViewById(R.id.spinnerMinePercent);
        spinnerCoveredColor = findViewById(R.id.spinnerCoveredColor);
        spinnerUncoveredColor = findViewById(R.id.spinnerUncoveredColor);
        spinnerSuspectedColor = findViewById(R.id.spinnerSuspectedColor);
        spinnerMineColor = findViewById(R.id.spinnerMineColor);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Populate spinners
        setupBoardSizeSpinners();
        setupMinePercentSpinner();
        setupColorSpinners();

        // Save & Play
        btnSaveSettings.setOnClickListener(v -> {
            int rows = Integer.parseInt(spinnerRows.getSelectedItem().toString());
            int columns = Integer.parseInt(spinnerColumns.getSelectedItem().toString());
            int minePercent = Integer.parseInt(spinnerMinePercent.getSelectedItem().toString());

            // Simple validation
            if (rows < 2 || columns < 2) {
                Toast.makeText(this, "Board must be at least 2x2", Toast.LENGTH_SHORT).show();
                return;
            }
            if (minePercent < 1 || minePercent > 99) {
                Toast.makeText(this, "Mine percent must be between 1 and 99", Toast.LENGTH_SHORT).show();
                return;
            }

            // Map color names to actual color ints
            int coveredColor = colorNameToInt(spinnerCoveredColor.getSelectedItem().toString());
            int uncoveredColor = colorNameToInt(spinnerUncoveredColor.getSelectedItem().toString());
            int suspectedColor = colorNameToInt(spinnerSuspectedColor.getSelectedItem().toString());
            int mineColor = colorNameToInt(spinnerMineColor.getSelectedItem().toString());


            Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
            intent.putExtra(EXTRA_ROWS, rows);
            intent.putExtra(EXTRA_COLUMNS, columns);
            intent.putExtra(EXTRA_MINE_PERCENT, minePercent);
            intent.putExtra(EXTRA_COVERED_COLOR, coveredColor);
            intent.putExtra(EXTRA_UNCOVERED_COLOR, uncoveredColor);
            intent.putExtra(EXTRA_SUSPECTED_COLOR, suspectedColor);
            intent.putExtra(EXTRA_MINE_COLOR, mineColor);

            startActivity(intent);
        });

        // Back to menu: just finish this Activity and return
        btnBackToMenu.setOnClickListener(v -> finish());
    }

    // Sets up row/column spinner options (example: 5..15; you can adjust)
    private void setupBoardSizeSpinners() {
        List<String> sizeOptions = new ArrayList<>();
        // common board sizes: 5..15
        for (int i = 5; i <= 15; i++) {
            sizeOptions.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sizeOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRows.setAdapter(adapter);
        spinnerColumns.setAdapter(adapter);

        // set reasonable defaults (matching MainActivity earlier)
        spinnerRows.setSelection(sizeOptions.indexOf("5"));
        spinnerColumns.setSelection(sizeOptions.indexOf("5"));
    }

    // Sets up mine percent spinner (example: 5%, 10%, 15%, 20%, ...)
    private void setupMinePercentSpinner() {
        List<String> percents = new ArrayList<>();
        int[] common = {5, 10, 15, 20, 25, 30};
        for (int p : common) percents.add(String.valueOf(p));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, percents);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMinePercent.setAdapter(adapter);
        spinnerMinePercent.setSelection(percents.indexOf("10")); // default 10%
    }

    // Color spinner setup: use readable names and map them to Color ints in colorNameToInt()
    private void setupColorSpinners() {
        List<String> colorNames = new ArrayList<>();
        colorNames.add("GRAY");
        colorNames.add("WHITE");
        colorNames.add("YELLOW");
        colorNames.add("RED");
        colorNames.add("BLUE");
        colorNames.add("GREEN");
        colorNames.add("BLACK");
        colorNames.add("LIGHT_GRAY");
        // add or remove color names to fit your design

        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colorNames);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCoveredColor.setAdapter(colorAdapter);
        spinnerUncoveredColor.setAdapter(colorAdapter);
        spinnerSuspectedColor.setAdapter(colorAdapter);
        spinnerMineColor.setAdapter(colorAdapter);

        // set defaults to match MainActivity defaults
        spinnerCoveredColor.setSelection(colorNames.indexOf("GRAY"));
        spinnerUncoveredColor.setSelection(colorNames.indexOf("WHITE"));
        spinnerSuspectedColor.setSelection(colorNames.indexOf("YELLOW"));
        spinnerMineColor.setSelection(colorNames.indexOf("RED"));
    }

    // Map color name strings to Android color ints
    private int colorNameToInt(String name) {
        switch (name) {
            case "WHITE":
                return Color.WHITE;
            case "GRAY":
                return Color.GRAY;
            case "LIGHT_GRAY":
                return 0xFFD3D3D3; // light gray hex
            case "YELLOW":
                return Color.YELLOW;
            case "RED":
                return Color.RED;
            case "BLUE":
                return Color.BLUE;
            case "GREEN":
                return Color.GREEN;
            case "BLACK":
                return Color.BLACK;
            default:
                return Color.GRAY; // fallback
        }
    }
}
