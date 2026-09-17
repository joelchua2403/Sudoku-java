package com.sudoku;

public interface Command {

    record Place(Position position, int value) implements Command {
    }

    record Clear(Position position) implements Command {
    }

    record Hint() implements Command {
    }

    record Check() implements Command {
}

    record Quit() implements Command {
    }
}