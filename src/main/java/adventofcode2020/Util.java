package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

}
