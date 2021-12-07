package nl.jellehulter.aoc.day7;

import nl.jellehulter.aoc.day6.Day6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day7 {

    private ArrayList<Integer> integers = new ArrayList<>();
    private int maximum;
    private int minimum;

    Day7() {
        this.integers = new ArrayList<>();
        this.maximum = 0;
        this.minimum = Integer.MAX_VALUE;
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day7/input.txt"));
        String line = s.nextLine();
        String[] splitted = line.split(",");
        for(String split : splitted) {
            int i = Integer.parseInt(split);
            if(i > maximum) {
                maximum = i;
            } else if(i < minimum) {
                minimum = i;
            }
            integers.add(i);
        }
    }

    /**
     * Performs Part 1 of this puzzle.
     * It calculates the optimal crab position by trying out
     * all possible positions and returning the one with
     * its lowest possible fuel value.
     */
    public void part1() {
        int currentBest = -1;
        int fuelBest = Integer.MAX_VALUE;
        for(Integer i : integers) {
            int fuel = 0;
            for(Integer j : integers) {
                fuel += Math.abs(i - j);
            }

            if(fuel < fuelBest) {
                currentBest = i;
                fuelBest = fuel;
            }
        }
        System.out.println("Best position: " + currentBest);
        System.out.println("Fuel used: " + fuelBest);

    }

    /**
     * Performs part 2 of this puzzle.
     * Does the same as part 1, but then adds another for loop
     * which performs the summation from 1 to the absolute difference
     * between i and j.
     */
    public void part2() {
        int currentBest = -1;
        int fuelBest = Integer.MAX_VALUE;
        for(int i = minimum; i <= this.maximum; i++) {
            int fuel = 0;
            for(Integer j : integers) {
                for(int k = 1; k <= Math.abs(i-j); k++) {
                    fuel += k;
                }
            }

            if(fuel < fuelBest) {
                currentBest = i;
                fuelBest = fuel;
            }
        }
        System.out.println("Best position: " + currentBest);
        System.out.println("Fuel used: " + fuelBest);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day7 day7 = new Day7();
        day7.readFile();
        day7.part1();
        day7.part2();
    }

}
