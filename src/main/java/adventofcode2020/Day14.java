package adventofcode2020;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class Day14 {
    /**
     * I modify values based on the AOC Day 14 algorithm
     */
    static class ValueMask {

        private long setZeroMask;
        private long setOneMask;

        public ValueMask(long setZeroMask, long setOneMask) {
            this.setZeroMask = setZeroMask;
            this.setOneMask = setOneMask;
        }

        long apply(long v) {
            return (v | setOneMask) & setZeroMask;
        }

        public static ValueMask parse(String string) {
            long setZeroMask = 0;
            long setOneMask = 0;
            for (int i = 0; i < string.length(); ++i) {
                char singleBitMask = string.charAt(i);
                setZeroMask <<= 1;
                setOneMask <<= 1;
                if (singleBitMask == '1') {
                    setOneMask |= 1;
                    setZeroMask |= 1;
                } else if (singleBitMask == '0') {
                    setOneMask |= 1;
                } else if (singleBitMask == 'X') {
                    setZeroMask |= 1;
                }
            }
            return new ValueMask(setZeroMask, setOneMask);
        }
    }

    /**
     * I return all possible addresses based on the Day 14 part 2 mask algorithm
     */
    static class AddressMask {
        long setOneMask = 0;
        ArrayList<Integer> varyBits = new ArrayList<>();

        public AddressMask(long setOneMask, ArrayList<Integer> varyBits) {
            this.setOneMask = setOneMask;
            this.varyBits = varyBits;
        }

        static AddressMask parse(String s) {
            ArrayList<Integer> varyBits = new ArrayList<>();
            long setOneMask = 0;
            for (int i = 0; i < s.length(); ++i) {
                char maskBit = s.charAt(s.length() - 1 - i);
                if (maskBit == '1') {
                    setOneMask |= (1L << i);
                } else if (maskBit == 'X')
                    varyBits.add(i);
            }
            return new AddressMask(setOneMask, varyBits);
        }

        /**
         * This can't work for all possible mask values (36 bits is too large),
         * but the puzzle data doesn't have masks with more than 9 'x's
         * 
         * @param address
         *            initial address
         * @return all possible addresses for the given mask.
         */
        Collection<Long> apply(long address) {
            HashSet<Long> allAddresses = new HashSet<Long>();
            address |= setOneMask;
            allAddresses.add(address);
            for (int bitPosition : varyBits) {
                for (var a : new HashSet<>(allAddresses)) {
                    allAddresses.add(a | (1L << bitPosition));
                    allAddresses.add(a & ~(1L << bitPosition));
                }
            }
            return allAddresses;
        }
    }

    public static void main(String[] strings) {
        {
            ValueMask mask = null;
            HashMap<Long, Long> memory = new HashMap<>();
            for (String l : Util.inputLinesForDay(14)) {
                if (l.startsWith("mask"))
                    mask = ValueMask.parse(l.split(" = ")[1]);
                else {
                    long[] addressAndValue = parseMemSet(l);
                    long maskedValue = mask.apply(addressAndValue[1]);
                    memory.put(addressAndValue[0], maskedValue);
                }
            }
            long sumOfAllMemoryValues = memory.values().stream()
                    .mapToLong(v -> v).sum();
            System.out.print("Day 14 part 1: ");
            System.out.println(sumOfAllMemoryValues);
        }

        {
            HashMap<Long, Long> memory = new HashMap<>();
            AddressMask mask = null;
            for (String l : Util.inputLinesForDay(14)) {
                if (l.startsWith("mask"))
                    mask = AddressMask.parse(l.split(" = ")[1]);
                else {
                    long[] addressAndValue = parseMemSet(l);
                    for (long address : mask.apply(addressAndValue[0])) {
                        memory.put(address, addressAndValue[1]);
                    }
                }
            }
            long sumOfAllMemoryValues = memory.values().stream()
                    .mapToLong(v -> v).sum();
            System.out.print("Day 14 part 2: ");
            System.out.println(sumOfAllMemoryValues);
        }
    }

    public static long[] parseMemSet(String l) {
        List<String> addressAndValue = Util.parseToGroups(
                Pattern.compile("mem\\[([0-9]+)\\] = ([0-9]+)"), l);

        return new long[] { Long.parseLong(addressAndValue.get(0)),
                Long.parseLong(addressAndValue.get(1)) };
    }

}
