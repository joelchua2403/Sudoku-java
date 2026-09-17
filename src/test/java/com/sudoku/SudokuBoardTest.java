package com.sudoku;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuBoardTest {

    @Test
    void returnsValuesFromInitialPuzzle() {
        int[][] puzzle = new int[9][9];
        puzzle[0][0] = 5;
        puzzle[1][2] = 7;

        SudokuBoard board = new SudokuBoard(puzzle);

        assertEquals(5, board.get(new Position(0, 0)));
        assertEquals(7, board.get(new Position(1, 2)));
        assertEquals(0, board.get(new Position(8, 8)));
    }

    @Test
void copiesTheInitialPuzzle() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);

    puzzle[0][0] = 9;

    assertEquals(5, board.get(new Position(0, 0)));
}

@Test
void rejectsPuzzleWithoutNineRows() {
    int[][] puzzle = new int[8][9];

    assertThrows(
            IllegalArgumentException.class,
            () -> new SudokuBoard(puzzle)
    );
}

@Test
void rejectsPuzzleRowsWithoutNineColumns() {
    int[][] puzzle = new int[9][8];

    assertThrows(
            IllegalArgumentException.class,
            () -> new SudokuBoard(puzzle)
    );
}
@ParameterizedTest
@ValueSource(ints = {-1, 10})
void rejectsCellValuesOutsideZeroToNine(int invalidValue) {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = invalidValue;

    assertThrows(
            IllegalArgumentException.class,
            () -> new SudokuBoard(puzzle)
    );
}

@Test
void identifiesPreFilledAndEditableCells() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);

    assertTrue(board.isFixed(new Position(0, 0)));
    assertFalse(board.isFixed(new Position(0, 1)));
}

@Test
void placesNumberInEditableCell() {
    int[][] puzzle = new int[9][9];
    SudokuBoard board = new SudokuBoard(puzzle);

    Position position = new Position(1, 2);

    board.set(position, 7);

    assertEquals(7, board.get(position));
}

@Test
void rejectsChangesToPreFilledCell() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);
    Position position = new Position(0, 0);

    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> board.set(position, 7)
    );

    assertEquals(
            "A1 is pre-filled",
            exception.getMessage()
    );

    assertEquals(5, board.get(position));
}

@ParameterizedTest
@ValueSource(ints = {-1, 0, 10})
void rejectsInvalidValuesWhenSettingCell(int invalidValue) {
    SudokuBoard board = new SudokuBoard(new int[9][9]);
    Position position = new Position(0, 0);

    assertThrows(
            IllegalArgumentException.class,
            () -> board.set(position, invalidValue)
    );

    assertEquals(0, board.get(position));
}

@Test
void clearsEditableCell() {
    SudokuBoard board = new SudokuBoard(new int[9][9]);
    Position position = new Position(1, 2);

    board.set(position, 7);
    board.clear(position);

    assertEquals(0, board.get(position));
}

@Test
void rejectsClearingPreFilledCell() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);
    Position position = new Position(0, 0);

    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> board.clear(position)
    );

    assertEquals(
            "A1 is pre-filled",
            exception.getMessage()
    );

    assertEquals(5, board.get(position));
}

@Test
void displaysBoardWithHeadingsAndEmptyCells() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;
    puzzle[1][2] = 7;

    SudokuBoard board = new SudokuBoard(puzzle);

    String expected = String.join(
            "\n",
            "    1 2 3 4 5 6 7 8 9",
            "  A 5 _ _ _ _ _ _ _ _",
            "  B _ _ 7 _ _ _ _ _ _",
            "  C _ _ _ _ _ _ _ _ _",
            "  D _ _ _ _ _ _ _ _ _",
            "  E _ _ _ _ _ _ _ _ _",
            "  F _ _ _ _ _ _ _ _ _",
            "  G _ _ _ _ _ _ _ _ _",
            "  H _ _ _ _ _ _ _ _ _",
            "  I _ _ _ _ _ _ _ _ _"
    );

    assertEquals(expected, board.display());
}

@Test
void reportsIncompleteBoardAsNotFull() {
    int[][] puzzle = new int[9][9];

    SudokuBoard board = new SudokuBoard(puzzle);

    assertFalse(board.isFull());
}

@Test
void reportsCompletedBoardAsFull() {
    int[][] puzzle = new int[9][9];

    for (int row = 0; row < 9; row++) {
        for (int column = 0; column < 9; column++) {
            puzzle[row][column] = 1;
        }
    }

    SudokuBoard board = new SudokuBoard(puzzle);

    assertTrue(board.isFull());
}

@Test
void returnsIndependentSnapshot() {
    int[][] puzzle = new int[9][9];
    puzzle[0][0] = 5;

    SudokuBoard board = new SudokuBoard(puzzle);

    int[][] snapshot = board.snapshot();

    assertEquals(5, snapshot[0][0]);

    snapshot[0][0] = 9;

    assertEquals(5, board.get(new Position(0, 0)));
}

}