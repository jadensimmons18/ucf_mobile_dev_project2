package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons from the XML
        Button btnStartGame = findViewById(R.id.btnStartGame);
        Button btnSettings = findViewById(R.id.btnSettings);

         //Set up click listeners
        btnStartGame.setOnClickListener(v -> {
            // Go directly to game with default settings
            Intent intent = new Intent(MainActivity.this, GameActivity.class);

            // Pass default settings
            intent.putExtra("rows", 5);
            intent.putExtra("columns", 5);
            intent.putExtra("minePercent", 10);

            // Default colors (you can change these)
            intent.putExtra("coveredColor", android.graphics.Color.GRAY);
            intent.putExtra("uncoveredColor", android.graphics.Color.WHITE);
            intent.putExtra("suspectedColor", android.graphics.Color.YELLOW);
            intent.putExtra("mineColor", android.graphics.Color.RED);

            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            // Go to settings screen
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}