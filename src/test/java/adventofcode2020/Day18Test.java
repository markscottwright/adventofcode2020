package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

public class Day18Test {

    @Test
    public void testPart1() {
        assertThat(Day18.Expression.parsePart1("2 * 3 + (4 * 5").eval())
                .isEqualTo(26);
        assertThat(Day18.Expression.parsePart1("5 + (8 * 3 + 9 + 3 * 4 * 3)")
                .eval()).isEqualTo(437);
        assertThat(Day18.Expression
                .parsePart1("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")
                .eval()).isEqualTo(13632);
    }

    @Test
    public void testPart2() throws Exception {
        assertThat(Day18.Expression.parsePart2("1 + (2 * 3) + (4 * (5 + 6)) ")
                .eval()).isEqualTo(51);
        assertThat(Day18.Expression.parsePart2("2 * 3 + (4 * 5)").eval())
                .isEqualTo(46);
        assertThat(Day18.Expression.parsePart2("5 + (8 * 3 + 9 + 3 * 4 * 3) ")
                .eval()).isEqualTo(1445);
        assertThat(Day18.Expression
                .parsePart2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))").eval())
                        .isEqualTo(669060);
    }

}
