package nl.jellehulter.aoc.y2021.day3;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class DayThree {

    private ArrayList<BigInteger> report;

    public DayThree() throws FileNotFoundException {
        this.report = new ArrayList<>();
        readFile();
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day3/input.txt"));
        while (s.hasNext()) {
            report.add(new BigInteger(s.nextLine(), 2));
        }
    }

    public void part1() {
        int size = 12;
        BigInteger result = new BigInteger("0", 2);
        for (int i = 0; i < 32; i++) {
            int count = 0;
            for (BigInteger number : report) {
                if (number.testBit(i))
                    count++;
            }
            if (count > report.size() / 2)
                result = result.setBit(i);
        }
        int epsilon = result.intValue();
        int gamma = (int) Math.pow(2, size) - epsilon - 1;
        System.out.println("Epsilon: " + epsilon);
        System.out.println("Gamma:" + (gamma));
        System.out.println("Result:" + (epsilon * gamma));
    }

    public int calculatePart2(boolean oxygen) {
        ArrayList<BigInteger> remaining = report;
        int size = 12;
        for (int i = size - 1; i >= 0; i--) {
            ArrayList<BigInteger> zeroes = new ArrayList<>();
            ArrayList<BigInteger> ones = new ArrayList<>();
            for(BigInteger number : remaining) {
                if(number.testBit(i)) {
                    ones.add(number);
                } else {
                    zeroes.add(number);
                }
            }
            if(oxygen) {
                if(zeroes.size() > ones.size()) {
                    remaining = zeroes;
                } else {
                    remaining = ones;
                }
            } else {
                if(zeroes.size() <= ones.size() && zeroes.size() > 0) {
                    remaining = zeroes;
                } else {
                    remaining = ones;
                }
            }
            if(remaining.size() == 1) {
                return remaining.get(0).intValue();
            }
        }
        return -1;
    }

    public void part2() {
        int oxygen = calculatePart2(true);
        int co2 = calculatePart2(false);
        System.out.println("Oxygen gen: " + oxygen);
        System.out.println("CO2: " + co2);
        System.out.println("Result: " + (oxygen * co2));
    }


    public static void main(String[] args) throws FileNotFoundException {
        DayThree dayThree = new DayThree();
        dayThree.part1();
        dayThree.part2();
    }

}
