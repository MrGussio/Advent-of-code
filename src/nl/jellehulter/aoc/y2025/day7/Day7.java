package nl.jellehulter.aoc.y2025.day7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
 
    private String testInput = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............""";
    
    public void part1() throws IOException {
        String input = Files.readString(Path.of("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day7\\input.txt"));
        List<String> lines = input.lines().toList();
        char[][] grid = lines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        
        int height = lines.size();
        
        int s = lines.getFirst().indexOf('S');
        Set<Coord> beams = new HashSet<>();
        Set<Coord> newBeams = new HashSet<>();
        Set<Coord> splits = new HashSet<>();
        beams.add(new Coord(s, 0));
        int previousSize = 0;
        while(beams.size() != previousSize) {
            previousSize = beams.size();
            for(Coord c : beams) {
                if(c.y < height - 1) {
                    char below = grid[c.y + 1][c.x];
                    if (below == '.') {
                        newBeams.add(new Coord(c.x, c.y + 1));
                    } else if (below == '^') {
                        splits.add(c);
                        char leftBelow = grid[c.y + 1][c.x - 1];
                        char rightBelow = grid[c.y + 1][c.x + 1];
                        if (leftBelow == '.') {
                            newBeams.add(new Coord(c.x - 1, c.y + 1));
                        }
                        if (rightBelow == '.') {
                            newBeams.add(new Coord(c.x + 1, c.y + 1));
                        }
                    }
                }
            }
            beams.addAll(newBeams);
            newBeams.clear();
        }
        
        System.out.println(splits.size());
    }
    
    public void part2() throws IOException {
        String input = Files.readString(Path.of("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day7\\input.txt"));
        List<String> lines = input.lines().toList();
        char[][] grid = lines.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        int s = lines.getFirst().indexOf('S');

        Set<Coord> nextRound = new HashSet<>();
        Set<Coord> unfinished = new HashSet<>();
        Map<Coord, Long> beamCounts = new HashMap<>();
        Coord start = new Coord(s, 0);
        beamCounts.put(start, 1L);
        nextRound.add(start);    
        for(int i = 0; i < lines.size() - 1; i++) {
            unfinished = nextRound;
            nextRound = new HashSet<>();
            for (Coord c : unfinished) {
                Coord below = new Coord(c.x, c.y + 1);
                char charBelow = get(grid, below);
                long currentCount = beamCounts.getOrDefault(c, 0L);
                if (charBelow == '.') {
                    nextRound.add(below);
                    beamCounts.put(below, beamCounts.getOrDefault(below, 0L) + currentCount);
                } else if (charBelow == '^') {
                    Coord leftBelow = new Coord(c.x - 1, c.y + 1);
                    Coord rightBelow = new Coord(c.x + 1, c.y + 1);
                    if (get(grid, leftBelow) == '.') {
                        nextRound.add(leftBelow);
                        beamCounts.put(leftBelow, beamCounts.getOrDefault(leftBelow, 0L) + currentCount);
                    }
                    if (get(grid, rightBelow) == '.') {
                        nextRound.add(rightBelow);
                        beamCounts.put(rightBelow, beamCounts.getOrDefault(rightBelow, 0L) + currentCount);
                    }
                }
            }
        }
        long result = beamCounts.entrySet().stream().filter(coordLongEntry -> coordLongEntry.getKey().y == lines.size() - 1)
                .map(Map.Entry::getValue)
                .mapToLong(value -> value)
                .sum();
        System.out.println(result);
    }
    
    public char get(char[][] grid, Coord c) {
        return grid[c.y][c.x];
    }

    record Coord(int x, int y){}

    public static void main(String[] args) throws IOException {
        Day7 d = new Day7();
        d.part2();
    }
    
    
}
