package nl.jellehulter.aoc.y2025.day3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day3 {

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day3\\input.txt"));
        List<String> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(s.nextLine());
        }
        int total = 0;
        for(String line : lines) {
            int a = 0;
            for(int i = 0; i < line.length(); i++) {
                for(int j = i + 1; j < line.length(); j++) {
                    int num = Integer.parseInt(line.charAt(i) + "" + line.charAt(j));
                    if(num > a) {
                        a = num;
                    }
                }
            }
            System.out.println(a);
            total += a;
        }
        System.out.println("Total: " + total);
    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day3\\input.txt"));
        List<String> lines = new ArrayList<>();
        while(s.hasNext()) {
            lines.add(s.nextLine());
        }
        long total = 0;
        for(String line : lines) {
            String a = line.substring(0, 12);

            long numA = Long.parseLong(a);
            for(int i = 12; i < line.length(); i++) {
                long nextBest = numA;
                char nextChar = line.charAt(i);
                for(int j = 0; j < a.length(); j++) {
                    StringBuilder testA = new StringBuilder(a);
                    testA.deleteCharAt(j);
                    testA.append(nextChar);
                    long newNumA = Long.parseLong(testA.toString());
                    if(newNumA > nextBest) {
                        nextBest = newNumA;
                    }
                }
                numA = nextBest;
                a = String.valueOf(numA);
            }
            System.out.println(a);
            total += numA;
        }
        System.out.println("Total: " + total);
    }

    public static void main(String[] args) {
        Day3 d = new Day3();
        try {
//            d.part1();
            d.part2();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
