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
        int goal = SIZE * SIZE - 1;
        HashMap<Integer, Integer> distances = new HashMap<>();
        HashMap<Integer, Integer> prev = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            distances.put(i, Integer.MAX_VALUE);
        }
        distances.put(0, grid[0]);
        while (true) {
            int p = distances.entrySet().stream()
                    .filter(e -> !visited.contains(e.getKey()))
                    .filter(e -> e.getValue() != Integer.MAX_VALUE)
                    .min(Map.Entry.comparingByValue()).orElseThrow().getKey();
            visited.add(p);
            if (p == goal) {
                break;
            }
            List<Integer> neighbors = getNeighbours(p).stream().filter(i -> !visited.contains(i)).collect(Collectors.toList());
            for (Integer neighbor : neighbors) {
                int cost = distances.get(p) + grid[neighbor];
                if (cost < distances.get(neighbor)) {
                    distances.put(neighbor, cost);
                    prev.put(neighbor, p);
                }
            }
        }
        Stack<Integer> path = new Stack<>();
        int u = goal;
        int cost = 0;
        while (true) {
            path.push(u);
            cost += grid[u];
            if (!prev.containsKey(u)) {
                break;
            }
            u = prev.get(u);
        }
        System.out.println(cost - grid[0]);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day15 day15 = new Day15();
        day15.readFile();
//        day15.readFilePart2();
        long start = System.currentTimeMillis();
        day15.part1();
        System.out.println(System.currentTimeMillis() - start);
    }


}
