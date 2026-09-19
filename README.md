# Sudoku CLI

A production-oriented command-line Sudoku game implemented in Java 17 using object-oriented design and test-driven development.

The application generates randomized Sudoku puzzles with exactly 30 pre-filled cells. Every generated puzzle is checked to ensure that it has exactly one valid solution.

## Features

* Generates a randomized Sudoku puzzle
* Provides exactly 30 pre-filled cells
* Ensures that generated puzzles have a unique solution
* Displays the grid with row letters and column numbers
* Allows the player to place numbers from 1–9
* Allows the player to clear editable cells
* Protects original pre-filled cells from modification
* Provides hints using the generated solution
* Detects duplicate numbers in:

  * Rows
  * Columns
  * 3×3 subgrids
* Detects successful puzzle completion
* Allows the player to start another puzzle
* Supports case-insensitive commands
* Includes unit and end-to-end tests

## Requirements

The application can run on Windows, macOS or Linux.

Required software:

* Java Development Kit 17 or newer
* Apache Maven 3.8 or newer
* A command-line terminal

Confirm that Java and Maven are installed:

```
java -version
javac -version
mvn -version
```

Both `java` and `javac` should report version 17 or newer.

## Project structure

```
sudoku-java/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── sudoku/
    │               ├── Main.java
    │               ├── Position.java
    │               ├── Command.java
    │               ├── CommandParser.java
    │               ├── SudokuBoard.java
    │               ├── Violation.java
    │               ├── SudokuValidator.java
    │               ├── SudokuSolver.java
    │               ├── Puzzle.java
    │               ├── SudokuGenerator.java
    │               └── SudokuGame.java
    └── test/
        └── java/
            └── com/
                └── sudoku/
                    ├── PositionTest.java
                    ├── CommandParserTest.java
                    ├── SudokuBoardTest.java
                    ├── ViolationTest.java
                    ├── SudokuValidatorTest.java
                    ├── SudokuSolverTest.java
                    ├── SudokuGeneratorTest.java
                    └── SudokuGameTest.java
```

## Building the application

From the directory containing `pom.xml`, run:

```
mvn clean package
```

This command:

1. Removes previous build output.
2. Compiles the production source code.
3. Compiles the test code.
4. Runs the complete automated test suite.
5. Creates an executable JAR file.

If the build succeeds, Maven creates:

```
target/sudoku-cli-1.0.0.jar
```

## Running the application

After building the project, run:

```
java -jar target/sudoku-cli-1.0.0.jar
```

Alternatively, run the main class through Maven or directly through an IDE.

## Running the tests

Run the complete test suite with:

```
mvn clean test
```

A successful run should end with:

```
BUILD SUCCESS
```

Individual parameterized inputs are counted as separate test executions. Therefore, Maven may report more test executions than the number of test methods in the source files.

## Gameplay

When the game starts, it displays a generated puzzle:

```
Welcome to Sudoku!

Here is your puzzle:
    1 2 3 4 5 6 7 8 9
  A 5 3 _ _ 7 _ _ _ _
  B 6 _ _ 1 9 5 _ _ _
  C _ 9 8 _ _ _ _ 6 _
  D 8 _ _ _ 6 _ _ _ 3
  E 4 _ _ 8 _ 3 _ _ 1
  F 7 _ _ _ 2 _ _ _ 6
  G _ 6 _ _ _ _ 2 8 _
  H _ _ _ 4 1 9 _ _ 5
  I _ _ _ _ 8 _ _ 7 9

Enter command (e.g., A3 4, C5 clear, hint, check, quit):
```

Rows are identified by letters `A–I`.

Columns are identified by numbers `1–9`.

An underscore represents an empty cell.

## Commands

### Place a number

Enter a cell followed by a number:

```
B3 7
```

This places the number `7` in row B, column 3.

Numbers must be between `1` and `9`.

Commands and coordinates are case-insensitive:

```
b3 7
```

### Clear a cell

Enter a cell followed by `clear`:

```
C5 clear
```

This returns the selected editable cell to an empty state.

Pre-filled cells cannot be cleared.

### Request a hint

Enter:

```
hint
```

The game selects an editable cell that is empty or incorrect and places the correct solution value into it.

Example:

```
Hint: Cell E5 = 5
```

### Check the grid

Enter:

```
check
```

The game checks the current board for duplicate numbers in rows, columns and 3×3 subgrids.

If no violations are found:

```
No rule violations detected.
```

Example row violation:

```
Number 3 already exists in Row A.
```

Example column violation:

```
Number 5 already exists in Column 1.
```

Example subgrid violation:

```
Number 8 already exists in the same 3x3 subgrid.
```

### Quit the game

Enter:

```
quit
```

