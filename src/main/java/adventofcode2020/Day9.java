package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.EvictingQueue;

public class Day9 {
    public static void main(String[] args) throws IOException {

        List<Long> input = Files
                .readAllLines(Paths.get("src/main/resources/day9.txt")).stream()
                .map(Long::parseLong).collect(Collectors.toList());

        long part1Answer = findNumberNotSumOfPrevious(input);
        System.out.println("Day 9 part 1: " + part1Answer);

        List<Long> sumParts = numbersThatAddTo(input, part1Answer);
        long part2Answer = sumParts.get(0) + sumParts.get(sumParts.size() - 1);
        System.out.println("Day 9 part 2: " + part2Answer);
    }

    private static List<Long> numbersThatAddTo(List<Long> input,
            long part1Answer) {
        for (int i = 0; i < input.size(); i++) {
            long sum = input.get(i);
            for (int k = i + 1; k < input.size(); ++k) {
                sum += input.get(k);
                if (sum == part1Answer) {
                    // return input[i...k]
                    List<Long> output = sortedSubList(input, i, k);
                    return output;
                } else if (sum > part1Answer)
                    break;
            }
        }

        throw new RuntimeException("No solution found");
    }

    private static List<Long> sortedSubList(List<Long> input, int from,
            int toInclusive) {
        List<Long> output = new ArrayList<Long>();
        for (int i = from; i <= toInclusive; ++i) {
            output.add(input.get(i));
        }
        output.sort(Long::compare);
        return output;
    }

    private static long findNumberNotSumOfPrevious(List<Long> input) {
        EvictingQueue<Long> last25Numbers = EvictingQueue.create(25);
        for (Long number : input) {
            if (last25Numbers.size() == 25) {
                boolean sumFound = false;
                for (Set<Long> potentialAddends : com.google.common.collect.Sets
                        .combinations(new HashSet<>(last25Numbers), 2)) {
                    Iterator<Long> addendIterator = potentialAddends.iterator();
                    Long addend1 = addendIterator.next();
                    Long addend2 = addendIterator.next();
                    if (number == addend1 + addend2) {
                        sumFound = true;
                    }
                }
                if (!sumFound) {
                    // System.out.println(number + " <- " + last25Numbers);
                    return number;
                }
            }
            last25Numbers.add(number);
        }

        throw new RuntimeException("No solution found");
    }

}
