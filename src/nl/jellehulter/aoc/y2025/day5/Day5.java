package nl.jellehulter.aoc.y2025.day5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Day5 {
    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day5\\input.txt"));
        List<String> ranges = new ArrayList<>();
        List<Long> ingredients = new ArrayList<>();

        boolean blankFound = false;
        while(s.hasNext()) {
            String next = s.nextLine();
            if(!blankFound) {
                if (next.isBlank()) {
                    blankFound = true;
                } else {
                    ranges.add(next);
                }
            } else {
                ingredients.add(Long.parseLong(next));
            }
        }

        var rangeList = ranges.stream()
                .map(str -> {
                    String[] bounds = str.split("-");
                    return new Range(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
                }).toList();

        var freshIngredients = ingredients.stream()
                .filter(i -> rangeList.stream().anyMatch(r -> r.inRange(i)))
                .toList();

        System.out.println(freshIngredients.size());
    }

    record Range(long start, long end) {

        boolean inRange(long num) {
            return num >= start && num <= end;
        }

        long size() {
            return end - start + 1;
        }

    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day5\\input.txt"));
        List<String> ranges = new ArrayList<>();

        while(s.hasNext()) {
            String next = s.nextLine();

            if (next.isBlank()) {
                break;
            }
            ranges.add(next);
        }

        var rangeList = ranges.stream()
                .map(str -> {
                    String[] bounds = str.split("-");
                    return new Range(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
                }).toList();

        List<Range> mergedRanges = mergeRanges(rangeList);

        long size = mergedRanges.stream().map(Range::size)
                .mapToLong(value -> value)
                .sum();

        System.out.println(size);
    }

    List<Range> mergeRanges(List<Range> ranges) {
        List<Range> sorted = new ArrayList<>(ranges);
        List<Range> merged = new ArrayList<>();

        sorted.sort(Comparator.comparingLong(r -> r.start));
        Range prev = sorted.getFirst();

        for (int i = 1; i < sorted.size(); i++) {
            Range curr = sorted.get(i);
            if (prev.inRange(curr.start)) {
                prev = new Range(prev.start, Math.max(prev.end, curr.end));
            } else {
                merged.add(prev);
                prev = curr;
            }
        }
        merged.add(prev);
        return merged;
    }

    public static void main(String[] args) {
        Day5 d = new Day5();
        try {
            d.part2();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
