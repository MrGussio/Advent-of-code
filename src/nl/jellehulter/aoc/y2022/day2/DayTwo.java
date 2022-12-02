package nl.jellehulter.aoc.y2022.day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DayTwo {

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day2/daytwo.txt"));
        int total = 0;
        while(scanner.hasNext()) {
            int score = 0;
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            char opp = split[0].charAt(0);
            char me = split[1].charAt(0);

            switch (me) {
                case 'X':
                    score += 1;
                    if(opp == 'A') score += 3;
                    if(opp == 'B') score += 0;
                    if(opp == 'C') score += 6;
                    break;
                case 'Y':
                    score += 2;
                    if(opp == 'A') score += 6;
                    if(opp == 'B') score += 3;
                    if(opp == 'C') score += 0;
                    break;
                case 'Z':
                    score += 3;
                    if(opp == 'A') score += 0;
                    if(opp == 'B') score += 6;
                    if(opp == 'C') score += 3;
                    break;
            }
            total += score;
        }
        System.out.println(total);
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day2/daytwo.txt"));
        int total = 0;
        while(scanner.hasNext()) {
            int score = 0;
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            char opp = split[0].charAt(0);
            char me = split[1].charAt(0);

            switch (me) {
                case 'X':
                    score += 0;
                    if(opp == 'A') score += 3; //Need to pick scissors
                    if(opp == 'B') score += 1; //Need to pick rock
                    if(opp == 'C') score += 2; //Need to pick paper
                    break;
                case 'Y':
                    score += 3;
                    if(opp == 'A') score += 1; //Match the other
                    if(opp == 'B') score += 2;
                    if(opp == 'C') score += 3;
                    break;
                case 'Z':
                    score += 6;
                    if(opp == 'A') score += 2; //Need to pick paper
                    if(opp == 'B') score += 3; //Need to pick scissors
                    if(opp == 'C') score += 1; //Need to pick rock
                    break;
            }
            total += score;
        }
        System.out.println(total);
    }

    public static void main(String[] args) throws FileNotFoundException {
        DayTwo dayTwo = new DayTwo();
        dayTwo.part1();
        dayTwo.part2();
    }
}
