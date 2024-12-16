package nl.jellehulter.aoc.y2024.day7;

import nl.jellehulter.aoc.y2024.Day;
import nl.jellehulter.aoc.y2024.day6.DaySix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaySeven extends Day {

    record Equation(long result, List<Long> operands){

        enum Operation {
            MULTIPLY,
            ADD,
            CONCATENATE
        }

        static Equation fromString(String s) {
            String[] colonSplit = s.split(": ");
            String[] spaceSplit = colonSplit[1].split(" ");
            long result = Long.parseLong(colonSplit[0]);
            List<Long> operands = Arrays.stream(spaceSplit)
                    .map(Long::parseLong)
                    .toList();
            return new Equation(result, operands);
        }

        Equation compute(Operation o) {
            List<Long> newOperands = new ArrayList<>();
            long a = operands.get(0);
            long b = operands.get(1);
            if(o == Operation.ADD) {
                newOperands.add(a + b);
            } else if(o == Operation.MULTIPLY) {
                newOperands.add(a * b);
            } else if(o == Operation.CONCATENATE){
                newOperands.add(Long.parseLong(a + "" + b));
            } else {
                throw new RuntimeException();
            }
            newOperands.addAll(operands.subList(2, operands.size()));
            return new Equation(result, newOperands);
        }

        boolean isCorrect() {
            if(operands.size() == 1) {
                return operands.getFirst() == result;
            }

            return compute(Operation.ADD).isCorrect() || compute(Operation.MULTIPLY).isCorrect();
        }

        boolean isCorrectWithConcatenate() {
            if(operands.size() == 1) {
                return operands.getFirst() == result;
            }
            return Arrays.stream(Operation.values()).anyMatch(o -> compute(o).isCorrectWithConcatenate());
        }
    }


    public void part1() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day7/dayseven.txt");
        List<Equation> equations = lines.stream().map(Equation::fromString).toList();

        long result = equations.stream().filter(Equation::isCorrect)
                .mapToLong(Equation::result)
                .sum();
        System.out.println(result);
    }

    public void part2() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day7/dayseven.txt");
        List<Equation> equations = lines.stream().map(Equation::fromString).toList();

        long result = equations.stream().filter(Equation::isCorrectWithConcatenate)
                .mapToLong(Equation::result)
                .sum();
        System.out.println(result);
    }

    public static void main(String[] args) {
        DaySeven daySeven = new DaySeven();
        daySeven.part1();
        daySeven.part2();
    }
}
