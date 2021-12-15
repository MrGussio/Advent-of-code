package nl.jellehulter.aoc.day15;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 {

    public int SIZE = 100;
    private int grid[];

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day15/input.txt"));
        grid = new int[SIZE * SIZE];
        for (int i = 0; i < SIZE; i++) {
            String line = s.nextLine();
            int[] gridLine = line.chars().map(Character::getNumericValue).toArray();
            System.arraycopy(gridLine, 0, grid, i * SIZE, SIZE);
        }
    }

    public void readFilePart2() {
        int newSIZE = SIZE * 5;
        int[] newGrid = new int[newSIZE * newSIZE];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                for (int i = 0; i < grid.length; i++) {
                    int gridY = i / SIZE;
                    int gridX = i % SIZE;
                    int cost = (grid[i] + x + y);
                    while(cost > 9) {
                        cost -= 9;
                    }
                    newGrid[(SIZE * y + gridY)*newSIZE + (x * SIZE + gridX)] = cost;
                }
            }
        }
        grid = newGrid;
        SIZE = newSIZE;
    }

    public List<Integer> getNeighbours(int i) {
        int y = i / SIZE;
        int x = i % SIZE;
        List<Integer> result = new ArrayList<>();
        if (x > 0) {
            result.add(y * SIZE + x - 1);
        }
        if (x < SIZE - 1) {
            result.add(y * SIZE + x + 1);
        }
        if (y > 0) {
            result.add((y - 1) * SIZE + x);
        }
        if (y < SIZE - 1) {
            result.add((y + 1) * SIZE + x);
        }
        return result;
    }

    public void part1() {
        int[] distances = new int[SIZE*SIZE];
        Arrays.fill(distances,  1000000);
        distances[0] = grid[0];
        int[] lastDistances = distances.clone();
        do {
            lastDistances = distances.clone();
            for (int i = 0; i < grid.length; i++) {
                List<Integer> neighbours = getNeighbours(i);
                int min = 1000000;
                for (Integer neighbor : neighbours) {
                    min = Math.min(min, distances[neighbor]);
                }
                distances[i] = Math.min(grid[i] + min, distances[i]);
            }
        } while(!Arrays.equals(distances, lastDistances));
        System.out.println(lastDistances[SIZE*SIZE-1] - grid[0]);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day15 day15 = new Day15();
        day15.readFile();
        day15.readFilePart2();
        long start = System.currentTimeMillis();
        day15.part1();
        System.out.println(System.currentTimeMillis() - start);
    }


}
