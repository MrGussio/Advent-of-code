package nl.jellehulter.aoc.y2022.day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DayOne {

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day1/dayone.txt"));
        int max = 0;
        int currentMax = 0;
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            if(line.equals("")) {
                max = Math.max(max, currentMax);
                currentMax = 0;
                continue;
            }
            currentMax += Integer.parseInt(line);
        }
        System.out.println(max);
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day1/dayone.txt"));
        int currentTotal = 0;
        List<Integer> sums = new ArrayList<>();
        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            if(line.equals("")) {
                sums.add(currentTotal);
                currentTotal = 0;
                continue;
            }
            currentTotal += Integer.parseInt(line);
        }
        sums.sort(Comparator.reverseOrder());
        long total = 0;
        for(int i = 0; i < 3; i++) {
//            System.out.println(sums.get(i));
            total += sums.get(i);
        }
        System.out.println(total);
    }

    public static void main(String[] args) throws FileNotFoundException {
        DayOne dayOne = new DayOne();
        dayOne.part1();
        dayOne.part2();
    }

}
