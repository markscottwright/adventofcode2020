package adventofcode2020;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import adventofcode2020.Day20.FixedTile;
import adventofcode2020.Day20.MonochromeBitmap;
import adventofcode2020.Day20.Position;
import adventofcode2020.Day20.Tile;

public class Day20Test {

    @Test
    public void test() {
        //@formatter:off
        String input = 
                "Tile 2311:\n" + 
                "..##.#..#.\n" + 
                "##..#.....\n" + 
                "#...##..#.\n" + 
                "####.#...#\n" + 
                "##.##.###.\n" + 
                "##...#.###\n" + 
                ".#.#.#..##\n" + 
                "..#....#..\n" + 
                "###...#.#.\n" + 
                "..###..###\n" + 
                "\n" + 
                "Tile 1951:\n" + 
                "#.##...##.\n" + 
                "#.####...#\n" + 
                ".....#..##\n" + 
                "#...######\n" + 
                ".##.#....#\n" + 
                ".###.#####\n" + 
                "###.##.##.\n" + 
                ".###....#.\n" + 
                "..#.#..#.#\n" + 
                "#...##.#..\n" + 
                "\n" + 
                "Tile 1171:\n" + 
                "####...##.\n" + 
                "#..##.#..#\n" + 
                "##.#..#.#.\n" + 
                ".###.####.\n" + 
                "..###.####\n" + 
                ".##....##.\n" + 
                ".#...####.\n" + 
                "#.##.####.\n" + 
                "####..#...\n" + 
                ".....##...\n" + 
                "\n" + 
                "Tile 1427:\n" + 
                "###.##.#..\n" + 
                ".#..#.##..\n" + 
                ".#.##.#..#\n" + 
                "#.#.#.##.#\n" + 
                "....#...##\n" + 
                "...##..##.\n" + 
                "...#.#####\n" + 
                ".#.####.#.\n" + 
                "..#..###.#\n" + 
                "..##.#..#.\n" + 
                "\n" + 
                "Tile 1489:\n" + 
                "##.#.#....\n" + 
                "..##...#..\n" + 
                ".##..##...\n" + 
                "..#...#...\n" + 
                "#####...#.\n" + 
                "#..#.#.#.#\n" + 
                "...#.#.#..\n" + 
                "##.#...##.\n" + 
                "..##.##.##\n" + 
                "###.##.#..\n" + 
                "\n" + 
                "Tile 2473:\n" + 
                "#....####.\n" + 
                "#..#.##...\n" + 
                "#.##..#...\n" + 
                "######.#.#\n" + 
                ".#...#.#.#\n" + 
                ".#########\n" + 
                ".###.#..#.\n" + 
                "########.#\n" + 
                "##...##.#.\n" + 
                "..###.#.#.\n" + 
                "\n" + 
                "Tile 2971:\n" + 
                "..#.#....#\n" + 
                "#...###...\n" + 
                "#.#.###...\n" + 
                "##.##..#..\n" + 
                ".#####..##\n" + 
                ".#..####.#\n" + 
                "#..#.#..#.\n" + 
                "..####.###\n" + 
                "..#.#.###.\n" + 
                "...#.#.#.#\n" + 
                "\n" + 
                "Tile 2729:\n" + 
                "...#.#.#.#\n" + 
                "####.#....\n" + 
                "..#.#.....\n" + 
                "....#..#.#\n" + 
                ".##..##.#.\n" + 
                ".#.####...\n" + 
                "####.#.#..\n" + 
                "##.####...\n" + 
                "##..#.##..\n" + 
                "#.##...##.\n" + 
                "\n" + 
                "Tile 3079:\n" + 
                "#.#.#####.\n" + 
                ".#..######\n" + 
                "..#.......\n" + 
                "######....\n" + 
                "####.#..#.\n" + 
                ".#...#.##.\n" + 
                "#.#####.##\n" + 
                "..#.###...\n" + 
                "..#.......\n" + 
                "..#.###...";
        //@formatter:on

        Map<Long, Tile> tiles = Arrays.stream(input.split("\n\n"))
                .map(Day20.Tile::parse)
                .collect(Collectors.toMap(t -> t.tileId, t -> t));

        // part 1
        int mapSideLength = (int) Math.sqrt(tiles.keySet().size());
        HashMap<Position, FixedTile> solution = new HashMap<>();
        boolean worked = Day20.placeTiles(tiles, solution, tiles.keySet(),
                mapSideLength);
        assert worked;

        FixedTile nwTile = solution.get(new Position(0, 0));
        FixedTile neTile = solution.get(new Position(0, mapSideLength - 1));
        FixedTile swTile = solution.get(new Position(mapSideLength - 1, 0));
        FixedTile seTile = solution
                .get(new Position(mapSideLength - 1, mapSideLength - 1));
        assertThat(
                nwTile.tileId * neTile.tileId * swTile.tileId * seTile.tileId)
                        .isEqualTo(20899048083289L);

        // part 2
        var seaMonster = new MonochromeBitmap(Day20.getSeaMonster());
        boolean[][] map = Day20.combine(solution);
        int numSeaMonsters = 0;
        for (var m : Day20.buildAllTransformations(map)) {
            numSeaMonsters = seaMonster.countInstances(m);
            if (numSeaMonsters > 0) {
                break;
            }
        }
        assertThat(numSeaMonsters).isEqualTo(2);
    }

}
