package adventofcode2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

public class Day6 {

    static Set<Character> allGroupAnswers(String group) {
        Set<Character> allAnswers = new TreeSet<>();
        for (int i = 0; i < group.length(); ++i) {
            if (!Character.isWhitespace(group.charAt(i)))
                allAnswers.add(group.charAt(i));
        }
        return allAnswers;
    }

    static Set<Character> toMutableSetOfChar(String s) {
        TreeSet<Character> chars = new TreeSet<Character>();
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i)))
                chars.add(s.charAt(i));
        }
        return chars;
    }

    static Set<Character> allCommonAnswers(String group) {
        return Util.delimitedStream(group, "\n").map(Day6::toMutableSetOfChar)
                .reduce((s1, s2) -> {
                    s1.retainAll(s2);
                    return s1;
                }).get();
    }

    public static void main(String[] args) throws FileNotFoundException {
        int part1Answer = Util.delimitedStream(
                new File("src/main/resources/day6.txt"), "\n\n")
                        .map(Day6::allGroupAnswers).mapToInt(Set::size).sum();
        System.out.println("Day 6 part 1: " + part1Answer);
        int part2Answer = Util.delimitedStream(
                new File("src/main/resources/day6.txt"), "\n\n")
                        .map(Day6::allCommonAnswers).mapToInt(Set::size).sum();
        System.out.println("Day 6 part 2: " + part2Answer);
    }
}
