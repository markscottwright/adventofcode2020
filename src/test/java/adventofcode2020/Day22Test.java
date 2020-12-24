package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class Day22Test {
    // @formatter:off
    final static String input = "Player 1:\n" + 
            "9\n" + 
            "2\n" + 
            "6\n" + 
            "3\n" + 
            "1\n" + 
            "\n" + 
            "Player 2:\n" + 
            "5\n" + 
            "8\n" + 
            "4\n" + 
            "7\n" + 
            "10";
    // @formatter:on

    @Test
    public void testPart1() {
        Day22.Combat game = Day22.Combat
                .parse(Arrays.asList(input.split("\n")));

        game.play();
        assertThat(game.winningScore()).isEqualTo(306);
    }

    @Test
    public void testPart2() {
        Day22.Combat game = Day22.Combat
                .parse(Arrays.asList(input.split("\n")));
        game.playRecursiveGame();
        assertThat(game.winningScore()).isEqualTo(291);
    }

}
