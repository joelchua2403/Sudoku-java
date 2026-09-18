package com.sudoku;

public record Violation(
        Type type,
        int value,
        int index
) {

    public enum Type {
        ROW,
        COLUMN,
        SUBGRID
    }

    public String message() {
        return switch (type) {
            case ROW ->
                    "Number " + value
                            + " already exists in Row "
                            + (char) ('A' + index)
                            + ".";

            case COLUMN ->
                    "Number " + value
                            + " already exists in Column "
                            + (index + 1)
                            + ".";

            case SUBGRID ->
                    "Number " + value
                            + " already exists in the same 3x3 subgrid.";
        };
    }
}