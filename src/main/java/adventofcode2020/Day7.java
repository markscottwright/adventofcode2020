package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Use TreeMap/TreeSet everywhere for nice println-ing.
 */
public class Day7 {
    /**
     * A bag and what bags it must contain
     */
    static public class BagRule {
        final String container;
        final Map<String, Integer> contents;

        public BagRule(String container, Map<String, Integer> contents) {
            this.container = container;
            this.contents = contents;
        }

        /**
         * Parse a line like: "plaid tan bags contain 4 light coral bags, 2 dim
         * fuchsia bags, 3 mirrored coral bags."
         * 
         * Into a rule likes: "plaid tan" : {"light coral" : 4, "dim fuchsia":
         * 2, "mirrored coral": 3}
         */
        static BagRule parse(String s) {
            String[] containerAndContents = s.split("contain");
            String container = containerAndContents[0].replaceAll("bags", "")
                    .strip();

            TreeMap<String, Integer> contents = new TreeMap<>();
            for (String contentsString : containerAndContents[1].split(",")) {
                contentsString = contentsString.replaceAll("\\.", "")
                        .replaceAll(" bags", "").replaceAll(" bag", "").strip();
                if (!contentsString.equals("no other")) {
                    String[] countAndBag = contentsString.split(" ", 2);
                    contents.put(countAndBag[1].strip(),
                            Integer.parseInt(countAndBag[0]));
                }
            }
            return new BagRule(container, contents);
        }
    }

    /**
     * Given a set of rules of bag to the bags it contains, create the reversed
     * map of bag to which bags contain it.
     */
    static Map<String, TreeSet<String>> createContainedByMap(
            Collection<BagRule> rules) {
        TreeMap<String, TreeSet<String>> containedByRules = new TreeMap<>();
        rules.forEach(r -> {
            for (String contentsBagType : r.contents.keySet()) {
                if (!containedByRules.containsKey(contentsBagType))
                    containedByRules.put(contentsBagType, new TreeSet<>());
                containedByRules.get(contentsBagType).add(r.container);
            }
        });
        return containedByRules;
    }

    /**
     * Precondition: no cycles in containedByMap
     */
    static void collectAllContainers(TreeSet<String> foundContainers,
            Map<String, TreeSet<String>> containedByMap, String bagType) {
        TreeSet<String> containers = containedByMap.get(bagType);

        if (containers != null) {
            for (String container : containers) {
                foundContainers.add(container);
                collectAllContainers(foundContainers, containedByMap,
                        container);
            }
        }
    }

    /**
     * Precondition: no cycles in containedByMap
     */
    static TreeSet<String> collectAllContainers(
            Map<String, TreeSet<String>> containedByMap, String bagType) {
        TreeSet<String> foundContainers = new TreeSet<>();
        collectAllContainers(foundContainers, containedByMap, bagType);
        return foundContainers;
    }

    /**
     * Using the bag rules collection, recursively count all the bags in 1 of
     * bagName.
     */
    private static int countBagsIn(TreeMap<String, BagRule> bagToRule,
            String bagName) {
        int count = 0;
        for (Entry<String, Integer> containedBag : bagToRule
                .get(bagName).contents.entrySet()) {
            count += containedBag.getValue();
            count += containedBag.getValue()
                    * countBagsIn(bagToRule, containedBag.getKey());
        }
        return count;
    }

    private static TreeMap<String, BagRule> buildRuleMap(
            List<BagRule> bagRules) {
        TreeMap<String, BagRule> bagToRule = new TreeMap<>();
        for (BagRule bagRule : bagRules) {
            bagToRule.put(bagRule.container, bagRule);
        }
        return bagToRule;
    }

    public static void main(String[] args) throws IOException {
        var bagRules = Files
                .readAllLines(Paths.get("src/main/resources/day7.txt")).stream()
                .map(BagRule::parse).collect(Collectors.toList());
        
        var containedByMap = createContainedByMap(bagRules);
        System.out.println("Day 7 part 1:"
                + collectAllContainers(containedByMap, "shiny gold").size());

        var bagToRule = buildRuleMap(bagRules);
        int bagsInShinyGold = countBagsIn(bagToRule, "shiny gold");
        System.out.println("Day 7 part 2:" + bagsInShinyGold);
    }
}
