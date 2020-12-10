package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;

public class Day10 {
    public static void main(String[] args) throws IOException {

        int maxJoltage = 0;
        var adapterJoltages = new TreeSet<Integer>();
        for (String line : Files
                .readAllLines(Paths.get("src/main/resources/day10.txt"))) {
            int adapterJoltage = Integer.parseInt(line);
            adapterJoltages.add(adapterJoltage);
            maxJoltage = Math.max(maxJoltage, adapterJoltage);
        }

        var adapterChain = new ArrayList<Integer>();
        adapterChain.add(0);
        createChain(adapterChain, maxJoltage + 3, adapterJoltages);

        int part1Answer = numOneDifferencesTimesNumThreeDifferences(
                adapterChain);
        System.out.println("Day 10 part 1: " + part1Answer);

        long totalWaysToArrange = numWaysToArrange(adapterChain);
        System.out.println("Day 10 part 2: " + totalWaysToArrange);
    }

    private static long numWaysToArrange(ArrayList<Integer> adapterChain) {
        // partition solution into groups separated by 3
        ArrayList<TreeSet<Integer>> partitoned = new ArrayList<>();
        partitoned.add(new TreeSet<>());
        partitoned.get(partitoned.size() - 1).add(adapterChain.get(0));
        for (int i = 0; i < adapterChain.size() - 1; i++) {
            if (adapterChain.get(i + 1) - adapterChain.get(i) == 3)
                partitoned.add(new TreeSet<>());
            partitoned.get(partitoned.size() - 1).add(adapterChain.get(i + 1));
        }
        // partitoned.forEach(System.out::println);

        // the inner elements in a joltage group can be re-arranged. The first
        // and last can't since they're already three away from the next group,
        // so no other joltage in their group can take their place. So the
        // number of combinations is (#permutations of group 0) * (#permutations
        // of group 1)* ....
        long totalWaysToArrange = 1;
        for (var group : partitoned) {
            totalWaysToArrange *= waysToArrangeGroup(group);
        }
        return totalWaysToArrange;
    }

    private static int numOneDifferencesTimesNumThreeDifferences(
            ArrayList<Integer> adapterChain) {
        int oneDifferences = 0;
        int threeDifferences = 1; // always one for connmection to device
        for (int i = 0; i < adapterChain.size() - 1; i++) {
            if (adapterChain.get(i + 1) - adapterChain.get(i) == 1)
                oneDifferences++;
            else
                threeDifferences++;
        }
        // System.out.println(
        // String.format("%d one differences, %d three differences",
        // oneDifferences, threeDifferences));
        int part1Answer = oneDifferences * threeDifferences;
        return part1Answer;
    }

    /**
     * How many combinations of inner elements (excluding first and last) are
     * there? Assuming group.size() <= 5, this is numCombinations(size-2). If
     * group.size() > 5, this doesn't work, since it's possible to permute in a
     * way such that there will be a >3 difference between elements.
     * 
     * The puzzle counts "ways to arrange" differently than you'd think. It's
     * not permutations (look at the examples) - the puzzle doesn't care about
     * order.
     * 
     * It's the number of ways you can shorten the group.
     * 
     * <pre>
     * So, 1,2,3,4,5: 
     * 1,2,3,4,5 
     * 1,2,3,5 
     * 1,2,4,5
     * 1,2,5
     * 1,3,4,5
     * 1,3,5
     * 1,4,5 (7)
     * 
     * And 1,2,3,4:
     * 1,2,3,4 
     * 1,2,4 
     * 1,3,4 
     * 1,4 (4)
     * 
     * And 1,2,3: 
     * 1,2,3 
     * 1,3 (2)
     * </pre>
     */
    private static long waysToArrangeGroup(TreeSet<Integer> group) {
        switch (group.size()) {
        case 1:
        case 2:
            return 1;
        case 3:
            return 2;
        case 4:
            return 4;
        case 5:
            return 7;
        default:
            throw new RuntimeException("Naieve waysToArrangeGroup failed");
        }
    }

    private static boolean createChain(ArrayList<Integer> adapterChain,
            int finalJoltage, TreeSet<Integer> remainingJoltages) {
        if (remainingJoltages.size() == 0)
            return true;

        int terminalJoltage = adapterChain.get(adapterChain.size() - 1);
        var joltagesInRange = allJoltagesWithinThreeOf(remainingJoltages,
                terminalJoltage);

        for (int j : joltagesInRange) {
            adapterChain.add(j);
            remainingJoltages.remove(j);
            if (createChain(adapterChain, finalJoltage, remainingJoltages))
                return true;
            adapterChain.remove(adapterChain.size() - 1);
            remainingJoltages.add(j);
        }

        return false;
    }

    private static ArrayList<Integer> allJoltagesWithinThreeOf(
            TreeSet<Integer> remainingJoltages, int terminalJoltage) {
        var joltagesInRange = new ArrayList<Integer>();
        var remainingJoltagesIterator = remainingJoltages.iterator();
        while (remainingJoltagesIterator.hasNext()) {
            var j = remainingJoltagesIterator.next();
            if (Math.abs(terminalJoltage - j) <= 3)
                joltagesInRange.add(j);
            else
                break;
        }
        return joltagesInRange;
    }
}