The current game ends immediately.

## Move validation

A place or clear command validates that:

* The coordinate is between `A1` and `I9`
* The command has the correct format
* A placed number is between `1` and `9`
* A pre-filled cell is not modified
* A pre-filled cell is not cleared

Following the supplied gameplay examples, a number that conflicts with Sudoku rules is accepted initially.

For example:

```
A3 3
```

may be accepted even if row A already contains `3`.

The violation is reported when the player enters:

```
check
```

This separates move-entry validation from Sudoku-rule validation.

## Completion

The game is completed when:

1. Every cell contains a number.
2. There are no duplicate numbers in any row.
3. There are no duplicate numbers in any column.
4. There are no duplicate numbers in any 3×3 subgrid.

After completion, the application displays:

```
You have successfully completed the Sudoku puzzle!
Play again? (y/n)
```

Entering `y` starts a newly generated puzzle. Any other response ends the application.

## Design overview

The application separates responsibilities across small, focused classes.

### `Main`

The application entry point.

It creates the production dependencies, connects them and starts `SudokuGame`.

`Main` contains no Sudoku business logic.

### `Position`

Represents a zero-based row and column.

It converts user-facing coordinates such as `B3` into internal array indexes:

```
B3 → row 1, column 2
```

It also converts internal coordinates back into user-facing form for hints and error messages.

### `Command`

Defines the supported command types:

* `Place`
* `Clear`
* `Hint`
* `Check`
* `Quit`

Java records are used because commands are small immutable data objects.

### `CommandParser`

Converts raw command-line text into typed `Command` objects.

For example:

```
"B3 7"
```

becomes conceptually:

```
Place(Position(1, 2), 7)
```

The parser handles whitespace, case-insensitive keywords, number ranges and malformed commands.

### `SudokuBoard`

Owns the mutable state of the current game.

Its responsibilities include:

* Storing the current cell values
* Remembering which cells were originally pre-filled
* Protecting clues from modification
* Placing numbers
* Clearing editable cells
* Detecting whether the board is full
* Producing a safe grid snapshot
* Formatting the board for terminal display

The board copies incoming arrays so external code cannot silently change its internal state.

### `Violation`

Represents a Sudoku-rule violation.

A violation contains:

* Violation type
* Duplicate value
* Relevant row, column or subgrid index

It also produces a human-readable message for the command-line interface.

### `SudokuValidator`

Checks the current board for duplicate numbers.

It validates:

* All nine rows
* All nine columns
* All nine 3×3 subgrids

Empty cells are ignored.

It also determines whether a board is solved by combining:

```
board is full AND no violations exist
```

### `SudokuSolver`

Solves Sudoku grids using recursive backtracking.

For each empty cell, the solver:

1. Tries candidate values from 1–9.
2. Checks whether the candidate can be placed legally.
3. Recursively attempts to solve the remaining cells.
4. Removes the candidate if that path fails.
5. Tries the next candidate.

The solver also counts solutions up to a supplied limit. The generator uses this capability to determine whether a puzzle has a unique solution.

### `Puzzle`

Stores two separate grids:

* `clues`: the puzzle shown to the player
* `solution`: the completed answer used for hints

Keeping them separate ensures that removing clues does not modify the completed solution.

### `SudokuGenerator`

Creates randomized puzzles in two stages.

First, it generates a complete solution:

1. Randomly fills the three diagonal 3×3 subgrids.
2. Uses the backtracking solver to fill the remaining cells.

Second, it creates the playable puzzle:

1. Copies the completed solution.
2. Randomly selects cells for removal.
3. Temporarily removes a value.
4. Uses the solver to count remaining solutions.
5. Keeps the removal only when exactly one solution remains.
6. Continues until exactly 30 clues remain.

If a removal order cannot reach 30 clues while maintaining uniqueness, the generator retries using a new completed grid.

### `SudokuGame`

Coordinates the user interface.

It is responsible for:

* Displaying the puzzle
* Reading commands
* Calling the appropriate domain component
* Displaying results and errors
* Providing hints
* Detecting completion
* Managing replay and quitting

Console input and output are injected into the class, allowing the complete workflow to be tested without real keyboard input.

## Puzzle-generation algorithm

A blank Sudoku grid contains a very large number of possible solutions. To produce varied grids efficiently, the generator first fills the diagonal subgrids:

```
X . .
. X .
. . X
```

These three subgrids do not share rows or columns with one another, so each can safely contain a randomized arrangement of `1–9`.

The solver then completes the remaining cells.

To produce a 30-clue puzzle, values are removed in random order. After every removal, the solver counts solutions up to a maximum of two:

* `0`: the puzzle is unsolvable
* `1`: the puzzle has a unique solution
* `2`: the puzzle has multiple solutions

