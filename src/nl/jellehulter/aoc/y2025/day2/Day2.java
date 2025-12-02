package nl.jellehulter.aoc.y2025.day2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day2 {

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day2\\input.txt"));
        String line = s.nextLine();

        String[] ranges = line.split(",");
        List<Pair> pairs = Arrays.stream(ranges)
                .map(pair -> {
                    String[] bounds = pair.split("-");
                    return new Pair(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
                }).toList();

        var result = pairs.stream().map(Pair::getInvalidIds)
                .flatMap(List::stream)
                .mapToLong(Long::longValue)
                .sum();

        System.out.println("Result: " + result);
    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day2\\input.txt"));
        String line = s.nextLine();

        String[] ranges = line.split(",");
        List<Pair> pairs = Arrays.stream(ranges)
                .map(pair -> {
                    String[] bounds = pair.split("-");
                    return new Pair(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
                }).toList();

        var result = pairs.stream().map(Pair::getIncreasingInvalidIds)
                .flatMap(List::stream)
                .mapToLong(Long::longValue)
                .sum();

        System.out.println("Result: " + result);
    }


    record Pair(long first, long second) {

        List<Long> getInvalidIds() {
            return LongStream.rangeClosed(first, second)
                    .filter(i -> {
                        String num = String.valueOf(i);
                        if(num.length() % 2 != 0)
                            return false;
                        String first = num.substring(0, num.length() / 2);
                        String second = num.substring(num.length() / 2);
                        return first.equals(second);
                    }).boxed()
                    .toList();
        }

        List<Long> getIncreasingInvalidIds() {
            return LongStream.rangeClosed(first, second)
                    .filter(i -> {
                        String num = String.valueOf(i);
                        return isRepetitive(num);
                    }).boxed()
                    .toList();
        }

    }

    public static boolean isRepetitive(String s) {
        for(int i = 0; i < s.length() / 2; i++) {
            String substring = s.substring(0, i + 1);
            if(s.length() % substring.length() != 0) {
                continue;
            }
            if(s.equals(substring.repeat(s.length() / substring.length()))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Day2 d = new Day2();
        System.out.println(isRepetitive("999"));
        System.out.println(isRepetitive("123123"));
        try {
            d.part1();
            d.part2();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
