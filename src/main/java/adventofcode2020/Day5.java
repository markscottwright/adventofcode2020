package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Day5 {
    static public class Seat {

        final int row;
        final int column;

        public Seat(int row, int column) {
            this.row = row;
            this.column = column;
        }

        int getSeatId() {
            return row * 8 + column;
        }

        public static Seat parse(String line) {
            String rowDirections = line.substring(0, 7);
            String columnDirections = line.substring(7);
            int row = 0;
            for (char direction : rowDirections.toCharArray()) {
                row <<= 1;
                if (direction == 'B')
                    row += 1;
            }
            int column = 0;
            for (char direction : columnDirections.toCharArray()) {
                column <<= 1;
                if (direction == 'R')
                    column += 1;
            }

            return new Seat(row, column);
        }

    }

    public static void main(String[] args) throws IOException {
        var seats = new ArrayList<Seat>();
        Files.readAllLines(Paths.get("src/main/resources/day5.txt")).stream()
                .map(Seat::parse).forEach(seats::add);
        seats.sort((s1, s2) -> Integer.compare(s1.getSeatId(), s2.getSeatId()));

        int maxSeatId = seats.get(seats.size() - 1).getSeatId();

        System.out.println("Day 5 part 1:" + maxSeatId);

        int mySeatId = 0;
        for (int i = 1; i < seats.size(); i++) {
            if (seats.get(i).getSeatId() - seats.get(i - 1).getSeatId() == 2) {
                mySeatId = seats.get(i).getSeatId() - 1;
                break;
            }
        }
        System.out.println("Day 5 part 2:" + mySeatId);
    }
}
