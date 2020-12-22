package adventofcode2020;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

public class Day21Test {

    @Test
    public void test() {
        String input = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n"
                + "trh fvjkl sbzzf mxmxvkd (contains dairy)\n"
                + "sqjhc fvjkl (contains soy)\n"
                + "sqjhc mxmxvkd sbzzf (contains fish)";
        var foods = Arrays.stream(input.split("\n")).map(Day21.Food::parse)
                .collect(Collectors.toList());
        var allergenToIngredients = Day21.assignIngredientToAllergen(foods);
        var part1SampleAnswer = Day21.numTimesNonAllergenicIngredientsUsed(
                foods, allergenToIngredients);
        assertThat(part1SampleAnswer).isEqualTo(5);
    }

}
