package com.sudoku;

public class SudokuSolver {

    public boolean solve(int[][] grid) {
        if (!hasNoConflicts(grid)) {
            return false;
        }
        return solveFrom(grid, 0);
    }

    private boolean solveFrom(int[][] grid, int cell) {
        if (cell == 81) {
            return true;
        }

        int row = cell / 9;
        int column = cell % 9;

        if (grid[row][column] != 0) {
            return solveFrom(grid, cell + 1);
        }

        for (int value = 1; value <= 9; value++) {
            if (canPlace(grid, row, column, value)) {
                grid[row][column] = value;

                if (solveFrom(grid, cell + 1)) {
                    return true;
                }

                grid[row][column] = 0;
            }
        }

        return false;
    }

    private boolean canPlace(
            int[][] grid,
            int row,
            int column,
            int value
    ) {
        for (int index = 0; index < 9; index++) {
            if (grid[row][index] == value) {
                return false;
            }

            if (grid[index][column] == value) {
                return false;
            }
        }

        int startingRow = (row / 3) * 3;
        int startingColumn = (column / 3) * 3;

        for (
                int currentRow = startingRow;
                currentRow < startingRow + 3;
                currentRow++
        ) {
            for (
                    int currentColumn = startingColumn;
                    currentColumn < startingColumn + 3;
                    currentColumn++
            ) {
                if (grid[currentRow][currentColumn] == value) {
                    return false;
                }
            }
        }

        return true;
    }

    public int countSolutions(int[][] grid, int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException(
                    "Limit must be positive."
            );
        }

        if (!hasNoConflicts(grid)) {
            return 0;
        }

        return countFrom(grid, 0, limit);
    }

private int countFrom(
        int[][] grid,
        int cell,
        int limit
) {
    while (
            cell < 81
                    && grid[cell / 9][cell % 9] != 0
    ) {
        cell++;
    }

    if (cell == 81) {
        return 1;
    }

    int row = cell / 9;
    int column = cell % 9;
    int total = 0;

    for (int value = 1; value <= 9; value++) {
        if (canPlace(grid, row, column, value)) {
            grid[row][column] = value;

            total += countFrom(
                    grid,
                    cell + 1,
                    limit - total
            );

            grid[row][column] = 0;

            if (total >= limit) {
                return total;
            }
        }
    }

    return total;
}

private boolean hasNoConflicts(int[][] grid) {
    for (int row = 0; row < 9; row++) {
        for (int column = 0; column < 9; column++) {
            int value = grid[row][column];

            if (value == 0) {
                continue;
            }

            grid[row][column] = 0;

            boolean valid = canPlace(
                    grid,
                    row,
                    column,
                    value
            );

            grid[row][column] = value;

            if (!valid) {
                return false;
            }
        }
    }

    return true;
}

}