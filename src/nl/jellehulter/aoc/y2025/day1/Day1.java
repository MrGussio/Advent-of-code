package nl.jellehulter.aoc.y2025.day1;

import nl.jellehulter.aoc.y2021.day19.Day19;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day1 {

    private void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day1\\input.txt"));

        int dial = 50;
        int password = 0;
        while (s.hasNext()) {
            String l = s.nextLine();
            int direction = l.startsWith("L") ? -1 : 1;
            int value = Integer.parseInt(l.substring(1));
            dial = (dial + direction * value + 100) % 100;
            System.out.println(dial);
            if (dial == 0)
                password++;
        }

        System.out.printf("Password: %s%n", password);
    }

    private void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new FileInputStream("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day1\\input.txt"));

        int dial = 50;
        int password = 0;
        while (s.hasNext()) {
            while (s.hasNext()) {
                String l = s.nextLine();
                int direction = l.startsWith("L") ? -1 : 1;
                int value = Integer.parseInt(l.substring(1));
                int passes0 = 0;
                for(int i = 0; i < value; i++) {
                    dial += direction;
                    if(dial == 0) {
                        passes0++;
                    }
                    if(dial == 100) {
                        dial = 0;
                        passes0++;
                    }
                    if(dial == -1) {
                        dial = 99;
                    }
                }
                password += passes0;
                System.out.printf("%s dial: %s, passes 0: %s%n", l, dial, passes0);
            }
        }

        System.out.printf("Password: %s%n", password);
    }

    public static void main(String[] args) {
        Day1 d = new Day1();
        try {
            d.part2();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
