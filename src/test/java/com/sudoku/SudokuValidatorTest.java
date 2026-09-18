package com.sudoku;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}