package com.sudoku;

public class SudokuBoard {

    private static final int SIZE = 9;

    private final int[][] cells;

    private final boolean [][] fixed;

    public SudokuBoard(int[][] puzzle) {
        requireValidPuzzle(puzzle);
        this.cells = copy(puzzle);
        this.fixed = new boolean[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                this.fixed[row][column] = this.cells[row][column] != 0;
            }
        }
    }

    public int get(Position position) {
        return cells[position.row()][position.column()];
    }

    public boolean isFixed(Position position) {
        return fixed[position.row()][position.column()];
    }

public void set(Position position, int value) {
    requireEditable(position);

    if (value < 1 || value > 9) {
        throw new IllegalArgumentException(
                "Number must be between 1 and 9"
        );
    }

    cells[position.row()][position.column()] = value;
}

public void clear(Position position) {
   requireEditable(position);
    cells[position.row()][position.column()] = 0;
}

public String display() {
    StringBuilder output = new StringBuilder();

    output.append("    1 2 3 4 5 6 7 8 9");
    output.append('\n');

    for (int row = 0; row < SIZE; row++) {
        output.append("  ");
        output.append((char) ('A' + row));

        for (int column = 0; column < SIZE; column++) {
            output.append(' ');

            int value = cells[row][column];

            if (value == 0) {
                output.append('_');
            } else {
                output.append(value);
            }
        }

        if (row < SIZE - 1) {
            output.append('\n');
        }
    }

    return output.toString();
}

public boolean isFull() {
    for (int row = 0; row < SIZE; row++) {
        for (int column = 0; column < SIZE; column++) {
            if (cells[row][column] == 0) {
                return false;
            }
        }
    }

    return true;
}

    private static int[][] copy(int[][] source) {
        int[][] result = new int[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                result[row][column] = source[row][column];
            }
        }

        return result;
    }

    public int[][] snapshot() {
    return copy(cells);
}

    private static void requireValidPuzzle(int[][] puzzle) {
    if (puzzle == null || puzzle.length != SIZE) {
        throw new IllegalArgumentException(
                "Board must contain exactly 9 rows"
        );
    }
    for (int[] row : puzzle) {
        if (row == null || row.length != SIZE) {
            throw new IllegalArgumentException(
                    "Each row must contain exactly 9 columns"
            );
        }

    for (int value : row) {
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException(
                    "Cell values must be from 0 to 9"
            );
        }
    }
    }
}

private void requireEditable(Position position) {
    if (isFixed(position)) {
        throw new IllegalStateException(
                position + " is pre-filled"
        );
    }
}


}