package nl.jellehulter.aoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DayOne {

    public ArrayList<Integer> depths;
    public ArrayList<Integer> threeDepths;

    public DayOne() {
        depths = new ArrayList<>();
    }

    public void compareDepths(ArrayList<Integer> list) {
        int increases = 0;
        for(int i = 1; i < list.size(); i++) {
            if(list.get(i - 1) < list.get(i)) {
                increases++;
            }
        }
        System.out.println(increases);
    }

    public void calculateThreeDepths() {
        threeDepths = new ArrayList<>();
        for(int i = 2; i < depths.size(); i++) {
            int x = depths.get(i-2) + depths.get(i-1) + depths.get(i);
            System.out.println(x);
            threeDepths.add(x);
        }
    }



    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/dayone.txt"));
        while(scanner.hasNext()) {
            depths.add(Integer.parseInt(scanner.nextLine().strip()));
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        DayOne dayOne = new DayOne();
        dayOne.readFile();
        dayOne.compareDepths(dayOne.depths);

        dayOne.calculateThreeDepths();
        dayOne.compareDepths(dayOne.threeDepths);
    }

}