A removal is retained only when the solution count remains exactly one.

## Backtracking algorithm

The solver views the board as 81 sequential cells.

For each empty position:

1. Try values `1–9`.
2. Reject any value already present in the same row.
3. Reject any value already present in the same column.
4. Reject any value already present in the same 3×3 subgrid.
5. Place a valid candidate.
6. Recursively continue to the next cell.
7. If no solution is possible, reset the cell to zero and try another value.

The algorithm finishes successfully when all 81 cells have been processed.

## Testing approach

The project was developed using the Red-Green-Refactor TDD cycle.

### Red

A failing test was written before implementing each behaviour.

Examples:

* Creating a position
* Rejecting invalid coordinates
* Parsing commands
* Protecting pre-filled cells
* Detecting duplicate values
* Solving a puzzle
* Ensuring puzzle uniqueness
* Handling command-line interactions

### Green

The minimum implementation required to pass the failing test was added.

### Refactor

Once tests passed, duplication was removed and responsibilities were extracted into focused helper methods and classes.

The test suite includes:

* Unit tests for domain classes
* Boundary-value tests
* Parameterized tests
* Solver and generator tests
* Command parsing tests
* Console workflow tests
* End-to-end game interaction tests

Random-dependent tests use a fixed seed so their behaviour is repeatable.

## Design principles

The implementation aims to follow clean-code and SOLID principles.

### Single Responsibility Principle

Each class has one primary responsibility. For example:

* Parsing is handled by `CommandParser`
* State is handled by `SudokuBoard`
* Validation is handled by `SudokuValidator`
* Solving is handled by `SudokuSolver`
* Generation is handled by `SudokuGenerator`

### Encapsulation

`SudokuBoard` keeps its arrays private and returns copies when exposing grid data.

All mutations pass through controlled methods such as:

```
set()
clear()
```

### Dependency injection

`SudokuGame` and `SudokuGenerator` receive their dependencies through constructors.

This avoids hidden dependencies and makes tests deterministic.

### Small methods

Complex operations are divided into focused methods such as:

```
handlePlace()
handleClear()
handleCheck()
revealHint()
canPlace()
countFrom()
```

### Immutable value objects

Records are used for data-oriented types such as:

* `Position`
* `Command.Place`
* `Command.Clear`
* `Violation`
* `Puzzle`

## Assumptions

* Java array indexes are zero-based internally.
* User-facing rows are labelled `A–I`.
* User-facing columns are numbered `1–9`.
* Zero represents an empty cell internally.
* Empty cells are displayed as `_`.
* Pre-filled cells are immutable.
* Player-entered duplicate values are accepted until `check` is requested.
* A hint may fill an empty cell or correct an incorrect editable cell.
* Generated puzzles must contain exactly 30 clues.
* Generated puzzles must have exactly one solution.
* A completed board must be full and contain no rule violations.
* Commands are case-insensitive.
* The game uses terminal input and output only.
* No external Sudoku-solving library is used.

## Error handling

Invalid commands produce a usage message instead of terminating the application.

Examples of invalid input include:

```
hello
A0 5
J3 4
B2 0
B2 10
B2 seven
B2 5 extra
```

Attempts to modify clues produce messages such as:

```
Invalid move. A1 is pre-filled.
```

After an invalid command or move, the application continues accepting input.

## Performance considerations

Sudoku solving and uniqueness checking use recursive backtracking.

The solution counter stops when it reaches the requested limit. During generation, the limit is two because the generator only needs to distinguish unique puzzles from puzzles with multiple solutions.

This avoids unnecessarily counting every possible solution.

Puzzle generation is retried if a particular removal order cannot produce exactly 30 unique clues.

## Known limitations

* The application uses a command-line interface only.
* Game progress is not persisted between sessions.
* There is no undo command.
* Difficulty is not formally graded.
* Clue placement is randomized rather than visually symmetrical.
* The application does not provide candidate-note functionality.
* The application does not time the player.

These features could be added without replacing the core board, validation or solving components.

## Possible future improvements

* Difficulty levels
* Symmetrical clue generation
* Undo and redo
* Candidate notes
* Saved games
* Timer and scoring
* Coloured terminal output
* Graphical or web interface
* Improved solving heuristics
* Most-constrained-cell selection
* Puzzle-generation benchmarks

## Clean build

To remove generated build output:

```
mvn clean
```

The `target` directory is generated by Maven and should not be committed.

## Submission contents

The submission should include:

* Java source files
* JUnit tests
* `pom.xml`
* `README.md`
* `.gitignore`

The submission should not include:

* `target/`
* `.class` files
* IDE-specific build output
* Executable platform-specific attachments

## Author

Joel Chua
