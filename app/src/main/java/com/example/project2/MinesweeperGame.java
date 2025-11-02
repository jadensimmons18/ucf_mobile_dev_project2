package com.example.project2;

import java.util.Random;

public class MinesweeperGame {
    // Game board properties
    private Cell[][] board;        // 2D array of all cells
    private int rows;
    private int columns;
    private int totalMines;
    private int flagsPlaced;
    private boolean gameOver;
    private boolean gameWon;

    // Constructor
    public MinesweeperGame(int rows, int columns, int minePercentage) {
        this.rows = rows;
        this.columns = columns;
        this.totalMines = calculateTotalMines(rows, columns, minePercentage);
        this.board = new Cell[rows][columns];
        this.flagsPlaced = 0;
        this.gameOver = false;
        this.gameWon = false;

        initializeBoard();
        placeMines();
        calculateAdjacentMines();
    }

    // Initialize empty board
    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = new Cell();
            }
        }
    }
    // Randomly place mines
    private void placeMines() {
        int minesPlaced = 0;
        Random random = new Random();
        while (minesPlaced < totalMines) {
            int row = random.nextInt(rows); //randInt between 0 - (rows -1)
            int col = random.nextInt(columns); //randInt between 0 - (cols -1)
            if (!board[row][col].isMine()) {
                board[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }
    // Calculate adjacent mines for all cells
    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!board[i][j].isMine()) {
                    int count = countAdjacentMines(i, j);
                    board[i][j].setAdjacentMines(count); // Sets the variable inside Cell class to track the number of adjacent mines
                }
            }
        }
    }
    // Count mines around a specific cell
    private int countAdjacentMines(int row, int col) {
        int count = 0;
        // Check all 8 surrounding cells
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                // Skip the cell itself
                if (i == 0 && j == 0) continue;

                // Check if in bounds
                if (isValidCell(newRow, newCol)) {
                    if (board[newRow][newCol].isMine()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    // Check if cell coordinates are within the board
    private boolean isValidCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < columns){
            return true;
        }
        return false;
    }
    // Reveals the cell that is clicked and if the cell has no adjacent mines then it reveals all adjacent cells too (Called by controller)
    public boolean revealCell(int row, int col) {
        if (gameOver || !isValidCell(row, col)) { // Can't reveal if game is over or invalid cell
            return false;
        }

        Cell cell = board[row][col];

        if (cell.isFlagged() || cell.isRevealed()) {// Can't reveal flagged or already revealed cells
            return false;
        }

        cell.setRevealed(true); // reveal this cell

        if (cell.isMine()) { // If a mine is hit Game over
            gameOver = true;
            return true; // Return true = hit mine
        }

        if (cell.getAdjacentMines() == 0) {// If cell has 0 adjacent mines, reveal surrounding cells
            revealAdjacentCells(row, col);
        }
        checkWinCondition(); // Check if player won
        return false; // Return false = safe cell
    }
    // Function that cascades and reveals all surrounding cells
    private void revealAdjacentCells(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (i == 0 && j == 0) continue; // We already know this ones not a mine
                if (isValidCell(newRow, newCol)) {
                    Cell adjacentCell = board[newRow][newCol];
                    if (!adjacentCell.isRevealed() && !adjacentCell.isFlagged()) { // if the cell is not already revealed and not flagged
                        revealCell(newRow, newCol); // reveal the cell
                    }
                }
            }
        }
    }
    // Toggles flag off/on
    public void toggleFlag(int row, int col) {
        if (gameOver || !isValidCell(row, col)) {
            return;
        }
        Cell cell = board[row][col];
        if (cell.isRevealed()) {
            return; // Can't flag revealed cells
        }
        if (cell.isFlagged()) { // if the cell is flagged unflag it
            cell.setFlagged(false);
            flagsPlaced--;
        }
        else { // If cell is not flagged flag it
            cell.setFlagged(true);
            flagsPlaced++;
        }
    }
    // Linearly checks all squares to check if the player has won yet
    private void checkWinCondition() {
        int revealedCount = 0;
        int totalNonMines = (rows * columns) - totalMines;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j].isRevealed() && !board[i][j].isMine()) {
                    revealedCount++;
                }
            }
        }
        if (revealedCount == totalNonMines) {
            gameWon = true;
            gameOver = true;
        }
    }
    // Calculate total mines based on the percentage set by user
    private int calculateTotalMines(int rows, int cols, int percentage) {
        int totalCells = rows * cols;
        return (int) Math.ceil(totalCells * percentage / 100.0);
    }
    // Getters
    public Cell getCell(int row, int col) {
        return board[row][col];
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public int getTotalMines() {
        return totalMines;
    }
    public int getRemainingMines() {
        return totalMines - flagsPlaced;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public boolean isGameWon() {
        return gameWon;
    }
}
