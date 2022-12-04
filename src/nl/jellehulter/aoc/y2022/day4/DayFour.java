package nl.jellehulter.aoc.y2022.day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DayFour {

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day4/dayfour.txt"));
        int counter = 0;
        while(scanner.hasNext()) {
            String pairs = scanner.nextLine();
            String[] split = pairs.split(",");
            String[] pair1 = split[0].split("-");
            String[] pair2 = split[1].split("-");
            int a = Integer.parseInt(pair1[0]);
            int b = Integer.parseInt(pair1[1]);
            int c = Integer.parseInt(pair2[0]);
            int d = Integer.parseInt(pair2[1]);

            if((a <= c && b >= d) || (a >= c && b <= d)) {
                counter++;
            }
        }
        System.out.println(counter);
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day4/dayfour.txt"));
        int counter = 0;
        while(scanner.hasNext()) {
            String pairs = scanner.nextLine();
            String[] split = pairs.split(",");
            String[] pair1 = split[0].split("-");
            String[] pair2 = split[1].split("-");
            int a = Integer.parseInt(pair1[0]);
            int b = Integer.parseInt(pair1[1]);
            int c = Integer.parseInt(pair2[0]);
            int d = Integer.parseInt(pair2[1]);

            if((a >= c && a <= d) || (b >= c && b <= d) || (c >= a && c <= b) || (d >= a && d <= b)) {
                counter++;
            }
        }
        System.out.println(counter);
    }

    public static void main(String[] args) throws FileNotFoundException {
        DayFour dayFour = new DayFour();
        dayFour.part1();
        dayFour.part2();
    }
}
