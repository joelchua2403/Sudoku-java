package com.sudoku;

import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class SudokuGame {

    private final SudokuGenerator generator;
    private final CommandParser parser;
    private final SudokuValidator validator;
    private final Scanner input;
    private final PrintWriter output;
    private final Random random;

    public SudokuGame(
            SudokuGenerator generator,
            CommandParser parser,
            SudokuValidator validator,
            Scanner input,
            PrintWriter output,
            Random random
    ) {
        this.generator = generator;
        this.parser = parser;
        this.validator = validator;
        this.input = input;
        this.output = output;
        this.random = random;
    }

    public void run() {
        output.println("Welcome to Sudoku!");

        Puzzle puzzle = generator.generate();
        SudokuBoard board = new SudokuBoard(
                puzzle.clues()
        );

        output.println();
        output.println("Here is your puzzle:");
        output.println(board.display());

        while (input.hasNextLine()) {
            output.println();
            output.println(
                    "Enter command "
                            + "(e.g., A3 4, C5 clear, "
                            + "hint, check, quit):"
            );
            output.flush();

            Command command = parser.parse(
                    input.nextLine()
            );

            if (command instanceof Command.Quit) {
                break;
            }
        }

        output.println("Thanks for playing!");
        output.flush();
    }
}