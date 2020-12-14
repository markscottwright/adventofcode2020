package adventofcode2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) throws FileNotFoundException {
        List<Long> busses = Util
                .delimitedStream(new File("src/main/resources/day13.txt"),
                        "[\\n,]")
                .filter(s -> !s.equals("x")).map(Long::parseLong)
                .collect(Collectors.toList());
        long firstDepartureTime = busses.remove(0);
        long busId;
        long departureTime = firstDepartureTime;
        outer: while (true) {
            for (var id : busses) {
                if (departureTime % id == 0) {
                    busId = id;
                    break outer;
                }
                departureTime++;
            }
        }

        System.out.println("Day 13 part 1: "
                + (departureTime - firstDepartureTime) * busId);

        // parse into a map from busId to its departure offset from time 0
        var departures = Util.inputLinesForDay(13).get(1).split(",");
        TreeMap<Long, Integer> busToOffset = new TreeMap<>();
        for (int i = 0; i < departures.length; i++) {
            if (departures[i].equals("x"))
                continue;

            busToOffset.put(Long.parseLong(departures[i]), i);
        }
        long part2 = partTwo(busToOffset);
        System.out.println("Day 13 part 2: " + part2);
    }

    private static long partTwo(TreeMap<Long, Integer> busToOffset) {
        // keep incrementing time by an offset equal to all of the busId that
        // work for the current time multiplied together. At each interval, see
        // if one of the remaining bus id's works for the current time. If it
        // does, start incrementing by the old increment * bus id.
        long busIdAtOffset0 = busToOffset.keySet().stream()
                .filter(busId -> busToOffset.get(busId) == 0).findFirst().get();
        long timeIncrement = busIdAtOffset0;
        Set<Long> remainingBusIds = new HashSet<>(busToOffset.keySet());
        remainingBusIds.remove(busIdAtOffset0);

        long time = timeIncrement * 2;
        while (true) {
            for (var busId : new HashSet<>(remainingBusIds)) {
                if ((time + busToOffset.get(busId)) % busId == 0) {
                    // current time is a valid interval for busId. Time can now
                    // increment by a multiplier of this busId
                    timeIncrement *= busId;
                    remainingBusIds.remove(busId);
                }
            }
            if (remainingBusIds.isEmpty())
                return time;
            time += timeIncrement;
        }
    }

}
