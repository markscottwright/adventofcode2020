package adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day3 {
    /**
     * A 2d map of tree locations
     */
    static public class Trees {

        private boolean[][] treeAtLocation;

        public Trees(String fileName) throws IOException {
            var trees = Files.readAllLines(Paths.get(fileName));
            treeAtLocation = new boolean[trees.size()][];
            for (int i = 0; i < trees.size(); i++) {
                treeAtLocation[i] = new boolean[trees.get(i).length()];
                for (int j = 0; j < treeAtLocation[i].length; j++) {
                    treeAtLocation[i][j] = trees.get(i).charAt(j) == '#';
                }
            }
        }

        /**
         * Starting at top left corner, follow slope down and count trees at
         * each location. Tree lines repeat infinitely to the right.
         */
        long treesEncounteredFollowingSlope(int right, int down) {
            int x = 0;
            int y = 0;
            int count = 0;
            while (y < treeAtLocation.length) {
                count += treeAtLocation[y][x % treeAtLocation[y].length] ? 1
                        : 0;
                y += down;
                x += right;
            }
            return count;
        }
    }

    public static void main(String[] args) throws IOException {
        Trees trees = new Trees("src/main/resources/day3.txt");
        System.out.println(
                "Day 3 part 1:" + trees.treesEncounteredFollowingSlope(3, 1));
        System.out.println(
                "Day 3 part 2:" + (trees.treesEncounteredFollowingSlope(1, 1)
                        * trees.treesEncounteredFollowingSlope(3, 1)
                        * trees.treesEncounteredFollowingSlope(5, 1)
                        * trees.treesEncounteredFollowingSlope(7, 1)
                        * trees.treesEncounteredFollowingSlope(1, 2)));
    }
}
