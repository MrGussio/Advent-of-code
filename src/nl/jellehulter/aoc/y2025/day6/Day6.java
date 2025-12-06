package nl.jellehulter.aoc.y2025.day6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day6 {

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day6\\input.txt"));
        List<List<String>> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(Arrays.stream(s.nextLine().split("\\s+")).toList());
        }
        List<String> operators = lines.getLast();
        lines = lines.subList(0, lines.size() - 1);
        int width = lines.getFirst().size();
        long sum = 0;
        for(int x = 0; x < width; x++) {
            String operator = operators.get(x);
            int finalX = x;
            sum += lines.stream()
                    .map(line -> line.get(finalX))
                    .mapToLong(Long::parseLong)
                    .reduce((a, b) -> {
                        return switch (operator) {
                            case "+" -> a + b;
                            case "*" -> a * b;
                            default -> 0;
                        };
                    })
                    .orElse(0);
        }

        System.out.println(sum);
    }
    

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day6\\input.txt"));
        List<String> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(s.nextLine());
        }
        String bottomRow = lines.getLast();

        // Find column start indices by splitting bottom row on operators
        List<Integer> colStarts = new ArrayList<>();
        int idx = 0;
        for (char c : bottomRow.toCharArray()) {
            if (c == '*' || c == '+') {
                colStarts.add(idx);
            }
            idx++;
        }
        colStarts.add(bottomRow.length()); //Add end column (I added this manually using spaces in the test file)

        // Read columns, preserving whitespace
        List<List<String>> columns = new ArrayList<>();
        for (int c = 0; c < colStarts.size() - 1; c++) {
            int start = colStarts.get(c);
            int end = colStarts.get(c + 1);
            List<String> colEntries = new ArrayList<>();
            for (String line : lines.subList(0, lines.size() - 1)) {
                colEntries.add(line.substring(start, end - 1));
            }
            columns.add(colEntries);
        }

        long sum = 0;
        int operator = 0;
        List<String> operators = bottomRow.chars()
                .mapToObj(ch -> String.valueOf((char) ch))
                .filter(ch -> ch.equals("+") || ch.equals("*"))
                .toList();
        for(List<String> col : columns) {
            int columnLength = col.stream()
                    .mapToInt(String::length)
                    .max()
                    .orElse(0);
            List<String> transformedColumn = new ArrayList<>();
            for(int i = columnLength - 1; i >= 0; i--) {
                StringBuilder sb = new StringBuilder();
                for (String str : col) {
                    if (i < str.length()) {
                        sb.append(str.charAt(str.length() - 1 - i));
                    }
                }
                transformedColumn.add(sb.toString());
            }
            int finalOperator = operator;
            sum += transformedColumn.stream()
                    .map(String::trim)
                    .mapToLong(Long::parseLong)
                    .reduce((a, b) -> switch (operators.get(finalOperator)) {
                        case "+" -> a + b;
                        case "*" -> a * b;
                        default -> 0;
                    })
                    .orElse(0);
            operator++;
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        Day6 d = new Day6();
        try {
            d.part2();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
