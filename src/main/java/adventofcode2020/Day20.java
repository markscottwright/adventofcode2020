package adventofcode2020;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day20 {
    public static class MonochromeBitmap {

        private HashSet<Position> setBits;
        private int height;
        private int width;

        public MonochromeBitmap(HashSet<Position> setBits) {
            this.setBits = setBits;
            this.height = setBits.stream().mapToInt(p -> p.y).max().getAsInt();
            this.width = setBits.stream().mapToInt(p -> p.x).max().getAsInt();
        }

        public int countInstances(boolean[][] map) {
            int count = 0;
            for (int y = 0; y < map.length - height; ++y) {
                for (int x = 0; x < map[y].length - width; ++x) {
                    if (isAt(x, y, map))
                        count++;
                }
            }
            return count;
        }

        private boolean isAt(int x, int y, boolean[][] map) {
            for (Position p : setBits)
                if ((y + p.y) >= map.length)
                    return false;
                else if ((x + p.x) >= map[y].length)
                    return false;
                else if (!map[y + p.y][x + p.x])
                    return false;
            return true;
        }

        Set<Position> allMatching(int x, int y, boolean[][] map) {
            HashSet<Position> matching = new HashSet<>();
            for (Position p : setBits)
                if ((y + p.y) >= map.length)
                    return new HashSet<>();
                else if ((x + p.x) >= map[y].length)
                    return new HashSet<>();
                else if (!map[y + p.y][x + p.x])
                    return new HashSet<>();
                else
                    matching.add(new Position(x + p.x, y + p.y));
            return matching;
        }

        public void unsetAllIn(boolean[][] map) {
            HashSet<Position> allMatchingInMap = new HashSet<>();
            for (int y = 0; y < map.length - height; ++y) {
                for (int x = 0; x < map[y].length - width; ++x) {
                    allMatchingInMap.addAll(allMatching(x, y, map));
                }
            }

            for (Position position : allMatchingInMap) {
                map[position.y][position.x] = false;
            }
        }
    }

    static class Position {
        final int x;
        final int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Position other = (Position) obj;
            return x == other.x && y == other.y;
        }
    }

    static public class FixedTile {

        long tileId;
        boolean[][] pixels;
        private boolean[] northEdge;
        private boolean[] eastEdge;
        private boolean[] westEdge;
        private boolean[] southEdge;

        public FixedTile(long tileId, boolean[][] pixels) {
            this.tileId = tileId;
            this.pixels = pixels;
            int n = pixels[0].length;
            northEdge = new boolean[n];
            eastEdge = new boolean[n];
            westEdge = new boolean[n];
            southEdge = new boolean[n];
            assert n == pixels.length;
            for (int i = 0; i < n; ++i) {
                northEdge[i] = pixels[0][i];
                southEdge[i] = pixels[n - 1][i];
                westEdge[i] = pixels[i][0];
                eastEdge[i] = pixels[i][n - 1];
            }
        }

        public boolean fits(Map<Position, FixedTile> placed,
                Position currentPosition) {
            var northTile = placed.get(
                    new Position(currentPosition.x, currentPosition.y - 1));
            var southTile = placed.get(
                    new Position(currentPosition.x, currentPosition.y + 1));
            var eastTile = placed.get(
                    new Position(currentPosition.x + 1, currentPosition.y));
            var westTile = placed.get(
                    new Position(currentPosition.x - 1, currentPosition.y));

            return (northTile == null
                    || Arrays.equals(northTile.southEdge, northEdge))
                    && (southTile == null
                            || Arrays.equals(southTile.northEdge, southEdge))
                    && (eastTile == null
                            || Arrays.equals(eastTile.westEdge, eastEdge))
                    && (westTile == null
                            || Arrays.equals(westTile.eastEdge, westEdge));
        }
    }

    static class Tile {
        public long tileId;
        private FixedTile rotated270;
        private FixedTile rotated180;
        private FixedTile rotated90;
        private FixedTile identity;
        private FixedTile flippedVertical;
        private FixedTile flippedHorizonal;
        private FixedTile flippedVertical90;
        private FixedTile flippedHorizonal90;

        public Tile(long tileId, boolean[][] pixels) {
            this.tileId = tileId;
            identity = new FixedTile(tileId, pixels);
            rotated90 = new FixedTile(tileId, Util.rotate90(pixels));
            rotated180 = new FixedTile(tileId, Util.rotate90(rotated90.pixels));
            rotated270 = new FixedTile(tileId,
                    Util.rotate90(rotated180.pixels));
            flippedVertical = new FixedTile(tileId, Util.flipVertical(pixels));
            flippedVertical90 = new FixedTile(tileId,
                    Util.flipVertical(rotated90.pixels));
            flippedHorizonal = new FixedTile(tileId,
                    Util.flipHorizontal(pixels));
            flippedHorizonal90 = new FixedTile(tileId,
                    Util.flipHorizontal(rotated90.pixels));
        }

        static Tile parse(String tileText) {
            String[] tileLines = tileText.split("\n");
            long tileId = Long.parseLong(tileLines[0]
                    .substring("tile ".length(), tileLines[0].length() - 1));

            boolean[][] pixels = new boolean[tileLines.length - 1][];
            for (int i = 1; i < tileLines.length; ++i) {
                pixels[i - 1] = new boolean[tileLines[i].length()];
                for (int j = 0; j < tileLines[i].length(); ++j)
                    pixels[i - 1][j] = tileLines[i].charAt(j) == '#';
            }

            return new Tile(tileId, pixels);
        }

        FixedTile[] rotations() {
            return new FixedTile[] { identity, rotated90, rotated180,
                    rotated270, flippedHorizonal, flippedVertical,
                    flippedVertical90, flippedHorizonal90 };
        }
    }

    public static HashSet<Position> getSeaMonster() {
        // @formatter:off
        String seaMonster = 
                "                  # \n" + 
                "#    ##    ##    ###\n" + 
                " #  #  #  #  #  #";
        // @formatter:on
        String[] seaMonsterLines = seaMonster.split("\n");
        HashSet<Position> seaMonsterBitMap = new HashSet<>();
        for (int y = 0; y < seaMonsterLines.length; ++y)
            for (int x = 0; x < seaMonsterLines[y].length(); ++x)
                if (seaMonsterLines[y].charAt(x) == '#')
                    seaMonsterBitMap.add(new Position(x, y));
        return seaMonsterBitMap;
    }

    public static void main(String[] strings) throws FileNotFoundException {
        Map<Long, Tile> tiles = Util
                .delimitedStream(Util.fileForDay(20), "\n\n").map(Tile::parse)
                .collect(Collectors.toMap(t -> t.tileId, t -> t));

        // part 1
        int mapSideLength = (int) Math.sqrt(tiles.keySet().size());
        HashMap<Position, FixedTile> solution = new HashMap<>();
        boolean worked = placeTiles(tiles, solution, tiles.keySet(),
                mapSideLength);
        assert worked;

        var nwTile = solution.get(new Position(0, 0));
        var neTile = solution.get(new Position(0, mapSideLength - 1));
        var swTile = solution.get(new Position(mapSideLength - 1, 0));
        var seTile = solution
                .get(new Position(mapSideLength - 1, mapSideLength - 1));
        System.out.println("Day 20 part 1: " + (nwTile.tileId * neTile.tileId
                * swTile.tileId * seTile.tileId));

        var seaMonster = new MonochromeBitmap(getSeaMonster());
        boolean[][] map = combine(solution);
        for (var m : buildAllTransformations(map)) {
            int numSeaMonsters = seaMonster.countInstances(m);
            if (numSeaMonsters > 0) {
                seaMonster.unsetAllIn(m);
                int part2 = Util.countTrue(m);
                System.out.println("Day 20 part 2: " + part2);
                break;
            }
        }
    }

    static List<boolean[][]> buildAllTransformations(boolean[][] map) {
        var m1 = map;
        var r90 = Util.rotate90(m1);
        var r180 = Util.rotate90(r90);
        var r270 = Util.rotate90(r180);
        var m5 = Util.flipHorizontal(m1);
        var m6 = Util.flipVertical(m1);
        var m7 = Util.flipHorizontal(r90);
        var m8 = Util.flipVertical(r90);
        var m9 = Util.flipHorizontal(r180);
        var m10 = Util.flipVertical(r180);
        List<boolean[][]> allMapTransformations = Arrays.asList(m1, r90, r180,
                r270, m5, m6, m7, m8, m9, m10);
        return allMapTransformations;
    }

    public static void printOn(PrintStream out, boolean[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                out.print(map[y][x] ? '#' : '.');
            }
            out.println();
        }
    }

    static boolean[][] combine(HashMap<Position, FixedTile> solution) {
        // assume square
        int maxTileOffset = solution.keySet().stream().mapToInt(p -> p.x).max()
                .getAsInt();

        // remove edges to get tile size
        int actualTileSize = solution.values().iterator().next().pixels.length
                - 2;
        int mapSide = (maxTileOffset + 1) * actualTileSize;
        boolean[][] map = new boolean[mapSide][];
        for (int y = 0; y < mapSide; ++y)
            map[y] = new boolean[mapSide];
        for (int y = 0; y <= maxTileOffset; ++y) {
            for (int x = 0; x <= maxTileOffset; ++x) {
                FixedTile tile = solution.get(new Position(x, y));
                for (int innerY = 0; innerY < actualTileSize; ++innerY) {
                    for (int innerX = 0; innerX < actualTileSize; ++innerX) {
                        // +1 to avoid first pixel which is an edge
                        map[y * actualTileSize + innerY][x * actualTileSize
                                + innerX] = tile.pixels[innerY + 1][innerX + 1];
                    }
                }
            }
        }

        return map;
    }

    static boolean placeTiles(Map<Long, Tile> allTiles,
            Map<Position, FixedTile> placed, Set<Long> tilesRemaining,
            int mapWidth) {
        if (placed.size() == allTiles.keySet().size())
            return true;

        int x = placed.size() / mapWidth;
        int y = placed.size() % mapWidth;
        Position currentPosition = new Position(x, y);
        var nextRemaining = new HashSet<>(tilesRemaining);
        for (Long tileId : tilesRemaining) {
            nextRemaining.remove(tileId);
            for (FixedTile fixed : allTiles.get(tileId).rotations()) {
                if (fixed.fits(placed, currentPosition)) {
                    placed.put(currentPosition, fixed);
                    if (placeTiles(allTiles, placed, nextRemaining, mapWidth))
                        return true;
                    placed.remove(currentPosition);
                }
            }
            nextRemaining.add(tileId);
        }

        return false;
    }

}
