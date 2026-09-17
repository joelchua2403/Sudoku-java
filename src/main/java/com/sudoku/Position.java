package com.sudoku;

public record Position(int row, int column) {

    public Position {
       if (row < 0 || row >= 9) {
            throw new IllegalArgumentException("Row cannot be negative or greater than 8");
        }
        if (column < 0 || column >= 9) {
            throw new IllegalArgumentException("Column cannot be negative or greater than 8");
        }
    }

public static Position parse(String text) {
    if (text == null || !text.matches("(?i)[A-I][1-9]")) {
        throw new IllegalArgumentException(
                "Cell must be A1 through I9"
        );
    }

    char rowCharacter = Character.toUpperCase(text.charAt(0));
    char columnCharacter = text.charAt(1);

    int row = rowCharacter - 'A';
    int column = columnCharacter - '1';

    return new Position(row, column);
}

@Override
public String toString() {
    char rowCharacter = (char) ('A' + row);
    int columnNumber = column + 1;

    return String.valueOf(rowCharacter) + columnNumber;
}

}