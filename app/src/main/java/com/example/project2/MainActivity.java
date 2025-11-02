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

        // Find the buttons
        Button btnStartGame = findViewById(R.id.btnStartGame);
        Button btnSettings = findViewById(R.id.btnSettings);

         //listeners
        btnStartGame.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, GameActivity.class);

            // Pass default settings
            intent.putExtra("rows", 5);
            intent.putExtra("columns", 5);
            intent.putExtra("minePercent", 10);
            intent.putExtra("coveredColor", android.graphics.Color.GRAY);
            intent.putExtra("uncoveredColor", android.graphics.Color.WHITE);
            intent.putExtra("suspectedColor", android.graphics.Color.YELLOW);
            intent.putExtra("mineColor", android.graphics.Color.RED);

            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}