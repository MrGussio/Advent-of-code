package nl.jellehulter.aoc.y2024.day5;

import java.util.*;
import java.util.stream.Collectors;
import nl.jellehulter.aoc.y2024.Day;

public class DayFive extends Day {

    public void part1() {
        record Rule(int a, int b) {
        }
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day5/dayfive.txt");
        List<Rule> rules = lines.stream().takeWhile(s -> !s.isEmpty()).map(s -> s.split("\\|"))
                .map(i -> new Rule(Integer.parseInt(i[0]), Integer.parseInt(i[1]))).toList();
        List<List<Integer>> updates =
                lines.stream().dropWhile(s -> !s.isEmpty()).skip(1).map(s -> s.split(","))
                        .map(s -> Arrays.stream(s).map(Integer::parseInt).toList()).toList();

        int result = updates.stream().filter(l -> l.stream().allMatch(i -> {
                    int index = l.indexOf(i);
                    boolean start = rules.stream().filter(r -> r.a == i)
                            .allMatch(rule -> !l.contains(rule.b) || l.indexOf(rule.b) > index);
                    boolean end = rules.stream().filter(r -> r.b == i)
                            .allMatch(rule -> !l.contains(rule.a) || l.indexOf(rule.a) < index);

                    return start && end;
                }))
                .peek(System.out::println)
                .map(l -> l.get(l.size() / 2))
                .reduce(0, Integer::sum);

        System.out.println(result);
    }

    public void part2() {
        record Rule(int a, int b) {
        }
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day5/dayfive.txt");
        List<Rule> rules = lines.stream().takeWhile(s -> !s.isEmpty()).map(s -> s.split("\\|"))
                .map(i -> new Rule(Integer.parseInt(i[0]), Integer.parseInt(i[1]))).toList();
        List<List<Integer>> updates =
                lines.stream().dropWhile(s -> !s.isEmpty()).skip(1).map(s -> s.split(","))
                        .map(s -> Arrays.stream(s).map(Integer::parseInt).toList()).toList();

        int result = updates.stream().filter(l -> !l.stream().allMatch(i -> {
                    int index = l.indexOf(i);
                    boolean start = rules.stream().filter(r -> r.a == i)
                            .allMatch(rule -> !l.contains(rule.b) || l.indexOf(rule.b) > index);
                    boolean end = rules.stream().filter(r -> r.b == i)
                            .allMatch(rule -> !l.contains(rule.a) || l.indexOf(rule.a) < index);

                    return start && end;
                }))
                .map(l -> l.stream().sorted((o1, o2) -> {
                    Optional<Rule> first = rules.stream().filter(r -> r.a == o1 && r.b == o2).findAny();
                    Optional<Rule> second = rules.stream().filter(r -> r.a == o2 && r.b == o1).findAny();
                    if(first.isPresent()) {
                        return -1;
                    }
                  return 1;
                }).toList())
                .peek(System.out::println)
                .map(l -> l.get(l.size() / 2))
                .reduce(0, Integer::sum);
        System.out.println(result);
    }

    public static void main(String[] args) {
        DayFive day = new DayFive();
        day.part1();
        day.part2();
    }

}
