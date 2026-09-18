package com.sudoku;

public record Puzzle(
    int[][] clues,
    int[][] solution
) {
    
}