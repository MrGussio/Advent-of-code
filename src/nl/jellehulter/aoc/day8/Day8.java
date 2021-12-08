package nl.jellehulter.aoc.day8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day8 {

    ArrayList<String> inputLines = new ArrayList<>();

    enum Segment {
        A,
        B,
        C,
        D,
        E,
        F,
        G;

        public static Segment parse(String input) {
            assert input.length() == 1;
            return Segment.parse(input.charAt(0));
        }

        public static Segment parse(Character input) {
            switch (input) {
                case 'a':
                    return A;
                case 'b':
                    return B;
                case 'c':
                    return C;
                case 'd':
                    return D;
                case 'e':
                    return E;
                case 'f':
                    return F;
                case 'g':
                    return G;
                default:
                    return null;
            }
        }

        public Character toChar() {
            return this.name().toLowerCase().charAt(0);
        }
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day8/input.txt"));
        while(s.hasNext()) {
            inputLines.add(s.nextLine());
        }
    }

    public void part1() {
        int sum = 0;
        for(String line : inputLines) {
            String[] firstSplit = line.split("\\|");
            String[] stringSignals = firstSplit[0].trim().split(" ");
            String[] outputValues = firstSplit[1].trim().split(" ");
            for(String outputValue : outputValues) {
                if(outputValue.length() == 2 || outputValue.length() == 4 || outputValue.length() == 3 || outputValue.length() == 7) {
                    System.out.println(outputValue);
                    sum++;
                }
            }
        }
        System.out.println("Result is: " + sum);
    }

    public void part2() {
        HashMap<Segment, Segment> segments = new HashMap<>();
        HashMap<Integer, List<String>> sizeMap = new HashMap<>();
        for(String line : inputLines) {
            String[] firstSplit = line.split("\\|");
            String[] stringSignals = firstSplit[0].trim().split(" ");
            String[] outputValues = firstSplit[1].trim().split(" ");

            for(String signal : stringSignals) {
                if(!sizeMap.containsKey(signal.length())) {
                    sizeMap.put(signal.length(), new ArrayList<>());
                }
                sizeMap.get(signal.length()).add(signal);
            }

            segments = deduct(sizeMap, stringSignals);

        }
    }

    public HashMap<Segment, Segment>  deduct(HashMap<Integer, List<String>> sizeMap, String[] signals) {
        HashMap<Segment, Segment> mapping = new HashMap<>();

        if(sizeMap.containsKey(2) && sizeMap.containsKey(3)) { //Numbers 1 and 7
            List<Character> a = sizeMap.get(2).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            List<Character> b = sizeMap.get(3).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            b.removeAll(a);
            mapping.put(Segment.A, Segment.parse(b.iterator().next()));
        }

        if(sizeMap.containsKey(2) && sizeMap.containsKey(6)) { //Numbers 1 and 6
            for(String a : sizeMap.get(6)) { //Loop through 0, 6 and 9 and determine the number 6
                List<Character> aList = a.chars().mapToObj(c -> (char) c).collect(Collectors.toList()); //Potential number 6
                List<Character> b = sizeMap.get(2).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList()); //Number 1
                List<Character> one = new ArrayList<>(b);
                b.removeAll(aList);
                if(b.size() == 1) {
                    Character fSegment = b.iterator().next();
                    mapping.put(Segment.F, Segment.parse(fSegment));
                    one.remove(fSegment);
                    mapping.put(Segment.C, Segment.parse(one.iterator().next()));
                }
            }
        }

        //Find a number 9 using the stuff we know, determine the bottom line G
        if(sizeMap.containsKey(6) && sizeMap.containsKey(6)) {
            for(String a : sizeMap.get(6)) {
                List<Character> aList = a.chars().mapToObj(c -> (char) c).collect(Collectors.toList()); //Potential number 9
                aList.remove(mapping.get(Segment.C).toChar());
                aList.remove(mapping.get(Segment.F).toChar());
                aList.remove(mapping.get(Segment.F).toChar());
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        Day8 day8 = new Day8();
        day8.readFile();
        day8.part2();
    }


}
