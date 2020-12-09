package adventofcode2020;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import adventofcode2020.Day8.Result.ProgramExit;

public class Day8 {
    static public class Result {
        private ProgramExit exitType;
        private int accumulator;

        @Override
        public String toString() {
            return "Result [exitType=" + exitType + ", accumulator="
                    + accumulator + "]";
        }

        public Result(ProgramExit exitType, int accumulator) {
            this.exitType = exitType;
            this.accumulator = accumulator;
        }

        enum ProgramExit {
            LOOP, EXIT
        };

    }

    static abstract class Instruction {
        static Instruction parse(String s) {
            var fields = s.split("\\s+");
            switch (fields[0]) {
            case "acc":
                return new Acc(parseInt(fields[1]));
            case "nop":
                return new Nop(parseInt(fields[1]));
            case "jmp":
                return new Jmp(parseInt(fields[1]));
            default:
                throw new RuntimeException(
                        "Uknown instruction: " + fields[0] + " in " + s);
            }
        }

        protected abstract void apply(HandholdGameConsole handholdGameConsole);

        protected abstract Instruction swapNopAndJmp();
    }

    static class Acc extends Instruction {
        @Override
        public String toString() {
            return "Acc [value=" + value + "]";
        }

        private int value;

        public Acc(int value) {
            this.value = value;
        }

        @Override
        protected void apply(HandholdGameConsole handholdGameConsole) {
            handholdGameConsole.address += 1;
            handholdGameConsole.accumulator += value;
        }

        @Override
        protected Instruction swapNopAndJmp() {
            return this;
        }
    }

    static class Jmp extends Instruction {
        @Override
        protected Instruction swapNopAndJmp() {
            return new Nop(value);
        }

        @Override
        public String toString() {
            return "Jmp [value=" + value + "]";
        }

        private int value;

        public Jmp(int value) {
            this.value = value;
        }

        @Override
        protected void apply(HandholdGameConsole handholdGameConsole) {
            handholdGameConsole.address += value;
        }

    }

    static class Nop extends Instruction {
        @Override
        protected Instruction swapNopAndJmp() {
            return new Jmp(value);
        }

        @Override
        public String toString() {
            return "Nop [value=" + value + "]";
        }

        private int value;

        public Nop(int value) {
            this.value = value;
        }

        @Override
        protected void apply(HandholdGameConsole handholdGameConsole) {
            handholdGameConsole.address += 1;
        }

    }

    static class HandholdGameConsole {
        private List<Instruction> program;
        int accumulator = 0;
        int address = 0;

        public HandholdGameConsole(List<Instruction> program) {
            this.program = program;
        }

        static List<Instruction> readProgram(File program) throws IOException {
            return Files.readAllLines(program.toPath()).stream()
                    .map(Instruction::parse).collect(Collectors.toList());
        }

        Result runProgramUntilLoop() {
            HashSet<Integer> addressesSeen = new HashSet<>();
            while (!addressesSeen.contains(address)) {
                addressesSeen.add(address);
                Instruction nextInstruction = program.get(address);
                // System.out.println(String.format("(%d) %d: %s", accumulator,
                // address, nextInstruction));
                nextInstruction.apply(this);
                if (address >= program.size())
                    return new Result(Result.ProgramExit.EXIT, accumulator);
            }

            return new Result(Result.ProgramExit.LOOP, accumulator);
        }
    }

    public static void main(String[] args) throws IOException {
        var program = HandholdGameConsole
                .readProgram(new File("src/main/resources/day8.txt"));
        var part1Answer = new HandholdGameConsole(program)
                .runProgramUntilLoop();
        System.out.println("Day 8 part 1: " + part1Answer.accumulator);

        Result result = mutateProgramUntilExit(program);
        System.out.println("Day 8 part 2: " + result.accumulator);
    }

    private static Result mutateProgramUntilExit(List<Instruction> program) {
        // keep trying program mutations until one exits instead of looping
        int addressToMutate = 0;
        while (true) {
            if (addressToMutate >= program.size())
                throw new RuntimeException("No solution found");

            // find the next address to mutate and flip it
            while (!(program.get(addressToMutate) instanceof Nop)
                    && !(program.get(addressToMutate) instanceof Jmp)) {
                addressToMutate++;
            }
            List<Instruction> mutatedProgram = new ArrayList<>(program);
            mutatedProgram.set(addressToMutate,
                    mutatedProgram.get(addressToMutate).swapNopAndJmp());

            Result result = new HandholdGameConsole(mutatedProgram)
                    .runProgramUntilLoop();
            if (result.exitType == ProgramExit.EXIT) {
//                System.out.println("Address to mutate: " + addressToMutate
//                        + " - " + program.get(addressToMutate) + " -> "
//                        + program.get(addressToMutate).swapNopAndJmp());
                return result;
            }
            addressToMutate++;
        }
    }
}
