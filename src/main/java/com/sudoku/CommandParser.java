package com.sudoku;

public class CommandParser {

    public Command parse(String input) {
        String normalized = input.trim();

        if (normalized.equalsIgnoreCase("hint")) {
            return new Command.Hint();
        }

        if (normalized.equalsIgnoreCase("check")) {
            return new Command.Check();
        }

        if (normalized.equalsIgnoreCase("quit")) {
            return new Command.Quit();
        }

        String[] parts = normalized.split("\\s+");

        if (parts.length != 2) {
            throw usageError();
        }

        Position position = Position.parse(parts[0]);

        if (parts[1].equalsIgnoreCase("clear")) {
            return new Command.Clear(position);
        }

        int value;

        try {
            value = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw usageError();
        }

        if (value < 1 || value > 9) {
            throw new IllegalArgumentException(
                    "Number must be between 1 and 9"
            );
        }

        return new Command.Place(position, value);
    }

    private IllegalArgumentException usageError() {
        return new IllegalArgumentException(
                "Invalid command. Use A3 4, C5 clear, hint, check, or quit."
        );
    }
}