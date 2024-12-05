package nl.jellehulter.aoc.y2024.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import nl.jellehulter.aoc.y2024.Day;

public class DayTwo extends Day {

    public void part1() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day2/daytwo.txt");
        List<List<Integer>> split =
                lines.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList())
                        .toList();

        long count = split.stream().map(this::deltas).filter(this::isSafe).count();
        System.out.println(count);
    }

    public void part2() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day2/daytwo.txt");
        List<List<Integer>> split =
                lines.stream().map(s -> Arrays.stream(s.split(" ")).map(Integer::parseInt).toList())
                        .toList();

        long count = split.stream().map(this::createVariants)
                .filter(l -> l.stream()
                        .map(this::deltas)
                        .anyMatch(this::isSafe))
                .count();
        System.out.println(count);
    }

    private List<Integer> deltas(List<Integer> list) {
        return IntStream.range(1, list.size()).mapToObj(i -> list.get(i) - list.get(i - 1))
                .toList();
    }

    private boolean isSafe(List<Integer> delta) {
        return (delta.stream().allMatch(i -> i >= 0) || delta.stream().allMatch(i -> i <= 0)) &&
                delta.stream().map(Math::abs).allMatch(i -> i >= 1 && i <= 3);
    }

    private List<List<Integer>> createVariants(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(list);
        for (int i = 0; i < list.size(); i++) {
            List<Integer> sublist = new ArrayList<>(list);
            sublist.remove(i);
            result.add(sublist);
        }
        return result;
    }

    public static void main(String[] args) {
        DayTwo dayTwo = new DayTwo();
        dayTwo.part1();
        dayTwo.part2();
    }

}
