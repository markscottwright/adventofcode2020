package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
    private List<Integer> values;

    public static void main(String[] args) throws IOException {
        System.out.println("Day 1 part 1: " + new Day1().part1());
        System.out.println("Day 1 part 2: " + new Day1().part2());
    }

    public Day1() throws IOException {
        values = Files.readAllLines(Paths.get("src/main/resources/day1.txt"))
                .stream().map(Integer::parseInt).collect(Collectors.toList());
        values.sort(Integer::compare);
    }

    private int part2() {
        for (int i = 0; i < values.size(); ++i) {
            for (int j = 0; j < values.size(); ++j) {
                for (int k = 0; k < values.size(); ++k) {
                    if (values.get(i) + values.get(j) + values.get(k) == 2020)
                        return values.get(i) * values.get(j) * values.get(k);
//                    if (values.get(i) + values.get(j) + values.get(k) > 2020)
//                        break;
                }
            }
        }

        throw new RuntimeException("No solution found");
    }

    private int part1() throws IOException {
        for (int i = 0; i < values.size(); ++i)
            for (int j = 0; j < values.size(); ++j) {
                if (values.get(i) + values.get(j) == 2020)
                    return values.get(i) * values.get(j);
            }

        throw new RuntimeException("No solution found");
    }
}
