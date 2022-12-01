package nl.jellehulter.aoc.y2021.day14;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day14 {

    private HashMap<String, String> rules;
    private HashMap<String, Long> pairCount;

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day14/input.txt"));
        rules = new HashMap<>();
        pairCount = new HashMap<>();
        String start = s.nextLine();
        for(int i = 0; i < start.length() - 1; i++) {
            String pair = start.substring(i, i+2);
            if(!pairCount.containsKey(pair)) {
                pairCount.put(pair, (long) 0);
            }
            pairCount.put(pair, pairCount.get(pair) + 1);
        }
        s.nextLine(); //Skip empty line
        while(s.hasNextLine()) {
            String line = s.nextLine();
            String[] split = line.split(" -> ");
            rules.put(split[0], split[1]);
        }
    }

    public void part1(int iterations) {
        for(int x = 0; x < iterations; x++) {
            HashMap<String, Long> newPairCount = new HashMap<>();
            for(String pair : pairCount.keySet()) {
                if(rules.containsKey(pair)) {
                    String mapped = rules.get(pair);
                    String firstPair = pair.charAt(0) + mapped;
                    String secondPair = mapped + pair.charAt(1);
                    if(!newPairCount.containsKey(firstPair)) {
                        newPairCount.put(firstPair, pairCount.get(pair));
                    } else {
                        newPairCount.put(firstPair, newPairCount.get(firstPair) + pairCount.get(pair));
                    }

                    if(!newPairCount.containsKey(secondPair)) {
                        newPairCount.put(secondPair, pairCount.get(pair));
                    } else {
                        newPairCount.put(secondPair, newPairCount.get(secondPair) + pairCount.get(pair));
                    }
                }
            }
            pairCount = newPairCount;
        }


        Map<Character, Long> charCount = new HashMap<>();
        for(String pair : pairCount.keySet()) {
            Character c = pair.charAt(0);
            if(charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + pairCount.get(pair));
            } else {
                charCount.put(c, pairCount.get(pair));
            }

            c = pair.charAt(1);
            if(charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + pairCount.get(pair));
            } else {
                charCount.put(c, pairCount.get(pair));
            }
        }
        long min = charCount.values().stream().min(Long::compare).get();
        long max = charCount.values().stream().max(Long::compare).get();
        //Round up, because a half means that we have a start or ending character of this size
        min = (min + 1) / 2;
        max = (max + 1) / 2;
        System.out.println(max-min);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day14 day14 = new Day14();
        day14.readFile();
        day14.part1(10);
        day14.readFile();
        day14.part1(40);
    }

}
