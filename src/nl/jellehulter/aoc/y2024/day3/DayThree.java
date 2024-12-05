package nl.jellehulter.aoc.y2024.day3;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.jellehulter.aoc.y2024.Day;

public class DayThree extends Day {

    public void part1() {
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day3/daythree.txt");

        String allLines = lines.stream().reduce("", (a, b) -> a + b);
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(allLines);
        int sum = 0;
        while(matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            sum += x*y;
        }
        System.out.println(sum);
    }

    public void part2() {
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day3/daythree.txt");
        String allLines = lines.stream().reduce("", (a, b) -> a + b);
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(allLines);
        int sum = 0;
        boolean enabled = true;
        while(matcher.find()) {
            if(matcher.group().equals("do()")) {
                enabled = true;
            } else if(matcher.group().equals("don't()")) {
                enabled = false;
            } else {
                if(enabled) {
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    sum += x * y;
                }
            }
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        DayThree dayThree = new DayThree();
        dayThree.part1();
        dayThree.part2();
    }


}
