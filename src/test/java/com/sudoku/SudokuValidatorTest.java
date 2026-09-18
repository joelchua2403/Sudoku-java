package com.sudoku;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuValidatorTest {

    private final SudokuValidator validator = new SudokuValidator();

    @Test
    void detectsDuplicateNumberInRow() {
        int[][] puzzle = new int[9][9];
        puzzle[0][0] = 3;
        puzzle[0][4] = 3;

        SudokuBoard board = new SudokuBoard(puzzle);

        List<Violation> violations = validator.validate(board);

        assertEquals(
                List.of(
                        new Violation(
                                Violation.Type.ROW,
                                3,
                                0
                        )
                ),
                violations
        );
    }

    @Test
void detectsDuplicateNumberInColumn() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;
    puzzle[3][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);

    List<Violation> violations = validator.validate(board);

    assertEquals(
            List.of(
                    new Violation(
                            Violation.Type.COLUMN,
                            5,
                            0
                    )
            ),
            violations
    );
}

@Test
void detectsDuplicateNumberInSubgrid() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 8;
    puzzle[1][1] = 8;

    SudokuBoard board = new SudokuBoard(puzzle);

    List<Violation> violations = validator.validate(board);

    assertEquals(
            List.of(
                    new Violation(
                            Violation.Type.SUBGRID,
                            8,
                            0
                    )
            ),
            violations
    );
}

@Test
void recognizesCompletedValidBoardAsSolved() {
    int[][] solution = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };

    SudokuBoard board = new SudokuBoard(solution);

    assertTrue(validator.isSolved(board));
}

@Test
void doesNotRecognizeIncompleteBoardAsSolved() {
    SudokuBoard board = new SudokuBoard(new int[9][9]);

    assertFalse(validator.isSolved(board));
}

}