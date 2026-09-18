package com.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViolationTest {

    @Test
    void formatsRowViolationMessage() {
        Violation violation = new Violation(
                Violation.Type.ROW,
                3,
                0
        );

        assertEquals(
                "Number 3 already exists in Row A.",
                violation.message()
        );
    }

    @Test
    void formatsColumnViolationMessage() {
        Violation violation = new Violation(
                Violation.Type.COLUMN,
                5,
                0
        );

        assertEquals(
                "Number 5 already exists in Column 1.",
                violation.message()
        );
    }

    @Test
    void formatsSubgridViolationMessage() {
        Violation violation = new Violation(
                Violation.Type.SUBGRID,
                8,
                0
        );

        assertEquals(
                "Number 8 already exists in the same 3x3 subgrid.",
                violation.message()
        );
    }
}