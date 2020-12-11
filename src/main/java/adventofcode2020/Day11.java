package adventofcode2020;

import static adventofcode2020.Day11.SeatState.empty;
import static adventofcode2020.Day11.SeatState.floor;
import static adventofcode2020.Day11.SeatState.occupied;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class Day11 {

    enum SeatState {
        empty, occupied, floor
    }

    static public class SeatGrid {
        SeatState[][] seats;

        /**
         * Read in lines in form 'LLL...LLL' into a 2d grid of SeatStates
         */
        public SeatGrid(List<String> inputLines) {
            seats = new SeatState[inputLines.size()][];
            int row = 0;
            for (String string : inputLines) {
                seats[row] = new SeatState[string.length()];
                int col = 0;
                for (char c : string.toCharArray()) {
                    seats[row][col] = c == '.' ? floor
                            : (c == 'L') ? empty : occupied;
                    col++;
                }
                row++;
            }
        }

        /**
         * Keep running seat state mutation until the next state is the same as
         * the previous state.
         */
        public void runUntilStable(boolean isPartTwo) {
            boolean stable = false;
            while (!stable) {
                // all seat changes take place simultaneously, so create a new
                // nextState grid
                SeatState[][] nextState = new SeatState[seats.length][];
                for (int row = 0; row < seats.length; ++row) {
                    nextState[row] = new SeatState[seats[row].length];
                    for (int col = 0; col < seats[row].length; ++col) {
                        // empty and num adjacent occupied seats = 0, become
                        // occupied

                        // occupied and num adjacent occupied seats >= 4, become
                        // empty

                        // otherwise, no change
                        int n;
                        if (isPartTwo)
                            n = numVisibleOccupiedSeats(row, col);
                        else
                            n = numAdjacentOccupiedSeats(row, col);
                        if (seats[row][col] == empty && n == 0) {
                            nextState[row][col] = occupied;
                        } else if (seats[row][col] == occupied
                                && (isPartTwo ? n >= 5 : n >= 4)) {
                            nextState[row][col] = empty;
                        } else {
                            nextState[row][col] = seats[row][col];
                        }
                    }
                }
                stable = Arrays.deepEquals(seats, nextState);
                seats = nextState;
            }
        }

        private SeatState getSeatState(int row, int col) {
            if (row < 0 || row >= seats.length)
                return SeatState.floor;
            else if (col < 0 || col >= seats[row].length)
                return SeatState.floor;
            return seats[row][col];
        }

        private int numAdjacentOccupiedSeats(int row, int col) {
            int n = 0;
            if (getSeatState(row - 1, col - 1) == occupied)
                n++;
            if (getSeatState(row - 1, col) == occupied)
                n++;
            if (getSeatState(row - 1, col + 1) == occupied)
                n++;
            if (getSeatState(row, col - 1) == occupied)
                n++;
            if (getSeatState(row, col + 1) == occupied)
                n++;
            if (getSeatState(row + 1, col - 1) == occupied)
                n++;
            if (getSeatState(row + 1, col) == occupied)
                n++;
            if (getSeatState(row + 1, col + 1) == occupied)
                n++;
            return n;
        }

        private int numVisibleOccupiedSeats(int row, int col) {
            int n = 0;
            if (firstSeatInDirectionIsOccupied(row, col, -1, -1))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, -1, 0))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, -1, +1))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, 0, -1))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, 0, +1))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, +1, -1))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, +1, 0))
                n++;
            if (firstSeatInDirectionIsOccupied(row, col, +1, +1))
                n++;
            return n;
        }

        private boolean firstSeatInDirectionIsOccupied(int row, int col,
                int rowMovement, int colMovement) {
            do {
                row += rowMovement;
                col += colMovement;
                SeatState state = getSeatState(row, col);
                if (state == occupied)
                    return true;
                else if (state == empty)
                    return false;
            } while (row >= 0 && col >= 0 && row < seats.length
                    && col <= seats[row].length);
            return false;
        }

        public int numOccupiedSeats() {
            int n = 0;
            for (int row = 0; row < seats.length; ++row)
                for (int col = 0; col < seats[row].length; ++col)
                    if (seats[row][col] == occupied)
                        n++;
            return n;
        }

        public void printOn(PrintStream out) {
            for (int row = 0; row < seats.length; ++row) {
                for (int col = 0; col < seats[row].length; ++col) {
                    if (seats[row][col] == occupied)
                        System.out.print("#");
                    else if (seats[row][col] == empty)
                        System.out.print("L");
                    else
                        System.out.print(".");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] emptyArgs) {
        SeatGrid seatGrid = new SeatGrid(Util.inputLinesForDay(11));
        seatGrid.runUntilStable(false);
        System.out.println("Day 11 part 1: " + seatGrid.numOccupiedSeats());
        seatGrid = new SeatGrid(Util.inputLinesForDay(11));
        seatGrid.runUntilStable(true);
        System.out.println("Day 11 part 2: " + seatGrid.numOccupiedSeats());
    }

}
