package nl.jellehulter.aoc.y2021.day11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {

    private Integer[] grid;
    private int size;

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day11/input.txt"));
        List<Integer> grid = new ArrayList<>();
        while (s.hasNextLine()) {
            grid.addAll(s.nextLine().chars().mapToObj(c -> Integer.parseInt(String.valueOf((char) c))).collect(Collectors.toList()));
        }
        this.grid = grid.toArray(Integer[]::new);
        size = (int) Math.sqrt(this.grid.length);
    }

    public int index(int x, int y) {
        return y * size + x;
    }

    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            if (i % size == 0)
                System.out.println();
            System.out.print(grid[i]);
        }
        System.out.println();
    }

    public List<Integer> getAdjacent(int node) {
        List<Integer> result = new ArrayList<>();
        int[] xs = {-1, +0, +1, +1, +1, +0, -1, -1};
        int[] ys = {-1, -1, -1, +0, +1, +1, +1, +0,};
        int x = node % size;
        int y = node / size;

        for(int i = 0; i < 8; i++) {
            if(x+xs[i] >= 0 && x+xs[i] < size &&
            y+ys[i] >= 0 && y+ys[i] < size) {
                result.add(index(x+xs[i], y+ys[i]));
            }
        }
        return result;
    }

    public int step() {
        grid = Arrays.stream(grid).map(i -> i + 1).toArray(Integer[]::new);
        Stack<Integer> stack = new Stack<>();
        final List<Integer> flashed = new ArrayList<>(IntStream.range(0, grid.length).filter(i -> grid[i] > 9).boxed().collect(Collectors.toList()));
        stack.addAll(flashed);
        while (!stack.empty()) {
            int index = stack.pop();
            List<Integer> adjacent = getAdjacent(index);
            for(int neighbor : adjacent) {
                grid[neighbor]++;
                if (grid[neighbor] > 9 && !flashed.contains(neighbor)) {
                    flashed.add(neighbor);
                    stack.add(neighbor);
                }
            }
        }
        for(int index : flashed) {
            grid[index] = 0;
        }
        return flashed.size();
    }

    public int part1() {
        return IntStream.range(0, 100).map(i -> this.step()).sum();
    }

    public long part2() {
        long step = 1;
        while(step() != size*size) {
            step++;
        }
        return step;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day11 day11 = new Day11();
        day11.readFile();
        System.out.println(day11.part1());
        day11.printGrid();

        day11 = new Day11();
        day11.readFile();
        System.out.println(day11.part2());
        day11.printGrid();
    }

}
