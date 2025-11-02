package com.example.project2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    // Game settings from Intent
    private int rows;
    private int columns;
    private int minePercent;
    private int coveredColor;
    private int uncoveredColor;
    private int suspectedColor;
    private int mineColor;

    private MinesweeperGame game;
    private Button[][] cellButtons;

    // UI
    private GridLayout gridGameBoard;
    private TextView txtMineCounter;
    private TextView txtTimer;
    private Button btnReset;
    private Button btnBackToMenu;

    // Timer
    private Handler timerHandler;
    private Runnable timerRunnable;
    private int secondsElapsed = 0;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get settings from Intent
        rows = getIntent().getIntExtra(SettingsActivity.EXTRA_ROWS, 5);
        columns = getIntent().getIntExtra(SettingsActivity.EXTRA_COLUMNS, 5);
        minePercent = getIntent().getIntExtra(SettingsActivity.EXTRA_MINE_PERCENT, 10);
        coveredColor = getIntent().getIntExtra(SettingsActivity.EXTRA_COVERED_COLOR, Color.GRAY);
        uncoveredColor = getIntent().getIntExtra(SettingsActivity.EXTRA_UNCOVERED_COLOR, Color.WHITE);
        suspectedColor = getIntent().getIntExtra(SettingsActivity.EXTRA_SUSPECTED_COLOR, Color.YELLOW);
        mineColor = getIntent().getIntExtra(SettingsActivity.EXTRA_MINE_COLOR, Color.RED);

        // Find views
        gridGameBoard = findViewById(R.id.gridGameBoard);
        txtMineCounter = findViewById(R.id.txtMineCounter);
        txtTimer = findViewById(R.id.txtTimer);
        btnReset = findViewById(R.id.btnReset);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // listeners
        btnReset.setOnClickListener(v -> resetGame());
        btnBackToMenu.setOnClickListener(v -> finish());

        // Initialize the game
        initializeGame();

        // Set up timer
        setupTimer();
    }

    private void initializeGame() {
        game = new MinesweeperGame(rows, columns, minePercent);

        updateMineCounter();

        // Reset timer
        secondsElapsed = 0;
        timerRunning = false;
        updateTimerDisplay();

        // Create the game board
        createGameBoard();
    }

    private void createGameBoard() {
        // Clear any existing views
        gridGameBoard.removeAllViews();

        // create the grid
        gridGameBoard.setRowCount(rows);
        gridGameBoard.setColumnCount(columns);

        // Calculate cell size based on screen width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int padding = 32;
        int cellSize = (screenWidth - padding) / columns;

        // Make sure cells aren't too small or too large
        if (cellSize > 150) cellSize = 150;
        if (cellSize < 60) cellSize = 60;

        // Create array to store buttons
        cellButtons = new Button[rows][columns];

        // Create a button for each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int row = i;
                final int col = j;

                // Create button
                Button cellButton = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cellSize;
                params.height = cellSize;
                params.setMargins(2, 2, 2, 2);
                cellButton.setLayoutParams(params);

                // Set initial appearance
                cellButton.setBackgroundColor(coveredColor);
                cellButton.setText("");
                cellButton.setTextSize(16);
                cellButton.setTextColor(Color.BLACK);
                cellButton.setGravity(Gravity.CENTER);
                cellButton.setPadding(0, 0, 0, 0);

                // reveal cell
                cellButton.setOnClickListener(v -> {
                    // Start timer on first click
                    if (!timerRunning && !game.isGameOver()) {
                        startTimer();
                    }
                    onCellClick(row, col);
                });

                // flag cell
                cellButton.setOnLongClickListener(v -> {
                    // Start timer on first click
                    if (!timerRunning && !game.isGameOver()) {
                        startTimer();
                    }
                    onCellLongClick(row, col);
                    return true;
                });

                // Add button to grid
                gridGameBoard.addView(cellButton);

                cellButtons[row][col] = cellButton;
            }
        }
    }

    private void onCellClick(int row, int col) {
        // Don't do anything if game is over
        if (game.isGameOver()) {
            return;
        }

        // Try to reveal the cell
        boolean hitMine = game.revealCell(row, col);

        // Update all cells
        updateAllCells();

        // Check if mine was hit
        if (hitMine) {
            stopTimer();
            revealAllMines();
            Toast.makeText(this, "Game Over! You hit a mine!", Toast.LENGTH_LONG).show();
        }
        // Check if won
        else if (game.isGameWon()) {
            stopTimer();
            Toast.makeText(this, "Congratulations! You won in " + secondsElapsed + " seconds!", Toast.LENGTH_LONG).show();
        }
    }

    private void onCellLongClick(int row, int col) {
        // Don't do anything if game is over
        if (game.isGameOver()) {
            return;
        }

        // Toggle flag
        game.toggleFlag(row, col);

        // Update cell
        updateCellView(row, col);

        // Update mine counter
        updateMineCounter();
    }

    private void updateCellView(int row, int col) {
        Button button = cellButtons[row][col];
        Cell cell = game.getCell(row, col);

        if (cell.isFlagged()) {
            // Show flag
            button.setText("🚩");
            button.setTextSize(20);
            button.setBackgroundColor(suspectedColor);
        } else if (cell.isRevealed()) {
            // Revealed cell
            if (cell.isMine()) {
                // Show mine
                button.setText("💣");
                button.setTextSize(20);
                button.setBackgroundColor(mineColor);
            } else {
                // Show number or empty
                int adjacentMines = cell.getAdjacentMines();
                if (adjacentMines > 0) {
                    button.setText(String.valueOf(adjacentMines));
                    button.setTextSize(18);
                    button.setTextColor(getNumberColor(adjacentMines));
                } else {
                    button.setText("");
                }
                button.setBackgroundColor(uncoveredColor);
            }
            button.setEnabled(false); // Disable revealed cells
        } else {
            // Covered cell
            button.setText("");
            button.setBackgroundColor(coveredColor);
            button.setEnabled(true);
        }
    }

    private void updateAllCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                updateCellView(i, j);
            }
        }
    }

    private void revealAllMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = game.getCell(i, j);
                if (cell.isMine()) {
                    Button button = cellButtons[i][j];
                    button.setText("💣");
                    button.setTextSize(20);
                    button.setBackgroundColor(mineColor);
                    button.setEnabled(false);
                }
            }
        }
    }

    private int getNumberColor(int number) {
        // Classic Minesweeper number colors
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return Color.rgb(0, 128, 0); // Dark green
            case 3: return Color.RED;
            case 4: return Color.rgb(0, 0, 128); // Dark blue
            case 5: return Color.rgb(128, 0, 0); // Dark red
            case 6: return Color.CYAN;
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }

    private void updateMineCounter() {
        txtMineCounter.setText(String.valueOf(game.getRemainingMines()));
    }

    private void resetGame() {
        // Stop timer
        stopTimer();

        // reset everything
        initializeGame();

        Toast.makeText(this, "Game reset!", Toast.LENGTH_SHORT).show();
    }

    private void setupTimer() {
        timerHandler = new Handler(); //todo fix
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (timerRunning) {
                    secondsElapsed++;
                    updateTimerDisplay();
                    timerHandler.postDelayed(this, 1000); // Run again in 1 second
                }
            }
        };
    }

    private void startTimer() {
        if (!timerRunning) {
            timerRunning = true;
            timerHandler.post(timerRunnable);
        }
    }

    private void stopTimer() {
        timerRunning = false;
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    private void updateTimerDisplay() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String timeString = String.format("%d:%02d", minutes, seconds);
        txtTimer.setText(timeString);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause timer when activity is paused
        if (timerRunning) {
            stopTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up timer
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }
}
