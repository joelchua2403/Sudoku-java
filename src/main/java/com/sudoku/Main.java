package com.sudoku;

import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SudokuSolver solver = new SudokuSolver();

        SudokuGenerator generator =
                new SudokuGenerator(
                        solver,
                        new Random()
                );

        SudokuGame game = new SudokuGame(
                generator,
                new CommandParser(),
                new SudokuValidator(),
                new Scanner(System.in),
                new PrintWriter(System.out, true),
                new Random()
        );

        game.run();
    }
}