package adventofcode2020;

import java.util.HashMap;

public class Day15 {

    public static void main(String[] strings) {
        var initialNumbers = new int[] { 2, 1, 10, 11, 0, 6 };
        int part1 = runGame(initialNumbers, 2020);
        System.out.println("Day 15 part 1: " + part1);
        int part2 = runGame(initialNumbers, 30_000_000);
        System.out.println("Day 15 part 2: " + part2);
    }

    // brute force seems to work? Maybe they were expecting a backwards linear
    // search over an array of values as the naive solution?
    private static int runGame(int[] initialNumbers, int numTurns) {
        HashMap<Integer, Integer> numberToTurn = new HashMap<>();
        int turn;
        for (turn = 0; turn < initialNumbers.length - 1; ++turn) {
            numberToTurn.put(initialNumbers[turn], turn);
        }

        int lastNumber = initialNumbers[initialNumbers.length - 1];
        while (true) {
            int nextNumber;
            if (numberToTurn.containsKey(lastNumber))
                nextNumber = turn - numberToTurn.get(lastNumber);
            else
                nextNumber = 0;
            numberToTurn.put(lastNumber, turn);
            if (turn + 1 == numTurns) {
                return lastNumber;
            }
            lastNumber = nextNumber;
            ++turn;
        }
    }

}
