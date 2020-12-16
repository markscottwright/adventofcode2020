package adventofcode2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {

    public static void fail(String message) throws RuntimeException {
        throw new RuntimeException(message);
    }

    public static int sum(Collection<Integer> containers) {
        return containers.stream().mapToInt(i -> i).sum();
    }

    public static List<String> inputLinesForDay(int dayNum) {
        String filePath = "src/main/resources/day" + dayNum + ".txt";
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + filePath);
        }
    }

    static public Stream<String> delimitedStream(File file, String delimiter)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(delimiter);
        final Spliterator<String> splt = Spliterators.spliterator(scanner,
                Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.stream(splt, false).onClose(scanner::close);
    }

    static public Stream<String> delimitedStream(String string,
            String delimiter) {
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(delimiter);
        final Spliterator<String> splt = Spliterators.spliterator(scanner,
                Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.stream(splt, false).onClose(scanner::close);
    }

    public static List<String> parseToGroups(Pattern p, String s) {
        Matcher matcher = p.matcher(s);
        if (!matcher.matches())
            throw new RuntimeException("Can't parse " + s + " with " + p);
        ArrayList<String> groups = new ArrayList<>();
        for (int i = 1; i < matcher.groupCount() + 1; ++i)
            groups.add(matcher.group(i));
        return groups;
    }

    public static <T> List<T> parseAndCollectForDay(int day,
            Function<String, T> parser) {
        return inputLinesForDay(day).stream().map(parser)
                .collect(Collectors.toList());
    }
}
