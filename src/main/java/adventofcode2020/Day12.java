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
        
        Position apply(Instruction i) {
            if (i.action == 'E')
                return new Position(x + i.unit, y, facing);
            else if (i.action == 'W')
                return new Position(x - i.unit, y, facing);
            else if (i.action == 'N')
                return new Position(x , y - i.unit, facing);
            else if (i.action == 'S')
                return new Position(x , y + i.unit, facing);
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
            return new Instruction(l.charAt(0), Integer.parseInt(l.substring(1)));
        }
    }

    public static void main(String[] emptyArgs) {
        List<Instruction> directions = Util.inputLinesForDay(12).stream()
                .map(Instruction::parse).collect(Collectors.toList());
        System.out.println("Day 12 part 1:");
    }

}
