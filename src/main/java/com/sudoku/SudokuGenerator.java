package com.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    private final SudokuSolver solver;
    private final Random random;
    private static final int REQUIRED_CLUES = 30;

    public SudokuGenerator(
            SudokuSolver solver,
            Random random
    ) {
        this.solver = solver;
        this.random = random;
    }

    public int[][] generateSolution() {
        int[][] grid = new int[9][9];

        fillDiagonalSubgrid(grid, 0);
        fillDiagonalSubgrid(grid, 3);
        fillDiagonalSubgrid(grid, 6);

        boolean solved = solver.solve(grid);

        if (!solved) {
            throw new IllegalStateException(
                    "Unable to generate Sudoku solution"
            );
        }

        return grid;
    }

    private void fillDiagonalSubgrid(
            int[][] grid,
            int startingIndex
    ) {
        List<Integer> values = new ArrayList<>();

        for (int value = 1; value <= 9; value++) {
            values.add(value);
        }

        Collections.shuffle(values, random);

        int index = 0;

        for (
                int row = startingIndex;
                row < startingIndex + 3;
                row++
        ) {
            for (
                    int column = startingIndex;
                    column < startingIndex + 3;
                    column++
            ) {
                grid[row][column] = values.get(index);
                index++;
            }
        }
    }

  public Puzzle generate() {
    for (int attempt = 0; attempt < 20; attempt++) {
        int[][] solution = generateSolution();
        int[][] clues = copy(solution);

        List<Integer> positions = new ArrayList<>();

        for (int position = 0; position < 81; position++) {
            positions.add(position);
        }

        Collections.shuffle(positions, random);

        int clueCount = 81;

        for (int position : positions) {
            if (clueCount == REQUIRED_CLUES) {
                break;
            }

            int row = position / 9;
            int column = position % 9;
            int originalValue = clues[row][column];

            clues[row][column] = 0;

            int solutionCount = solver.countSolutions(
                    copy(clues),
                    2
            );

            if (solutionCount == 1) {
                clueCount--;
            } else {
                clues[row][column] = originalValue;
            }
        }

        if (clueCount == REQUIRED_CLUES) {
            return new Puzzle(clues, solution);
        }
    }

    throw new IllegalStateException(
            "Unable to generate unique puzzle"
    );
}

private int[][] copy(int[][] source) {
    int[][] result = new int[9][9];

    for (int row = 0; row < 9; row++) {
        for (int column = 0; column < 9; column++) {
            result[row][column] = source[row][column];
        }
    }

    return result;
}
}