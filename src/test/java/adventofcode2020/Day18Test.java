package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class Day18Test {

    @Test
    public void test() {
        assertThat(Day18.Expression.parse("2 * 3 + (4 * 5").eval())
                .isEqualTo(26);
        assertThat(Day18.Expression.parse("5 + (8 * 3 + 9 + 3 * 4 * 3)").eval())
                .isEqualTo(437);
        assertThat(Day18.Expression
                .parse("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")
                .eval()).isEqualTo(13632);
    }

}
