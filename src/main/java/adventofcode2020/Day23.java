package adventofcode2020;

import java.util.Arrays;
import java.util.HashMap;

public class Day23 {

    static public class Cup {
        public Cup(long integer) {
            this.label = integer;
        }

        long label;
        Cup next;
    };

    /**
     * Keep a linked list of cups for easy re-ordering, and a hashtable for easy
     * searching. A poor mans B+ tree.
     */
    static public class Cups {
        HashMap<Long, Cup> cups = new HashMap<>();
        Cup current;
        private int largestLabel;

        public Cups(Integer... labels) {
            current = new Cup(labels[0]);
            cups.put((long) labels[0], current);
            Cup c = current;
            largestLabel = 0;
            for (int i = 1; i < labels.length; ++i) {
                c.next = new Cup(labels[i]);
                c = c.next;
                cups.put(c.label, c);
            }
            c.next = current;
            largestLabel = Arrays.stream(labels).mapToInt(i -> i).max()
                    .getAsInt();
        }

        @Override
        public String toString() {
            String out = "cups: (" + current.label + ")";
            int num = 0;
            Cup c = current.next;
            while (c != current && num++ < 100) {
                out += " " + c.label;
                c = c.next;
            }
            return out;
        }

        void log(String s) {
            System.out.println(s);
        }

        void playRound() {
            // log(this.toString());
            Cup c1 = removeAfter(current);
            Cup c2 = removeAfter(current);
            Cup c3 = removeAfter(current);
            // log("pick up: " + c1.label + ", " + c2.label + ", " + c3.label);
            long destination = decrementLabel(current.label);
            while (destination == c1.label || destination == c2.label
                    || destination == c3.label)
                destination = decrementLabel(destination);
            // log("destination: " + destination);
            insertAfter(destination, c1, c2, c3);
            current = current.next;
        }

        private void insertAfter(long destinationLabel, Cup c1, Cup c2,
                Cup c3) {
            Cup destinationCup = cups.get(destinationLabel);
            c1.next = c2;
            c2.next = c3;
            c3.next = destinationCup.next;
            destinationCup.next = c1;
        }

        private long decrementLabel(long label) {
            label--;
            if (label < 1)
                return largestLabel;
            return label;
        }

        private Cup removeAfter(Cup c) {
            Cup out = c.next;
            c.next = c.next.next;
            out.next = null;
            return out;
        }

        public String labelsAfter1() {
            Cup cupLabeled1 = cups.get(1L);
            String out = "";
            Cup c = cupLabeled1;
            c = c.next;
            do {
                out += c.label;
                c = c.next;
            } while (c != cupLabeled1);
            return out;
        }

        /**
         * Create a Cups with labels, then extend up to 1_000_000, maintaining
         * linked list and hashtable.
         */
        public static Cups toOneMillionStartingWith(Integer... labels) {
            Cups cups = new Cups(labels);
            Cup lastCup = cups.cups.get((long) labels[labels.length - 1]);
            for (long label = cups.largestLabel
                    + 1; label <= 1_000_000; ++label) {
                // create the next cup and append it to the end
                Cup c = new Cup(label);
                c.next = lastCup.next;
                lastCup.next = c;
                lastCup = c;
                cups.cups.put(label, c);
            }
            cups.largestLabel = 1_000_000;
            return cups;
        }

    }

    public static void main(String[] strings) {
        Cups cups = new Cups(1, 9, 3, 4, 6, 7, 2, 5, 8);
        for (int i = 0; i < 100; i++) {
            cups.playRound();
        }
        System.out.println("Day 23 part 1: " + cups.labelsAfter1());

        Cups cupsPart2 = Cups.toOneMillionStartingWith(1, 9, 3, 4, 6, 7, 2, 5,
                8);
        for (int i = 0; i < 10_000_000; i++) {
            cupsPart2.playRound();
        }
        Cup cupAfter1 = cupsPart2.cups.get(1L).next;
        System.out.println(
                "Day 23 part 1: " + (cupAfter1.label * cupAfter1.next.label));
    }

}
