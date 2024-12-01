package nl.jellehulter.aoc.y2024.day1;

import java.util.ArrayList;
import java.util.List;
import nl.jellehulter.aoc.y2024.Day;

public class DayOne extends Day {


    public void part1() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day1/test.txt");
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        lines.stream()
                .map(s -> s.split("   "))
                .forEach(s -> {
                    first.add(Integer.parseInt(s[0]));
                    second.add(Integer.parseInt(s[1]));
                });

        List<Integer> sortedFirst = first.stream().sorted().toList();
        List<Integer> sortedSecond = second.stream().sorted().toList();

        int difference = 0;
        for(int i = 0; i < first.size(); i++) {
            difference += Math.abs(sortedFirst.get(i) - sortedSecond.get(i));
        }

        System.out.println(difference);
    }

    public void part2() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day1/dayone.txt");
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        lines.stream()
                .map(s -> s.split("   "))
                .forEach(s -> {
                    first.add(Integer.parseInt(s[0]));
                    second.add(Integer.parseInt(s[1]));
                });

        long result = first.stream()
                .map(i -> i * second.stream().filter(j -> j.equals(i)).count())
                .reduce((a, b) -> a + b)
                .orElse(0L);
        System.out.println(result);
    }

    public static void main(String[] args) {
        DayOne dayOne = new DayOne();
        dayOne.part1();
        dayOne.part2();
    }

}
