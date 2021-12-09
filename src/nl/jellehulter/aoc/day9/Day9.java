package nl.jellehulter.aoc.day9;

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

    public void part2() {
        final List<Point> lowPoints = getLowPoints();
        int[] basins = lowPoints.stream().map(this::getBasinSize).sorted(Comparator.reverseOrder()).limit(3).mapToInt(i -> i).toArray();
        System.out.println(Arrays.stream(basins).reduce(1, (x, y) -> x * y));
    }

    /**
     * Returns a list of all the low points in the current grid
     * @return List of all lowest points.
     */
    public List<Point> getLowPoints() {
        List<Point> response = new ArrayList<>();
        for (int y = 0; y < y_size; y++) {
            for (int x = 0; x < x_size; x++) {
                int value = grid[y][x];
                Point p = new Point(x, y);
                if (getNeighbours(p).stream().allMatch(point -> grid[point.y][point.x] > value)) {
                    response.add(p);
                }
            }
        }
        return response;
    }

    /**
     * Gets all the neighbours of a certain node
     * @param center The center node to get the neighbours of
     * @return The neighbours of the center node
     */
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

    /**
     * Returns the size of the basin a low point is in
     * @param lowPoint The low point to calculate the basin size of
     * @return The basin size
     */
    public int getBasinSize(Point lowPoint) {
        final List<Point> basin = new ArrayList<>();
        final Stack<Point> newPoints = new Stack<>();
        basin.add(lowPoint);
        newPoints.add(lowPoint);
        do {
            final Point current = newPoints.pop();
            int currentValue = grid[current.y][current.x];
            List<Point> neighbours = getNeighbours(current);
            for (final Point neighbour : neighbours) {
                int value = grid[neighbour.y][neighbour.x];
                if (value != 9 && value > currentValue && basin.stream().noneMatch(neighbour::equals)) {
                    newPoints.add(neighbour);
                    basin.add(neighbour);
                }
            }
        } while (newPoints.size() > 0);
        return basin.size();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day9 day9 = new Day9();
        day9.readFile();
        day9.part1();
        day9.part2();
    }

}
