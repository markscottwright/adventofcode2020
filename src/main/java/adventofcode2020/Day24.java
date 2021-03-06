package adventofcode2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Day24 {

    static public class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        /**
         * Since this is hexagonal, lateral movement increases position by 2, as
         * hexagons on the line above are in-between those on the current line.
         */
        Position move(String direction) {
            if (direction.equals("e"))
                return new Position(x + 2, y);
            else if (direction.equals("w"))
                return new Position(x - 2, y);
            else if (direction.equals("nw"))
                return new Position(x - 1, y - 1);
            else if (direction.equals("ne"))
                return new Position(x + 1, y - 1);
            else if (direction.equals("sw"))
                return new Position(x - 1, y + 1);
            else if (direction.equals("se"))
                return new Position(x + 1, y + 1);
            else
                throw new RuntimeException("bad direction:" + direction);
        }

        Collection<Position> neighbors() {
            return Arrays.asList(new Position(x + 2, y), new Position(x - 2, y),
                    new Position(x - 1, y - 1), new Position(x + 1, y - 1),
                    new Position(x - 1, y + 1), new Position(x + 1, y + 1));
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

    /**
     * parse smashed together e,w,ne,nw,se,sw string into array of strings.
     */
    static List<String> parseDirections(String l) {
        ArrayList<String> directions = new ArrayList<>();
        int i = 0;
        while (i < l.length()) {
            if (l.charAt(i) == 'e') {
                directions.add("e");
                i++;
            } else if (l.charAt(i) == 'w') {
                directions.add("w");
                i++;
            } else {
                directions.add(l.substring(i, i + 2));
                i += 2;
            }
        }
        return directions;
    }

    /**
     * Using the rules from the puzzle, transform tiles from day n into those
     * for day n+1.
     */
    static Set<Position> flipAtDaysEnd(Set<Position> blackTiles) {
        HashSet<Position> nextBlackTiles = new HashSet<>();
        // Any black tile with zero or more than 2 black tiles immediately
        // adjacent to it is flipped to white.
        for (Position blackTile : blackTiles) {
            long numNeighboringBlackTiles = blackTile.neighbors().stream()
                    .filter(blackTiles::contains).count();
            if (numNeighboringBlackTiles == 1 || numNeighboringBlackTiles == 2)
                nextBlackTiles.add(blackTile);
        }

        // find all tiles that are neighbors of a black tile, and how many black
        // tiles it neighbors
        HashMap<Position, Integer> tileToNumBlackNeighbors = new HashMap<>();
        for (Position blackTile : blackTiles) {
            for (Position blackTileNeighbor : blackTile.neighbors()) {
                tileToNumBlackNeighbors.merge(blackTileNeighbor, 1,
                        (prevNumNeighbors, one) -> prevNumNeighbors + 1);
            }
        }

        for (var tileAndNumNeighbors : tileToNumBlackNeighbors.entrySet()) {
            // make a tile black if it is currently white and has 2 black
            // neighbors
            if (!blackTiles.contains(tileAndNumNeighbors.getKey())
                    && tileAndNumNeighbors.getValue() == 2)
                nextBlackTiles.add(tileAndNumNeighbors.getKey());

        }

        return nextBlackTiles;

    }

    public static void main(String[] args) {
        Set<Position> blackTiles = new HashSet<>();
        for (String l : Util.inputLinesForDay(24)) {
            Position tilePosition = new Position(0, 0);
            for (String direction : parseDirections(l))
                tilePosition = tilePosition.move(direction);

            // flip state of tile that we ended up at
            if (blackTiles.contains(tilePosition))
                blackTiles.remove(tilePosition);
            else
                blackTiles.add(tilePosition);
        }
        System.out.println("Day 24 part 1: " + blackTiles.size());

        for (int day = 0; day < 100; ++day)
            blackTiles = flipAtDaysEnd(blackTiles);
        System.out.println("Day 24 part 2: " + blackTiles.size());
    }

}
