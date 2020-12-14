package adventofcode2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {

    public static void fail(String message) throws RuntimeException {
        throw new RuntimeException(message);
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

}
