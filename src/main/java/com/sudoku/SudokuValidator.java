package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuValidator {

    public List<Violation> validate(SudokuBoard board) {
        int[][] grid = board.snapshot();
        List<Violation> violations = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            addDuplicates(
                    grid[row],
                    Violation.Type.ROW,
                    row,
                    violations
            );
        }

        for (int column = 0; column < 9; column++) {
            int[] values = new int[9];

            for (int row = 0; row < 9; row++) {
                values[row] = grid[row][column];
            }

            addDuplicates(
                    values,
                    Violation.Type.COLUMN,
                    column,
                    violations
            );
        }
        
        for (int subgrid = 0; subgrid < 9; subgrid++) {
    int[] values = new int[9];

    int startingRow = (subgrid / 3) * 3;
    int startingColumn = (subgrid % 3) * 3;

    int index = 0;

    for (int row = startingRow; row < startingRow + 3; row++) {
        for (
                int column = startingColumn;
                column < startingColumn + 3;
                column++
        ) {
            values[index] = grid[row][column];
            index++;
        }
    }

    addDuplicates(
            values,
            Violation.Type.SUBGRID,
            subgrid,
            violations
    );
}

        return violations;
    }

    private void addDuplicates(
            int[] values,
            Violation.Type type,
            int index,
            List<Violation> violations
    ) {
        boolean[] seen = new boolean[10];
        boolean[] reported = new boolean[10];

        for (int value : values) {
            if (value != 0 && seen[value] && !reported[value]) {
                violations.add(
                        new Violation(type, value, index)
                );

                reported[value] = true;
            }

            if (value != 0) {
                seen[value] = true;
            }
        }
    }

    public boolean isSolved(SudokuBoard board) {
    return board.isFull() && validate(board).isEmpty();
}
}