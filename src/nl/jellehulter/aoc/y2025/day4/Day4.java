package nl.jellehulter.aoc.y2025.day4;

import nl.jellehulter.aoc.y2021.day19.Day19;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Day4 {

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day4\\INPUT.txt"));
        List<String> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(s.nextLine());
        }
        int width = lines.getFirst().length();
        int height = lines.size();
        int score = 0;

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                char currentChar = lines.get(y).charAt(x);
                if(currentChar == '@') {
                    int adjacent = countAdjacent(x, y, lines);
                    if(adjacent < 4)
                        score++;
                }
            }
        }
        System.out.println(score);
    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day4\\input.txt"));
        List<String> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(s.nextLine());
        }
        int width = lines.getFirst().length();
        int height = lines.size();
        Set<Coord> removed = new HashSet<>();
        Set<Coord> toRemove = new HashSet<>();
        do {
            toRemove.clear();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    char currentChar = lines.get(y).charAt(x);
                    if (currentChar == '@') {
                        int adjacent = countAdjacent(x, y, lines);
                        if (adjacent < 4) {
                            toRemove.add(new Coord(x, y));
                        }
                    }
                }
            }
            System.out.println(toRemove.size());
            for(Coord c : toRemove) {
                StringBuilder sb = new StringBuilder(lines.get(c.y));
                sb.setCharAt(c.x, '.');
                lines.set(c.y, sb.toString());
                removed.add(c);
            }
        } while(!toRemove.isEmpty());
        System.out.println(removed.size());
    }

    record Coord(int x, int y) {}

    public int countAdjacent(int x, int y, List<String> lines) {
        int count = 0;
        int width = lines.getFirst().length();
        int height = lines.size();

        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                if(dx == 0 && dy == 0)
                    continue;
                int nx = x + dx;
                int ny = y + dy;
                if(nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if(lines.get(ny).charAt(nx) == '@') {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        Day4 d = new Day4();
        try {
            d.part2();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
