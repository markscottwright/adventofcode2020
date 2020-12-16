package adventofcode2020;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {
    static class Rule {
        static Pattern RULE_PATTERN = Pattern
                .compile("([ a-z]+): ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)");
        private String name;
        private int range1Start;
        private int range1End;
        private int range2Start;
        private int range2End;

        public Rule(String name, int range1Start, int range1End,
                int range2Start, int range2End) {
            this.name = name;
            this.range1Start = range1Start;
            this.range1End = range1End;
            this.range2Start = range2Start;
            this.range2End = range2End;
        }

        static boolean anyRuleMatchesNumber(Collection<Rule> rules,
                int number) {
            return rules.stream().anyMatch(r -> r.matches(number));
        }

        boolean matches(int number) {
            return range1Start <= number && number <= range1End
                    || range2Start <= number && number <= range2End;
        }

        static Rule parse(String l) {
            Matcher matcher = RULE_PATTERN.matcher(l);
            matcher.matches();
            return new Rule(matcher.group(1), parseInt(matcher.group(2)),
                    parseInt(matcher.group(3)), parseInt(matcher.group(4)),
                    parseInt(matcher.group(5)));
        }

        @Override
        public String toString() {
            return name + ": " + range1Start + "-" + range1End + " or "
                    + range2Start + "-" + range2End;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Rule other = (Rule) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }

    static class TicketFields {
        private List<Integer> fields;

        public TicketFields(List<Integer> fields) {
            this.fields = fields;
        }

        static TicketFields parse(String l) {
            return new TicketFields(Arrays.stream(l.split(","))
                    .map(Integer::parseInt).collect(Collectors.toList()));
        }

        @Override
        public String toString() {
            return "TicketFields [fields=" + fields + "]";
        }

        List<Integer> nonMatchingFields(Collection<Rule> rules) {
            return fields.stream()
                    .filter(f -> !Rule.anyRuleMatchesNumber(rules, f))
                    .collect(Collectors.toList());
        }
    }

    public static void main(String[] strings) {

        // parse input file
        ArrayList<Rule> rules = new ArrayList<>();
        ArrayList<TicketFields> nearbyTickets = new ArrayList<>();
        TicketFields myTicket = null;
        int inputSection = 1;
        for (String l : Util.inputLinesForDay(16)) {
            if (l.contains("your ticket") || l.contains("nearby ticket"))
                inputSection++;
            else if (l.isEmpty())
                continue;
            else if (inputSection == 1) {
                rules.add(Rule.parse(l));
            } else if (inputSection == 2) {
                myTicket = TicketFields.parse(l);
            } else if (inputSection == 3)
                nearbyTickets.add(TicketFields.parse(l));
        }

        // find valid tickets (And part 1 solution)
        ArrayList<TicketFields> validNearbyTickets = new ArrayList<>();
        int nonMatchingSum = 0;
        for (TicketFields ticketFields : nearbyTickets) {
            List<Integer> nonMatchingFields = ticketFields
                    .nonMatchingFields(rules);
            nonMatchingSum += Util.sum(nonMatchingFields);
            if (nonMatchingFields.isEmpty())
                validNearbyTickets.add(ticketFields);
        }
        System.out.print("Day 16 part 1: ");
        System.out.println(nonMatchingSum);

        // to start with, a rule could be anywhere
        ArrayList<HashSet<Rule>> possibleRulesPerPosition = new ArrayList<>();
        for (int i = 0; i < myTicket.fields.size(); ++i) {
            possibleRulesPerPosition.add(new HashSet<Rule>(rules));
        }

        // determine which rules can't apply to a position
        for (TicketFields ticket : validNearbyTickets) {
            for (int fieldIndex = 0; fieldIndex < ticket.fields
                    .size(); fieldIndex++) {
                int fieldValue = ticket.fields.get(fieldIndex);
                possibleRulesPerPosition.get(fieldIndex)
                        .removeIf(rule -> !rule.matches(fieldValue));
            }
        }

        // some positions have more than one possible rule, find unique
        // positions
        List<Rule> rulesPerPosition = destructivelyFindUniqueAssignments(
                possibleRulesPerPosition);

        // find all my "departure" fields and multiply them together
        long part2 = 1;
        for (int i = 0; i < rulesPerPosition.size(); ++i)
            if (rulesPerPosition.get(i).name.startsWith("departure "))
                part2 *= myTicket.fields.get(i);

        System.out.println("Day 16 part 2: " + part2);
    }

    /**
     * Assign each rule to one and only one position. If this can't be
     * determined, this will never exit, but presumably that means the puzzle is
     * unsolvable.
     * 
     * @return
     */
    private static List<Rule> destructivelyFindUniqueAssignments(
            ArrayList<HashSet<Rule>> positionToRules) {
        HashSet<Rule> assignedRules = new HashSet<>();
        while (assignedRules.size() != positionToRules.size()) {
            for (int position = 0; position < positionToRules
                    .size(); ++position) {
                HashSet<Rule> rulesForThisPosition = positionToRules
                        .get(position);
                if (rulesForThisPosition.size() == 1) {
                    assignedRules.add(rulesForThisPosition.iterator().next());
                } else {
                    rulesForThisPosition.removeAll(assignedRules);
                }
            }
        }

        return positionToRules.stream().map(rules -> rules.iterator().next())
                .collect(Collectors.toList());
    }

}
