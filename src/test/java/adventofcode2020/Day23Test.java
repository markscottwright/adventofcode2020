package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import adventofcode2020.Day23.Cups;

public class Day23Test {

    @Test
    public void test() {
        Cups cups = new Cups(3, 8, 9, 1, 2, 5, 4, 6, 7);
        for (int i = 0; i < 100; ++i)
            cups.playRound();
        assertThat(cups.labelsAfter1()).isEqualTo("67384529");
    }

}
