package nl.jellehulter.aoc.y2022.day8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day8 {

    static final int SIZE =  99;
    int[][] grid;
    boolean[][] visibility = new boolean[SIZE][SIZE];

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day8/day8.txt"));
        grid = new int[SIZE][SIZE];
        for(int y = 0; y < SIZE; y++) {
            String line = scanner.nextLine();
            for(int x = 0; x < SIZE; x++) {
                grid[y][x] = Integer.parseInt(line.substring(x, x+1));
            }
        }
    }


    public void part1() throws FileNotFoundException {
        for(int y = 0; y < SIZE; y++ ){
            determineLeft(grid, y);
            determineRight(grid, y);
        }

        for(int x = 0; x < SIZE; x++) {
            determineTop(grid, x);
            determineBottom(grid, x);
        }

        int count = 0;
        for(int i = 0; i < SIZE*SIZE; i++) {
            if(visibility[i/SIZE][i%SIZE])
                count++;
        }
        System.out.println(count);
    }

    public void determineLeft(int[][] grid, int y) {
        int highest = -1;
        for(int x = 0; x < SIZE; x++) {
            if(grid[y][x] > highest) {
                visibility[y][x] = true;
                highest = grid[y][x];
            }
        }
    }

    public void determineRight(int[][] grid, int y) {
        int highest = -1;
        for(int x = SIZE - 1; x >= 0; x--) {
            if(grid[y][x] > highest) {
                visibility[y][x] = true;
                highest = grid[y][x];
            }
        }
    }

    public void determineTop(int[][] grid, int x) {
        int highest = -1;
        for(int y = 0; y < SIZE; y++) {
            if(grid[y][x] > highest) {
                visibility[y][x] = true;
                highest = grid[y][x];
            }
        }
    }

    public void determineBottom(int[][] grid, int x) {
        int highest = -1;
        for(int y = SIZE - 1; y >= 0; y--) {
            if(grid[y][x] > highest) {
                visibility[y][x] = true;
                highest = grid[y][x];
            }
        }
    }

    public void part2() throws FileNotFoundException {
        int max = 0;
        for(int y = 0; y < SIZE; y++) {
            for(int x = 0; x < SIZE; x++) {
                max = Math.max(max, scenicScore(grid, x, y));
            }
        }
        System.out.println(max);
    }

    public int scenicScore(int[][] grid, int x, int y) {
        int value = grid[y][x];
        //Left
        int left = 0;
        for(int i = x - 1; i >= 0; i--) {
            if(grid[y][i] >= value) {
                left++;
                break;
            }
            left++;
        }
        //Right
        int right = 0;
        for(int i = x + 1; i < SIZE; i++) {
            if(grid[y][i] >= value) {
                right++;
                break;
            }
            right++;
        }
        //Bottom
        int bottom = 0;
        for(int i = y + 1; i < SIZE; i++) {
            if(grid[i][x] >= value) {
                bottom++;
                break;
            }
            bottom++;
        }
        //Top
        int top = 0;
        for(int i = y - 1; i >= 0; i--) {
            if(grid[i][x] >= value) {
                top++;
                break;
            }
            top++;
        }
        int score = left * right * bottom * top;
        return score;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day8 day8 = new Day8();
        day8.readFile();
        day8.part1();
        day8.part2();
    }
}
