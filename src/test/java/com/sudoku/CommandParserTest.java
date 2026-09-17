package com.sudoku;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    void parsesPlaceCommand() {
        Command command = parser.parse("B3 7");

        Command.Place place = assertInstanceOf(
                Command.Place.class,
                command
        );

        assertEquals(new Position(1, 2), place.position());
        assertEquals(7, place.value());
    }

    @Test
    void parsesPlaceCommandWithLowercaseAndExtraSpaces() {
        Command command = parser.parse("  b3    7  ");

        Command.Place place = assertInstanceOf(
                Command.Place.class,
                command
        );

        assertEquals(new Position(1, 2), place.position());
        assertEquals(7, place.value());
    }

    @Test
    void parsesClearCommand() {
        Command command = parser.parse("C5 clear");

        Command.Clear clear = assertInstanceOf(
                Command.Clear.class,
                command
        );

        assertEquals(new Position(2, 4), clear.position());
    }

    @Test
    void parsesClearCommandCaseInsensitively() {
        Command command = parser.parse("c5 CLEAR");

        Command.Clear clear = assertInstanceOf(
                Command.Clear.class,
                command
        );

        assertEquals(new Position(2, 4), clear.position());
    }

    @Test
    void parsesHintCommand() {
        Command command = parser.parse("hint");

        assertInstanceOf(Command.Hint.class, command);
    }

    @Test
    void parsesHintCommandCaseInsensitively() {
        Command command = parser.parse("  HINT  ");

        assertInstanceOf(Command.Hint.class, command);
    }

    @Test
void parsesCheckCommand() {
    Command command = parser.parse("check");

    assertInstanceOf(Command.Check.class, command);
}

@Test
void parsesQuitCommand() {
    Command command = parser.parse("quit");

    assertInstanceOf(Command.Quit.class, command);
}

@ParameterizedTest
@ValueSource(strings = {
        "B3 0",
        "B3 10",
        "B3 -1"
})
void rejectsNumbersOutsideOneToNine(String input) {
    assertThrows(
            IllegalArgumentException.class,
            () -> parser.parse(input)
    );
}

@ParameterizedTest
@ValueSource(strings = {
        "",
        "hello",
        "B3",
        "B3 seven",
        "B3 7 extra"
})
void rejectsMalformedCommands(String input) {
    assertThrows(
            IllegalArgumentException.class,
            () -> parser.parse(input)
    );
}

}