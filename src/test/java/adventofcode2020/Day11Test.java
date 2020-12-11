package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class Day11Test {
    //@formatter:off
    String input = 
            "#.##.##.##\n"
                    + "#######.##\n"
                    + "#.#.#..#..\n"
                    + "####.##.##\n"
                    + "#.##.##.##\n"
                    + "#.#####.##\n"
                    + "..#.#.....\n"
                    + "##########\n"
                    + "#.######.#\n"
                    + "#.#####.##";
    //@formatter:on

    @Test
    public void test() {
        var seatGrid = new Day11.SeatGrid(Arrays.asList(input.split("\n")));
        seatGrid.runUntilStable(false);
        assertThat(seatGrid.numOccupiedSeats()).isEqualTo(37);
    }

    @Test
    public void testPartTwo() {
        var seatGrid = new Day11.SeatGrid(Arrays.asList(input.split("\n")));
        seatGrid.runUntilStable(true);
        assertThat(seatGrid.numOccupiedSeats()).isEqualTo(26);
    }

}
