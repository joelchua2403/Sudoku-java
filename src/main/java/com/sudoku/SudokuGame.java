package com.sudoku;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SudokuGame {

    private static final String PROMPT =
            "Enter command "
                    + "(e.g., A3 4, C5 clear, "
                    + "hint, check, quit):";

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

        boolean playAgain = true;

        while (playAgain) {
            Puzzle puzzle = generator.generate();
            playAgain = play(puzzle);
        }

        output.println("Thanks for playing!");
        output.flush();
    }

    private boolean play(Puzzle puzzle) {
        SudokuBoard board = new SudokuBoard(
                puzzle.clues()
        );

        output.println();
        output.println("Here is your puzzle:");
        output.println(board.display());

        while (input.hasNextLine()) {
            output.println();
            output.println(PROMPT);
            output.flush();

            Command command;

            try {
                command = parser.parse(
                        input.nextLine()
                );
            } catch (IllegalArgumentException exception) {
                output.println(exception.getMessage());
                continue;
            }

            if (command instanceof Command.Quit) {
                return false;
            }

            if (command instanceof Command.Place place) {
                handlePlace(board, place);
            }

            if (command instanceof Command.Clear clear) {
                handleClear(board, clear);
            }

            if (command instanceof Command.Check) {
                handleCheck(board);
            }

            if (command instanceof Command.Hint) {
                revealHint(
                        board,
                        puzzle.solution()
                );
            }

            if (validator.isSolved(board)) {
                output.println();
                output.println(
                        "You have successfully completed "
                                + "the Sudoku puzzle!"
                );

                output.println("Play again? (y/n)");
                output.flush();

                return input.hasNextLine()
                        && input.nextLine()
                                .trim()
                                .equalsIgnoreCase("y");
            }
        }

        return false;
    }

    private void handlePlace(
            SudokuBoard board,
            Command.Place place
    ) {
        try {
            board.set(
                    place.position(),
                    place.value()
            );

            output.println();
            output.println("Move accepted.");
            printCurrentGrid(board);
        } catch (
                IllegalStateException
                        | IllegalArgumentException exception
        ) {
            printInvalidMove(exception, board);
        }
    }

    private void handleClear(
            SudokuBoard board,
            Command.Clear clear
    ) {
        try {
            board.clear(clear.position());

            output.println();
            output.println("Cell cleared.");
            printCurrentGrid(board);
        } catch (
                IllegalStateException
                        | IllegalArgumentException exception
        ) {
            printInvalidMove(exception, board);
        }
    }

    private void handleCheck(SudokuBoard board) {
        List<Violation> violations =
                validator.validate(board);

        if (violations.isEmpty()) {
            output.println(
                    "No rule violations detected."
            );
            return;
        }

        for (Violation violation : violations) {
            output.println(violation.message());
        }
    }

    private void revealHint(
            SudokuBoard board,
            int[][] solution
    ) {
        List<Position> candidates =
                new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            for (int column = 0;
                 column < 9;
                 column++) {

                Position position =
                        new Position(row, column);

                boolean editable =
                        !board.isFixed(position);

                boolean needsCorrection =
                        board.get(position)
                                != solution[row][column];

                if (editable && needsCorrection) {
                    candidates.add(position);
                }
            }
        }

        if (candidates.isEmpty()) {
            output.println("No hint is needed.");
            return;
        }

        Position chosen = candidates.get(
                random.nextInt(candidates.size())
        );

        int correctValue =
                solution[chosen.row()]
                        [chosen.column()];

        board.set(chosen, correctValue);

        output.println(
                "Hint: Cell "
                        + chosen
                        + " = "
                        + correctValue
        );

        printCurrentGrid(board);
    }

    private void printInvalidMove(
            RuntimeException exception,
            SudokuBoard board
    ) {
        output.println();
        output.println(
                "Invalid move. "
                        + exception.getMessage()
                        + "."
        );

        printCurrentGrid(board);
    }

    private void printCurrentGrid(
            SudokuBoard board
    ) {
        output.println();
        output.println("Current grid:");
        output.println(board.display());
    }
}