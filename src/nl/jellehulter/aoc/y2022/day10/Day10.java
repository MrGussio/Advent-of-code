package nl.jellehulter.aoc.y2022.day10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day10 {

    int cycle = 0;
    int x = 1;
    int totalStrength = 0;

    StringBuilder crt;

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day10/day10.txt"));
        crt = new StringBuilder();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("noop")) {
                cycle();
                continue;
            }

            if(line.startsWith("addx")) {
                String[] split = line.split(" ");
                int val = Integer.parseInt(split[1]);
                cycle();
                cycle();
                x += val;

            }
        }
        System.out.println(totalStrength);
        System.out.println(crt);
    }

    public void cycle (){
        int position = cycle % 40;
        boolean inRange = position >= Math.max(x - 1, 0) && position <= Math.min(x + 1, 40);
        crt.append(inRange ? "#" : ".");
        cycle++;
        if(cycle % 40 == 0) crt.append("\n");
        if((cycle - 20) % 40 == 0) {
            totalStrength += cycle * x;
            System.out.println(cycle * x);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day10 day10 = new Day10();
        day10.part1();
    }

}
