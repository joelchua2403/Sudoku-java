package com.sudoku;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuGameTest {

    @Test
    void displaysWelcomeMessageAndPuzzle() {
        int[][] clues = new int[9][9];
        clues[0][0] = 5;

        Puzzle puzzle = new Puzzle(
                clues,
                new int[9][9]
        );

        SudokuGenerator generator = fixedGenerator(puzzle);

        StringWriter capturedOutput = new StringWriter();

        SudokuGame game = new SudokuGame(
                generator,
                new CommandParser(),
                new SudokuValidator(),
                new Scanner("quit\n"),
                new PrintWriter(capturedOutput, true),
                new Random(42)
        );

        game.run();

        String output = capturedOutput.toString();

        assertTrue(output.contains("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains(
                "  A 5 _ _ _ _ _ _ _ _"
        ));
    }

    private SudokuGenerator fixedGenerator(Puzzle puzzle) {
        return new SudokuGenerator(
                new SudokuSolver(),
                new Random(42)
        ) {
            @Override
            public Puzzle generate() {
                return puzzle;
            }
        };
    }

    @Test
void placesNumberAndDisplaysUpdatedBoard() {
    int[][] clues = new int[9][9];
    clues[0][0] = 5;

    Puzzle puzzle = new Puzzle(
            clues,
            new int[9][9]
    );

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("A2 7\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains("Move accepted."));
    assertTrue(output.contains(
            "  A 5 7 _ _ _ _ _ _ _"
    ));
}

@Test
void rejectsChangesToPreFilledCellAndContinues() {
    int[][] clues = new int[9][9];
    clues[0][0] = 5;

    Puzzle puzzle = new Puzzle(
            clues,
            new int[9][9]
    );

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("A1 7\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains(
            "Invalid move. A1 is pre-filled."
    ));

    assertTrue(output.contains(
            "  A 5 _ _ _ _ _ _ _ _"
    ));

    assertTrue(output.contains("Thanks for playing!"));
}

@Test
void clearsEditableCellAndDisplaysUpdatedBoard() {
    Puzzle puzzle = new Puzzle(
            new int[9][9],
            new int[9][9]
    );

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("A2 7\nA2 clear\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains("Move accepted."));
    assertTrue(output.contains("Cell cleared."));

    int clearedMessagePosition = output.indexOf(
            "Cell cleared."
    );

    String outputAfterClear = output.substring(
            clearedMessagePosition
    );

    assertTrue(outputAfterClear.contains(
            "  A _ _ _ _ _ _ _ _ _"
    ));
}

@Test
void checkReportsRuleViolation() {
    int[][] clues = new int[9][9];
    clues[0][0] = 5;

    Puzzle puzzle = new Puzzle(
            clues,
            new int[9][9]
    );

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("A2 5\ncheck\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains("Move accepted."));
    assertTrue(output.contains(
            "Number 5 already exists in Row A."
    ));
}

@Test
void checkReportsNoViolationsForValidGrid() {
    Puzzle puzzle = new Puzzle(
            new int[9][9],
            new int[9][9]
    );

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("check\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    assertTrue(
            capturedOutput.toString().contains(
                    "No rule violations detected."
            )
    );
}

private int[][] completedGrid() {
    return new int[][]{
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

@Test
void hintRevealsCorrectValue() {
    int[][] solution = completedGrid();
    int[][] clues = copy(solution);

    clues[8][8] = 0;

    Puzzle puzzle = new Puzzle(clues, solution);

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("hint\nquit\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains("Hint: Cell I9 = 9"));
    assertTrue(output.contains(
            "  I 3 4 5 2 8 6 1 7 9"
    ));
}

@Test
void announcesSuccessfulCompletion() {
    int[][] solution = completedGrid();
    int[][] clues = copy(solution);

    clues[8][8] = 0;

    Puzzle puzzle = new Puzzle(clues, solution);

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner("I9 9\n"),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertTrue(output.contains(
            "You have successfully completed "
                    + "the Sudoku puzzle!"
    ));
}

@Test
void startsAnotherPuzzleWhenPlayerChoosesYes() {
    int[][] solution = completedGrid();
    int[][] clues = copy(solution);
    clues[8][8] = 0;

    Puzzle puzzle = new Puzzle(clues, solution);

    SudokuGenerator generator = fixedGenerator(puzzle);
    StringWriter capturedOutput = new StringWriter();

    SudokuGame game = new SudokuGame(
            generator,
            new CommandParser(),
            new SudokuValidator(),
            new Scanner(
                    "I9 9\n"
                            + "y\n"
                            + "I9 9\n"
                            + "n\n"
            ),
            new PrintWriter(capturedOutput, true),
            new Random(42)
    );

    game.run();

    String output = capturedOutput.toString();

    assertEquals(
            2,
            countOccurrences(
                    output,
                    "Here is your puzzle:"
            )
    );

    assertEquals(
            2,
            countOccurrences(
                    output,
                    "You have successfully completed "
                            + "the Sudoku puzzle!"
            )
    );
}

private int countOccurrences(
        String text,
        String search
) {
    int count = 0;
    int position = 0;

    while (
            (position = text.indexOf(search, position))
                    != -1
    ) {
        count++;
        position += search.length();
    }

    return count;
}

}