package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuValidator {

    public List<Violation> validate(SudokuBoard board) {
        int[][] grid = board.snapshot();
        List<Violation> violations = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            boolean[] reported = new boolean[10];

            for (int column = 0; column < 9; column++) {
                int value = grid[row][column];

                if (value != 0 && seen[value] && !reported[value]) {
                    violations.add(
                            new Violation(
                                    Violation.Type.ROW,
                                    value,
                                    row
                            )
                    );

                    reported[value] = true;
                }

                if (value != 0) {
                    seen[value] = true;
                }
            }
        }

        return violations;
    }
}