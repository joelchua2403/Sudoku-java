package com.sudoku;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SudokuGeneratorTest {

    private final SudokuSolver solver = new SudokuSolver();

    @Test
    void generatesCompleteValidSolution() {
        SudokuGenerator generator = new SudokuGenerator(
                solver,
                new Random(42)
        );

        int[][] solution = generator.generateSolution();

        SudokuBoard board = new SudokuBoard(solution);
        SudokuValidator validator = new SudokuValidator();

        assertTrue(board.isFull());
        assertTrue(validator.isSolved(board));
    }

    @Test
    void generatesUniquePuzzleWithThirtyClues() {
        SudokuGenerator generator = new SudokuGenerator(
                solver,
                new Random(42)
        );
        Puzzle puzzle = generator.generate();

        assertEquals(30, countClues(puzzle.clues()));
        assertEquals(
            1,
            solver.countSolutions(puzzle.clues(), 2)
        );

        assertCluesMatchSolution(puzzle.clues(), puzzle.solution());
    }

    private int countClues(int[][] grid) {
        int count = 0;

        for (int[] row : grid) {
            for (int value : row) {
                if (value != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private void assertCluesMatchSolution(
        int[][] clues,
        int[][] solution

    ) {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (clues[row][column] != 0) {
                    assertEquals(
                        clues[row][column],
                        solution[row][column]
                    );
                }
            }
        }
    }

}