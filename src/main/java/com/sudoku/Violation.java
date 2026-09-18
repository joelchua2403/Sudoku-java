package com.sudoku;

public record Violation(Type type, int value, int index) {

    public enum Type {
        ROW,
        COLUMN,
        SUBGRID
    }
}