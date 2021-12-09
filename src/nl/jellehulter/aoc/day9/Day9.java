package nl.jellehulter.aoc.day9;

import nl.jellehulter.aoc.day8.Day8;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Day9 {

    public int[][] grid;
//    public int y_size = 5;
//    public int x_size = 10;

    public int y_size = 100;
    public int x_size = 100;

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day9/input.txt"));
        grid = new int[y_size][x_size];
        int i = 0;
        while (s.hasNext()) {
            String line = s.nextLine();
            grid[i] = line.chars().map(c -> Integer.parseInt(Character.toString(c))).toArray();
            i++;
        }
    }

    public void part1() {
        int sum = getLowPoints().stream().mapToInt(p -> grid[p.y][p.x] + 1).sum();
        System.out.println(sum);
    }

    public List<Point> getLowPoints() {
        List<Point> response = new ArrayList<>();
        for (int y = 0; y < y_size; y++) {
            for (int x = 0; x < x_size; x++) {
                int value = grid[y][x];
                if (x > 0 && grid[y][x - 1] <= value)
                    continue;
                if (x < x_size - 1 && grid[y][x + 1] <= value)
                    continue;
                if (y > 0 && grid[y - 1][x] <= value)
                    continue;
                if (y < y_size - 1 && grid[y + 1][x] <= value)
                    continue;
                response.add(new Point(x, y));
            }
        }
        return response;
    }

    public List<Point> getNeighbours(Point center) {
        List<Point> neighbours = new ArrayList<>();
        if (center.x > 0) {
            neighbours.add(new Point(center.x - 1, center.y));
        }
        if (center.x < x_size - 1) {
            neighbours.add(new Point(center.x + 1, center.y));
        }
        if (center.y > 0) {
            neighbours.add(new Point(center.x, center.y - 1));
        }
        if (center.y < y_size - 1) {
            neighbours.add(new Point(center.x, center.y + 1));
        }
        return neighbours;
    }

    public int getBasinSize(Point lowPoint) {
        final List<Point> basin = new ArrayList<>();
        final Stack<Point> newPoints = new Stack<>();
        basin.add(lowPoint);
        newPoints.add(lowPoint);
        do {
            final Point current = newPoints.pop();
            int currentValue = grid[current.y][current.x];
            List<Point> neighbours = getNeighbours(current);
            for(final Point neighbour : neighbours) {
                int value = grid[neighbour.y][neighbour.x];
                if(value != 9 && value > currentValue && basin.stream().noneMatch(neighbour::equals)) {
                    newPoints.add(neighbour);
                    basin.add(neighbour);
                }
            }
        } while(newPoints.size() > 0);
        return basin.size();
    }

    public void part2() {
        final List<Point> lowPoints = getLowPoints();
        int[] basins = lowPoints.stream().mapToInt(this::getBasinSize).sorted().toArray();
//        Arrays.stream(basins).
        System.out.println(basins[basins.length - 1] * basins[basins.length - 2] * basins[basins.length - 3]);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day9 day9 = new Day9();
        day9.readFile();
        day9.part1();
        day9.part2();
    }

}
