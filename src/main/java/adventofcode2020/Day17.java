package adventofcode2020;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day17 {

    static class Cube {
        @Override
        public String toString() {
            return "[" + x + "," + y + "," + z + "," + w + "]";
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z, w);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Cube other = (Cube) obj;
            return x == other.x && y == other.y && z == other.z && w == other.w;
        }

        public Cube(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        final int x;
        final int y;
        final int z;
        final int w;

        Collection<Cube> neighbors3d() {
            ArrayList<Cube> neighborList = new ArrayList<>();
            for (int xDiff = -1; xDiff <= 1; ++xDiff)
                for (int yDiff = -1; yDiff <= 1; ++yDiff)
                    for (int zDiff = -1; zDiff <= 1; ++zDiff)
                        if (!(xDiff == 0 && yDiff == 0 && zDiff == 0))
                            neighborList.add(new Cube(this.x + xDiff,
                                    this.y + yDiff, this.z + zDiff, w));
            return neighborList;
        }

        Collection<Cube> neighbors4d() {
            ArrayList<Cube> neighborList = new ArrayList<>();
            for (int xDiff = -1; xDiff <= 1; ++xDiff)
                for (int yDiff = -1; yDiff <= 1; ++yDiff)
                    for (int zDiff = -1; zDiff <= 1; ++zDiff)
                        for (int wDiff = -1; wDiff <= 1; ++wDiff)
                            if (!(xDiff == 0 && yDiff == 0 && zDiff == 0
                                    && wDiff == 0))
                                neighborList.add(new Cube(this.x + xDiff,
                                        this.y + yDiff, this.z + zDiff,
                                        this.w + wDiff));
            return neighborList;
        }

        static Set<Cube> cycle(Set<Cube> activeCubes, boolean is4d) {
            HashSet<Cube> cubesWithActiveNeighbors = new HashSet<>();
            for (Cube activeCube : activeCubes) {
                if (is4d)
                    cubesWithActiveNeighbors.addAll(activeCube.neighbors4d());
                else
                    cubesWithActiveNeighbors.addAll(activeCube.neighbors3d());
            }

            // for each cube that has an active neighbor, if it was already
            // active and it has 2-3 active neighbors, it's active. If it was
            // not active and it has 3 neighbors, it's active. Otherwise, it's
            // inactive.
            HashSet<Cube> out = new HashSet<>();
            for (var cubeWithActiveNeighbors : cubesWithActiveNeighbors) {
                boolean alreadyActive = activeCubes
                        .contains(cubeWithActiveNeighbors);
                long numActiveNeighbors;
                if (is4d) {
                    numActiveNeighbors = cubeWithActiveNeighbors.neighbors4d()
                            .stream().filter(activeCubes::contains).count();
                } else {
                    numActiveNeighbors = cubeWithActiveNeighbors.neighbors3d()
                            .stream().filter(activeCubes::contains).count();
                }

                if (alreadyActive && (numActiveNeighbors == 2
                        || numActiveNeighbors == 3)) {
                    out.add(cubeWithActiveNeighbors);
                } else if (!alreadyActive && numActiveNeighbors == 3) {
                    out.add(cubeWithActiveNeighbors);
                }
            }
            return out;
        }
    }

    public static void main(String[] strings) {
        int y = 0;

        Set<Cube> activeCubes = new HashSet<>();
        for (String l : Util.inputLinesForDay(17)) {
            for (int x = 0; x < l.length(); ++x) {
                if (l.charAt(x) == '#')
                    activeCubes.add(new Cube(x, y, 0, 0));
            }
            y++;
        }
        Set<Cube> activeCubesForPart2 = new HashSet<>(activeCubes);

        for (int cycle = 0; cycle < 6; ++cycle) {
            activeCubes = Cube.cycle(activeCubes, false);
        }
        System.out.println("Day 17 part 1: " + activeCubes.size());

        for (int cycle = 0; cycle < 6; ++cycle) {
            activeCubesForPart2 = Cube.cycle(activeCubesForPart2, true);
        }
        System.out.println("Day 17 part 2: " + activeCubesForPart2.size());
    }

}
