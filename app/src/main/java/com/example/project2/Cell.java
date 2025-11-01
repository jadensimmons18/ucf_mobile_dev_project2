package com.example.project2;

public class Cell {
    private boolean isMine; // declares if the cell is a mine or not
    private boolean isRevealed; // declares if the cell has been revealed or not
    private boolean isFlagged; // declares if the cell has been marked with a flag or not
    private int adjacentMines; // keeps track of how many mines are adjacent to the current cell

    // Constructor
    public Cell(){
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }
    // Getters
    public boolean isMine() {
        return isMine;
    }
    public boolean isRevealed(){
        return isRevealed;
    }
    public boolean isFlagged(){
        return this.isFlagged;
    }
    public int getAdjacentMines(){
        return adjacentMines;
    }
    //Setters
    public void setMine(boolean mine) {
        isMine = mine;
    }
    public void setAdjacentMines(int count) {
        adjacentMines = count;
    }
    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
    public void setFlagged(boolean flagged) {
        this.isFlagged = flagged;
    }
}
