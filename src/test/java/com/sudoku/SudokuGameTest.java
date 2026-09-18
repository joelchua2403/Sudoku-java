package com.sudoku;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.Scanner;

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
}