package nl.jellehulter.aoc.y2021.day6;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Day6 {

    class Fish {

        private int timer;

        public Fish(int timer) {
            this.timer = timer;
        }

        public boolean update() {
            timer--;
            if (timer == -1) {
                timer = 6;
                return true;
            }
            return false;
        }

        public int getTimer() {
            return timer;
        }
    }

    private CopyOnWriteArrayList<Fish> fishes;
    private HashMap<Integer, BigInteger> integerFishes;

    public Day6() {
        this.fishes = new CopyOnWriteArrayList<>();
        this.integerFishes = new HashMap<>();
    }

    /**
     * Reads the file and prepares the arrays for the object-oriented
     * way and the faster-approach.
     * @throws FileNotFoundException
     */
    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day6/input.txt"));
        String line = s.nextLine();
        String[] split = line.split(",");
        for (String timer : split) {
            fishes.add(new Fish(Integer.parseInt(timer)));
            int parsed = Integer.parseInt(timer);
            if(integerFishes.containsKey(parsed)) {
                integerFishes.put(parsed, integerFishes.get(parsed).add(BigInteger.ONE));
            } else {
                integerFishes.put(parsed, BigInteger.valueOf(1));
            }

        }
    }

    /**
     * Prints the current object-oriented output
     * @param days The current day which it represents
     */
    public void print(int days) {
        StringBuilder stringBuilder = new StringBuilder("After " + days + ": ");
        for (Fish fish : fishes) {
            stringBuilder.append(fish.getTimer()).append(",");
        }
        System.out.println(stringBuilder);
    }

    /**
     * Runs the simulation using the object-oriented way, but this
     * does not scale well exponentially hence you might probably
     * run out of heap space pretty quickly.
     * @param days Days to simulate
     * @return The amount of fish after {@code days} amount of days
     */
    public int runSimulation(int days) {
        for (int i = 0; i < days; i++) {
//            print(i);
            System.out.println(i);
            for (Fish fish : fishes) {
                if (fish.update()) {
                    fishes.add(new Fish(8));
                }
            }
        }
//        print(days);
        return fishes.size();
    }

    //My second improvement without use of objects, nevertheless too big to process
//    public int improvedSimulation(int days) {
//        for (int i = 0; i < days; i++) {
//            int newFish = 0;
//            for (int j = 0; j < integerFishes.size(); j++) {
//                int val = integerFishes.get(j);
//                if (val == 0) {
//                    integerFishes.set(j, 6);
//                    newFish++;
//                } else {
//                    integerFishes.set(j, val - 1);
//                }
//            }
//            for(int j = 0; j < newFish; j++) {
//                integerFishes.add(8);
//            }
//            System.out.println(i);
//        }
//        return integerFishes.size();
//    }

    /**
     * This optimisation makes use of a HashMap which counts the amount of fish
     * in a certain state. So if Key=4 and Value=10, it means that there are 10 fish with
     * a timer value of 4. It then moves this amount to the next state by setting key-1=amount,
     * and if key-1 is -1, it moves it to 6 and 8 because new baby fishes are born.
     * @param days
     * @return
     */
    public BigInteger secondImprovedOptimisation(int days) {
        for (int i = 0; i < days; i++) {
            HashMap<Integer, BigInteger> newMap = new HashMap<>();
            for(Integer key : integerFishes.keySet()) {
                BigInteger amount = integerFishes.get(key);
                if(key == 0) {
                    if(newMap.containsKey(key)) {
                        newMap.put(key, newMap.get(key).add(amount));
                    } else {
                        newMap.put(6, amount);
                    }
                    newMap.put(8,amount);
                } else {
                    if(newMap.containsKey(key - 1)) {
                        newMap.put(key - 1, newMap.get(key - 1).add(amount));
                    } else {
                        newMap.put(key - 1, amount);
                    }
                }
            }
            integerFishes = newMap;
            printOptimized(i);
        }
        BigInteger sum = BigInteger.ZERO;
        for(Integer key : integerFishes.keySet()) {
            BigInteger value = integerFishes.get(key);
            sum = sum.add(value);
        }
        return sum;
    }

    /**
     * Prints the current amount of fish per state to the console
     * @param day Current day to be used in the output
     */
    public void printOptimized(int day) {
        StringBuilder stringBuilder = new StringBuilder("After day" + day + ": ");
        for(Integer key : integerFishes.keySet()) {
            stringBuilder.append(key).append("=").append(integerFishes.get(key)).append(",");
        }
        System.out.println(stringBuilder);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day6 day6 = new Day6();
        day6.readFile();
        System.out.println(day6.secondImprovedOptimisation(256));
    }

}
