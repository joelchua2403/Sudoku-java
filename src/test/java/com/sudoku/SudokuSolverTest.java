package com.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuSolverTest {

    private final SudokuSolver solver = new SudokuSolver();

    @Test
    void solvesGridWithOneEmptyCell() {
        int[][] grid = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 0}
        };

        boolean solved = solver.solve(grid);

        assertTrue(solved);
        assertEquals(9, grid[8][8]);
    }

    @Test
void countsOneSolutionForUniquePuzzle() {
    int[][] puzzle = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    assertEquals(1, solver.countSolutions(puzzle, 2));
}

@Test
void stopsCountingAtSpecifiedLimit() {
    int[][] emptyGrid = new int[9][9];

    assertEquals(2, solver.countSolutions(emptyGrid, 2));
}

@Test
void doesNotSolveFullyFilledInvalidGrid() {
    int[][] invalidGrid = new int[9][9];

    for (int row = 0; row < 9; row++) {
        for (int column = 0; column < 9; column++) {
            invalidGrid[row][column] = 1;
        }
    }

    assertFalse(solver.solve(invalidGrid));
}

@Test
void countsZeroSolutionsForInvalidGrid() {
    int[][] invalidGrid = new int[9][9];
    invalidGrid[0][0] = 5;
    invalidGrid[0][1] = 5;

    assertEquals(
            0,
            solver.countSolutions(invalidGrid, 2)
    );
}

}