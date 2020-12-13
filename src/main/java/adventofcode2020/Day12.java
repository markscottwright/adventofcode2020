package adventofcode2020;

import java.util.List;
import java.util.stream.Collectors;

import adventofcode2020.Day12.Instruction;

public class Day12 {

    static public class Position {
        public Position(int x, int y, char facing) {
            this.x = x;
            this.y = y;
            this.facing = facing;
        }

        final int x;
        final int y;
        final char facing;

        static char rotate(char facing, int degrees) {
            String facings = "NESW";
            return facings
                    .charAt(((degrees / 90) + facings.indexOf(facing)) % 4);
        }

        Position apply(Instruction i) {
            if (i.action == 'E' || i.action == 'F' && facing == 'E')
                return new Position(x + i.unit, y, facing);
            else if (i.action == 'W' || i.action == 'F' && facing == 'W')
                return new Position(x - i.unit, y, facing);
            else if (i.action == 'N' || i.action == 'F' && facing == 'N')
                return new Position(x, y - i.unit, facing);
            else if (i.action == 'S' || i.action == 'F' && facing == 'S')
                return new Position(x, y + i.unit, facing);
            else if (i.action == 'R')
                return new Position(x, y, rotate(facing, i.unit));
            else if (i.action == 'L')
                return new Position(x, y, rotate(facing, 360 - i.unit));
            else
                throw new RuntimeException("Illegal instruction " + i);
        }

        @Override
        public String toString() {
            return "Position [x=" + x + ", y=" + y + ", facing=" + facing + "]";
        }

        public int manhattanDistanceFrom(int x, int y) {
            return Math.abs(this.x - x) + Math.abs(this.y - y);
        }
    }

    static public class ShipAndWaypoint {
        final int shipX;
        final int shipY;
        final int waypointX;
        final int waypointY;

        public ShipAndWaypoint(int shipX, int shipY, int waypointX,
                int waypointY) {
            this.shipX = shipX;
            this.shipY = shipY;
            this.waypointX = waypointX;
            this.waypointY = waypointY;
        }

        ShipAndWaypoint apply(Instruction i) {
            if (i.action == 'E')
                return new ShipAndWaypoint(shipX, shipY, waypointX + i.unit,
                        waypointY);
            else if (i.action == 'W')
                return new ShipAndWaypoint(shipX, shipY, waypointX - i.unit,
                        waypointY);
            else if (i.action == 'N')
                return new ShipAndWaypoint(shipX, shipY, waypointX,
                        waypointY - i.unit);
            else if (i.action == 'S')
                return new ShipAndWaypoint(shipX, shipY, waypointX,
                        waypointY + i.unit);
            else if (i.action == 'R' && i.unit == 90
                    || i.action == 'L' && i.unit == 270) {
                return new ShipAndWaypoint(shipX, shipY, -waypointY, waypointX);
            } else if ((i.action == 'R' || i.action == 'L') && i.unit == 180) {
                return new ShipAndWaypoint(shipX, shipY, -waypointX,
                        -waypointY);
            } else if (i.action == 'R' && i.unit == 270
                    || i.action == 'L' && i.unit == 90) {
                return new ShipAndWaypoint(shipX, shipY, waypointY, -waypointX);
            } else if (i.action == 'F')
                return new ShipAndWaypoint(shipX + i.unit * waypointX,
                        shipY + i.unit * waypointY, waypointX, waypointY);
            else
                throw new RuntimeException("Illegal instruction " + i);
        }

        public int manhattanDistanceFrom(int x, int y) {
            return Math.abs(this.shipX - x) + Math.abs(this.shipY - y);
        }
    }

    static public class Instruction {
        private char action;
        private int unit;

        public Instruction(char action, int unit) {
            this.action = action;
            this.unit = unit;
        }

        static Instruction parse(String l) {
            return new Instruction(l.charAt(0),
                    Integer.parseInt(l.substring(1)));
        }

        @Override
        public String toString() {
            return "Instruction [action=" + action + ", unit=" + unit + "]";
        }
    }

    public static void main(String[] emptyArgs) {
        List<Instruction> directions = Util.inputLinesForDay(12).stream()
                .map(Instruction::parse).collect(Collectors.toList());

        Position position = new Position(0, 0, 'E');
        for (Instruction instruction : directions) {
            position = position.apply(instruction);
        }
        System.out.println(
                "Day 12 part 1: " + position.manhattanDistanceFrom(0, 0));

        ShipAndWaypoint position2 = new ShipAndWaypoint(0, 0, 10, -1);
        for (Instruction instruction : directions) {
            position2 = position2.apply(instruction);
        }
        System.out.println(
                "Day 12 part 2: " + position2.manhattanDistanceFrom(0, 0));
    }

}
