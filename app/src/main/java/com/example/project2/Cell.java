package com.example.project2;

public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell(){
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }
    public void setMine(boolean b) {
    }
    public void setAdjacentMines(int count) {
    }
    public void setRevealed(boolean b) {
    }
    public void setFlagged(boolean b) {
    }
}
