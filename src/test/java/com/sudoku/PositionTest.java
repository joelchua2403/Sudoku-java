package com.sudoku;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PositionTest {

    @Test
    void storesRowAndColumn() {
        Position position = new Position(1, 2);

        assertEquals(1, position.row());
        assertEquals(2, position.column());
    }

    @Test
    void rejectsNegativeRow() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Position(-1, 2)
        );
    }

    @Test
    void rejectsRowGreaterThanEight() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Position(9, 2)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 9})
    void rejectsInvalidColumn(int column) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Position(2, column)
        );
    }

    @Test
    void parsesCoordinate() {
        Position position = Position.parse("B3");

        assertEquals(1, position.row());
        assertEquals(2, position.column());
    }

    @Test
    void parsesLowercaseCoordinate() {
        Position position = Position.parse("b3");

        assertEquals(new Position(1, 2), position);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "A",
            "A0",
            "A10",
            "J1",
            "11",
            "AA"
    })
    @NullSource
    void rejectsInvalidCoordinate(String input) {
        assertThrows(
                IllegalArgumentException.class,
                () -> Position.parse(input)
        );
    }

    @Test
    void formatsPositionAsUserCoordinate() {
        Position position = new Position(1, 2);

        assertEquals("B3", position.toString());
    }
}