package adventofcode2020;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Day21 {

    /**
     * A Food is a collections of ingredients and allergens
     */
    static class Food {
        private HashSet<String> ingredients;
        private HashSet<String> allergens;

        public Food(HashSet<String> ingredients, HashSet<String> allergens) {
            this.ingredients = ingredients;
            this.allergens = allergens;
        }

        static Food parse(String l) {
            HashSet<String> allergens = new HashSet<>();
            HashSet<String> ingredients = new HashSet<>();
            boolean inIngredients = true;
            for (String i : l.split(" ")) {
                if (i.startsWith("(contains"))
                    inIngredients = false;
                else if (inIngredients)
                    ingredients.add(i.strip());
                else
                    allergens.add(i.replaceAll("[),]", "").strip());
            }
            return new Food(ingredients, allergens);
        }

        @Override
        public String toString() {
            return "Food [ingredients=" + ingredients + ", allergens="
                    + allergens + "]";
        }
    }

    /**
     * Given a collection of foods, return a mapping from allergen to all
     * possible ingredients that contain that allergen.
     */
    static Map<String, HashSet<String>> assignIngredientToAllergen(
            Collection<Food> foods) {
        HashMap<String, HashSet<Food>> allergenToFood = new HashMap<>();
        for (Food food : foods) {
            for (String allergen : food.allergens) {
                if (!allergenToFood.containsKey(allergen))
                    allergenToFood.put(allergen, new HashSet<>());
                allergenToFood.get(allergen).add(food);
            }
        }
        TreeMap<String, HashSet<String>> allergenToIngredients = new TreeMap<>();
        for (String allergen : allergenToFood.keySet()) {
            var ingredientsAllergen = allergenToFood.get(allergen).stream()
                    .map(f -> f.ingredients)
                    .reduce((i1, i2) -> Util.intersection(i1, i2)).get();
            allergenToIngredients.put(allergen, ingredientsAllergen);
        }

        return allergenToIngredients;
    }

    public static void main(String[] strings) {
        var foods = Util.parseAndCollectForDay(21, Food::parse);

        var allergenToIngredients = assignIngredientToAllergen(foods);
        int part1 = numTimesNonAllergenicIngredientsUsed(foods,
                allergenToIngredients);
        System.out.println("Day 21 part 1: " + part1);

        var uniqueAssignments = findUniqueAssignments(allergenToIngredients);
        System.out.println("Day 21 part 2: "
                + String.join(",", uniqueAssignments.values()));

    }

    /**
     * Given a map from allergen to all ingredients that could contain those
     * allergens, reduce it to a set of unique assignments
     */
    public static TreeMap<String, String> findUniqueAssignments(
            Map<String, HashSet<String>> allergenToIngredients) {
        TreeMap<String, String> uniqueAssignments = new TreeMap<>();

        while (uniqueAssignments.size() < allergenToIngredients.keySet()
                .size()) {
            for (String allergen : allergenToIngredients.keySet()) {
                if (uniqueAssignments.containsKey(allergen))
                    continue;
                else if (allergenToIngredients.get(allergen).size() == 1)
                    uniqueAssignments.put(allergen, allergenToIngredients
                            .get(allergen).iterator().next());
                else
                    allergenToIngredients.get(allergen)
                            .removeAll(uniqueAssignments.values());
            }
        }

        return uniqueAssignments;
    }

    /**
     * Count all ingredients in foods that don't appear in the list of possible
     * allergens.
     */
    public static int numTimesNonAllergenicIngredientsUsed(List<Food> foods,
            Map<String, HashSet<String>> allergenToIngredients) {
        TreeSet<String> maybeAllergenicIngredients = new TreeSet<>();
        allergenToIngredients.values()
                .forEach(maybeAllergenicIngredients::addAll);

        // we could count these instead of putting them in a collection, but I
        // wanted to see usages
        Multiset<String> nonAllergenicIngredients = HashMultiset.create();
        foods.stream().flatMap(f -> f.ingredients.stream())
                .filter(i -> !maybeAllergenicIngredients.contains(i))
                .forEach(nonAllergenicIngredients::add);
        return nonAllergenicIngredients.size();
    }

}
