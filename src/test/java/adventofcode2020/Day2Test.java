package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

import adventofcode2020.Day2.PasswordEntry;

public class Day2Test {

    @Test
    public void test() {
        assertThat(PasswordEntry.parse("1-3 a: abcde").isGoodPart1()).isTrue();
        assertThat(PasswordEntry.parse("1-3 b: cdefg").isGoodPart1()).isFalse();
        assertThat(PasswordEntry.parse("2-9 c: ccccccccc").isGoodPart1()).isTrue();
    }

}
