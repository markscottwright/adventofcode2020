package adventofcode2020;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
    public interface Validator {
        boolean isValid(String value);
    }

    static public class PassportField {
        static HashMap<String, Validator> validators = new HashMap<>();
        static {
            validators.put("byr", v -> {
                return matches("[0-9]{4}", v) && parseInt(v) >= 1920
                        && parseInt(v) <= 2002;

            });
            validators.put("iyr", v -> {
                return matches("[0-9]{4}", v) && parseInt(v) >= 2010
                        && parseInt(v) <= 2020;

            });
            validators.put("eyr", v -> {
                return matches("[0-9]{4}", v) && parseInt(v) >= 2020
                        && parseInt(v) <= 2030;

            });
            validators.put("hgt", v -> {
                Matcher m = Pattern.compile("([0-9]+)in").matcher(v);
                if (m.matches() && parseInt(m.group(1)) >= 59
                        && parseInt(m.group(1)) <= 76)
                    return true;
                m = Pattern.compile("([0-9]+)cm").matcher(v);
                if (m.matches() && parseInt(m.group(1)) >= 150
                        && parseInt(m.group(1)) <= 193)
                    return true;
                return false;
            });
            validators.put("hcl", v -> {
                return matches("#[a-f0-9]{6}", v);
            });
            validators.put("ecl", v -> {
                return matches("amb|blu|brn|gry|grn|hzl|oth", v);
            });
            validators.put("pid", v -> {
                return matches("[0-9]{9}", v);
            });
        };

        final String key;
        final String value;

        public PassportField(String key, String value) {
            this.key = key;
            this.value = value;
        }

        static PassportField parse(String s) {
            var keyValue = s.split(":");
            return new PassportField(keyValue[0], keyValue[1]);
        }

        public boolean isValid() {
            boolean isValid = key.equals("cid") || (validators.containsKey(key)
                    && validators.get(key).isValid(value));
            if (isValid)
                return true;

            // System.out.println("Invalid: " + this);
            return false;
        }

        @Override
        public String toString() {
            return "PassportField [key=" + key + ", value=" + value + "]";
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int numValidPassportsPart1 = 0;
        int numValidPassportsPart2 = 0;
        var requiredFieldNames = new HashSet<>(
                asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"));
        try (var scanner = new Scanner(new File("src/main/resources/day4.txt"));
                var passportLineReader = scanner.useDelimiter("\n\n")) {
            while (passportLineReader.hasNext()) {
                var fields = Arrays
                        .stream(passportLineReader.next().split("\\s+"))
                        .map(PassportField::parse).collect(toList());
                var fieldNames = fields.stream().map(f -> f.key)
                        .collect(toSet());

                if (fieldNames.containsAll(requiredFieldNames)) {
                    numValidPassportsPart1 += 1;
                    if (fields.stream().allMatch(f -> f.isValid()))
                        numValidPassportsPart2 += 1;
                }
            }
        }

        System.out.println("Day 4 part 1: " + numValidPassportsPart1);
        System.out.println("Day 4 part 2: " + numValidPassportsPart2);
    }
}
