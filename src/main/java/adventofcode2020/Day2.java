package adventofcode2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
    static public class PasswordEntry {
        static Pattern pattern = Pattern
                .compile("([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)");

        final int min;
        final int max;
        final char theChar;
        final String thePassword;

        public PasswordEntry(int min, int max, char theChar,
                String thePassword) {
            this.min = min;
            this.max = max;
            this.theChar = theChar;
            this.thePassword = thePassword;
        }

        boolean isGoodPart1() {
            int count = 0;
            for (char c : thePassword.toCharArray()) {
                if (c == theChar)
                    count++;
            }

            return count >= min && count <= max;
        }

        boolean isGoodPart2() {
            boolean match1 = thePassword.charAt(min - 1) == theChar;
            boolean match2 = thePassword.charAt(max - 1) == theChar;
            return !(match1 && match2) && (match1 || match2);
        }

        public static PasswordEntry parse(String s) {
            Matcher matcher = pattern.matcher(s);
            if (!matcher.matches()) {
                throw new RuntimeException(s + " bad format");
            }
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            char theChar = matcher.group(3).charAt(0);
            String thePassword = matcher.group(4);
            return new PasswordEntry(min, max, theChar, thePassword);
        }

        @Override
        public String toString() {
            return "PasswordEntry [min=" + min + ", max=" + max + ", theChar="
                    + theChar + ", thePassword=" + thePassword + "]";
        }

    }

    public static void main(String[] args) throws IOException {
        var goodPasswords1 = Files
                .readAllLines(Paths.get("src/main/resources/day2.txt")).stream()
                .map(PasswordEntry::parse).filter(PasswordEntry::isGoodPart1)
                .count();
        System.out.println("Day 2 part 1:" + goodPasswords1);
        var goodPasswords2 = Files
                .readAllLines(Paths.get("src/main/resources/day2.txt")).stream()
                .map(PasswordEntry::parse).filter(PasswordEntry::isGoodPart2)
                .count();
        System.out.println("Day 2 part 2:" + goodPasswords2);
    }
}
