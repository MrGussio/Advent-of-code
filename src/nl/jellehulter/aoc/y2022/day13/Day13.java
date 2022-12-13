package nl.jellehulter.aoc.y2022.day13;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 {

    abstract class PairItem {

    }
    class Pair extends PairItem {

        public List<PairItem> contents = new ArrayList<>();

        Pair() {}

        Pair(Value value) {
            contents.add(value);
        }

        @Override
        public String toString() {
            return contents.stream().map(PairItem::toString).collect(Collectors.joining(",", "[", "]"));
        }
    }

    class Value extends PairItem {
        int value;

        public Value(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value+"";
        }
    }

    public Pair parse(BufferedReader reader) throws IOException {
        Pair pair = new Pair();
        StringBuilder currentInput = new StringBuilder();
        //Start at i=1 to skip the [ of the current list
        int i;
        while((i = reader.read()) != -1) {
            char c = (char) (i);
            switch(c) {
                case '[':
                    pair.contents.add(parse(reader));
                //If we see a ',' add the seen value to the pair if available
                case ',':
                    if(currentInput.length() > 0)
                        pair.contents.add(new Value(Integer.parseInt(currentInput.toString())));
                    currentInput = new StringBuilder();
                    break;
                //End this pair and add the seen value if available
                case ']':
                    if(currentInput.length() > 0)
                        pair.contents.add(new Value(Integer.parseInt(currentInput.toString())));
                    return pair;
                //Must be integer
                default:
                    currentInput.append(c);
                    break;
            }
        }
        return pair;
    }

    List<Pair> pairs;

    public void readFile() throws IOException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day13/day13.txt"));
        pairs = new ArrayList<>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("")) continue;
            pairs.add((Pair) parse(new BufferedReader(new StringReader(line))).contents.get(0));
        }
    }

    public void part1() throws FileNotFoundException {
        int i = 0;
        int count = 0;
        while(i*2 < pairs.size()) {
            Pair pair1 = pairs.get(i*2);
            Pair pair2 = pairs.get(i*2 + 1);
//            System.out.println("Pair " + (i+1) + ":");
//            System.out.println(pair1);
//            System.out.println(pair2);
            int res = checkOrder(pair1, pair2);
//            System.out.println(res == 1 ? "Right order" : "Not right order");
            if(res == 1) count += i+1;
            i++;
        }
        System.out.println("Final score: " + count);
    }

    public void part2() throws IOException {
        List<Pair> pairs = new ArrayList<>(this.pairs);
        Pair divide1 = parse(new BufferedReader(new StringReader("[[2]]")));
        Pair divide2 = parse(new BufferedReader(new StringReader("[[6]]")));
        pairs.add(divide1);
        pairs.add(divide2);

        pairs.sort(Collections.reverseOrder(this::checkOrder));
//        for(Pair p : pairs) {
//            System.out.println(p);
//        }

        System.out.println((pairs.indexOf(divide1) + 1) * (pairs.indexOf(divide2) + 1));
    }

    public int checkOrder(Pair left, Pair right) {
        for(int i = 0; i < left.contents.size(); i++) {
            PairItem l1 = left.contents.get(i);
            if(i == right.contents.size())
                return -1;
            PairItem r1 = right.contents.get(i);
            if(l1 instanceof Value leftValue && r1 instanceof Value rightValue) {
                if(leftValue.value < rightValue.value) return 1;
                if(leftValue.value > rightValue.value) return -1;
                continue;
            } else if(l1 instanceof Pair leftPair && r1 instanceof Pair rightPair) {
                int res = checkOrder(leftPair, rightPair);
                if(res == 0) continue;
                return res;
            } else if(l1 instanceof Pair leftPair && r1 instanceof Value rightValue) {
                int res = checkOrder(leftPair, new Pair(rightValue));
                if(res == 0) continue;
                return res;
            } else if(l1 instanceof Value leftValue && r1 instanceof Pair rightPair) {
                int res = checkOrder(new Pair(leftValue), rightPair);
                if(res == 0) continue;
                return res;
            }
        }
        //Check whether the right is smaller than the left in the case the for loop is never entered
        //if the left list is empty.
        //This was needed to cover the case [] [3]
        return Integer.compare(right.contents.size(), left.contents.size());
//        return 0 <
    }


    public static void main(String[] args) throws IOException {
        Day13 day13 = new Day13();
        day13.readFile();
        day13.part1();
        day13.part2();
    }
}
