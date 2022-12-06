package nl.jellehulter.aoc.y2022.day6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day6 {

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day6/day6.txt"));
        String line = scanner.nextLine();
        for(int i = 0; i < line.length() - 4; i++) {
            String substr = line.substring(i, i + 4);
            if(substr.chars().distinct().count() == 4) {
                System.out.println(i + 4);
                System.out.println(substr);
                break;
            }
        }
        System.out.println("No sequence found");
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day6/day6.txt"));
        String line = scanner.nextLine();
        for(int i = 0; i < line.length() - 14; i++) {
            String substr = line.substring(i, i + 14);
            if(substr.chars().distinct().count() == 14) {
                System.out.println(i + 14);
                System.out.println(substr);
                break;
            }
        }
        System.out.println("No sequence found");
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day6 day6 = new Day6();
        day6.part1();
        day6.part2();
    }
}
