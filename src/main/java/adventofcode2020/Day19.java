package adventofcode2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Day19 {

    static public class Rule {
        private String name;
        private ArrayList<ArrayList<String>> possibleSubRules;

        public Rule(String name,
                ArrayList<ArrayList<String>> possibleSubRules) {
            this.name = name;
            this.possibleSubRules = possibleSubRules;
        }

        String toPatternString(Map<String, Rule> otherRules) {
            if (possibleSubRules.size() == 1
                    && possibleSubRules.get(0).get(0).charAt(0) == '"')
                return possibleSubRules.get(0).get(0).substring(1, 2);

            String out = "(";
            for (int i = 0; i < possibleSubRules.size(); ++i) {
                if (i != 0)
                    out += "|";
                out += ruleListToRegex(otherRules, possibleSubRules.get(i));
            }
            return out + ")";
        }

        private String ruleListToRegex(Map<String, Rule> otherRules,
                ArrayList<String> firstSubRule) {
            String out = "";
            for (String subRuleName : firstSubRule) {
                out += otherRules.get(subRuleName).toPatternString(otherRules);
            }
            return out;
        }

        static Rule parse(String l) {
            String[] nameAndRule = l.split(":");
            String[] possibleSubRuleStrings = nameAndRule[1].strip()
                    .split("\\|");
            ArrayList<ArrayList<String>> possibleSubRules = new ArrayList<>();
            for (String possibleSubRuleString : possibleSubRuleStrings) {
                var possibleSubRule = new ArrayList<String>();
                for (String part : possibleSubRuleString.strip().split(" ")) {
                    possibleSubRule.add(part.strip());
                }
                possibleSubRules.add(possibleSubRule);
            }

            return new Rule(nameAndRule[0].strip(), possibleSubRules);
        }

        @Override
        public String toString() {
            return "Rule [name=" + name + ", possibleSubRules="
                    + possibleSubRules + "]";
        }
    }

    static class Rule8 extends Rule {

        public Rule8() {
            super("8", new ArrayList<>());
        }

        @Override
        String toPatternString(Map<String, Rule> otherRules) {
            return "(" + otherRules.get("42").toPatternString(otherRules)
                    + ")+";
        }

    }

    static class Rule11 extends Rule {

        private int largestMessage;

        public Rule11(int largestMessage) {
            super("11", new ArrayList<>());
            this.largestMessage = largestMessage;
        }

        @Override
        String toPatternString(Map<String, Rule> otherRules) {
            String rule42Pattern = "("
                    + otherRules.get("42").toPatternString(otherRules) + ")";
            String rule31Pattern = "("
                    + otherRules.get("31").toPatternString(otherRules) + ")";

            // this is looking for a balanced pattern of ab, aabb, aaabbb, etc.
            // I think that has to be recursive, which I believe is not possible
            // in regexes. But, since I know the largest possible message, I can
            // just do a[0-n/2]b[0-n/2] repetitions manually.
            StringBuffer out = new StringBuffer();
            out.append('(');
            for (int i = 1; i < largestMessage / 2; ++i) {
                if (i != 1)
                    out.append('|');
                out.append("(" + rule42Pattern.repeat(i)
                        + rule31Pattern.repeat(i) + ")");
            }
            out.append(')');
            return out.toString();
        }
    }

    public static void main(String[] strings) {
        Map<String, Rule> rules = new TreeMap<>();
        ArrayList<String> messages = new ArrayList<>();
        int largestMessageLen = Integer.MIN_VALUE;

        boolean inRules = true;
        for (String l : Util.inputLinesForDay(19)) {
            if (l.isEmpty())
                inRules = false;
            else if (inRules) {
                Rule r = Rule.parse(l);
                rules.put(r.name, r);
            } else {
                messages.add(l);
                largestMessageLen = Math.max(largestMessageLen, l.length());
            }
        }

        // use the rules to build a big regex and use it to match.
        Pattern p1 = Pattern.compile(rules.get("0").toPatternString(rules));
        long countPart1 = messages.stream().filter(m -> p1.matcher(m).matches())
                .count();
        System.out.println("Day 19 part 1: " + countPart1);

        rules.put("8", new Rule8());
        rules.put("11", new Rule11(largestMessageLen));
        Pattern p2 = Pattern.compile(rules.get("0").toPatternString(rules));
        long countPart2 = messages.stream().filter(m -> p2.matcher(m).matches())
                .count();
        System.out.println("Day 19 part 2: " + countPart2);
    }

}
