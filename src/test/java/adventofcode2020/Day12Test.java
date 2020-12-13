package adventofcode2020;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import adventofcode2020.Day12.Instruction;

public class Day12Test {
    private List<Instruction> instructions;

    @Before
    public void setUp() {
        instructions = Arrays.asList("F10\nN3\nF7\nR90\nF11".split("\\n"))
                .stream().map(Day12.Instruction::parse)
                .collect(Collectors.toList());
    }

    @Test
    public void testPart1() {
        var position = new Day12.Position(0, 0, 'E');
        for (Day12.Instruction instruction : instructions) {
            position = position.apply(instruction);
        }
        assertThat(position.x).isEqualTo(17);
        assertThat(position.y).isEqualTo(8);
        assertThat(position.manhattanDistanceFrom(0,0)).isEqualTo(25);
    }

    @Test
    public void testPart2() {
        var position = new Day12.ShipAndWaypoint(0, 0, 10, -1);
        for (Day12.Instruction instruction : instructions) {
            position = position.apply(instruction);
        }
        assertThat(position.shipX).isEqualTo(214);
        assertThat(position.shipY).isEqualTo(72);
        assertThat(position.manhattanDistanceFrom(0,0)).isEqualTo(286);
    }
}
