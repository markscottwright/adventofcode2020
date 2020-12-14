package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Test;

public class Day14Test {

    @Test
    public void testPart1() {
        var masks = Day14.ValueMask.parse("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X");
        assertThat(masks.apply(11)).isEqualTo(73);
        assertThat(masks.apply(101)).isEqualTo(101);
        assertThat(masks.apply(0)).isEqualTo(64);
    }
    
    @Test
    public void testPart2() throws Exception {
        var mask1 = Day14.AddressMask.parse("000000000000000000000000000000X1001X");
        assertThat(mask1.apply(42)).containsOnly(26L, 27L, 58L, 59L);
        var mask2 = Day14.AddressMask.parse("00000000000000000000000000000000X0XX");
        assertThat(mask2.apply(26)).containsOnly(16L, 17L, 18L, 19L, 24L, 25L, 26L, 27L);
        
        HashMap<Long, Long> memory = new HashMap<>();
        mask1.apply(42).forEach(a -> memory.put(a, 100L));
        mask2.apply(26).forEach(a -> memory.put(a, 1L));
        System.out.println(memory);
    }

}
