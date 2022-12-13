package nl.jellehulter.aoc.y2022.day13;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    public Pair parse(String input) {
        Pair pair = new Pair();
        StringBuilder currentInput = new StringBuilder();
        for(int i = 1; i < input.length(); i++) {
            switch(input.charAt(i)) {
                case '[':
                    pair.contents.add(parse(input.substring(i, input.indexOf(']', i) + 1)));
                    i = input.indexOf(']', i) + 1;//Skip until after the first next closing bracket
                case ',':
                case ']':
                    if(currentInput.length() > 0)
                        pair.contents.add(new Value(Integer.parseInt(currentInput.toString())));
                    currentInput = new StringBuilder();
                    break;
                default://Must be integer
                    currentInput.append(input.charAt(i));
                    break;
            }
        }
        return pair;
    }

    List<Pair> pairs;

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day13/day13.txt"));
        pairs = new ArrayList<>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("")) continue;
            pairs.add(parse(line));
        }
    }

    public void part1() throws FileNotFoundException {
        int i = 0;
        int count = 0;
        while(i*2 < pairs.size()) {
            Pair pair1 = pairs.get(i*2);
            Pair pair2 = pairs.get(i*2 + 1);
            System.out.println("Pair " + (i+1) + ":");
            System.out.println(pair1);
            System.out.println(pair2);
            int res = checkOrder(pair1, pair2);
            System.out.println(res == 1 ? "Right order" : "Not right order");
            if(res == 1) count+= (i+1);
            i++;
        }
        System.out.println("Final score: " + count);
    }

    public void part2() throws FileNotFoundException {
        List<Pair> pairs = new ArrayList<>(this.pairs);
        Pair divide1 = parse("[[2]]");
        Pair divide2 = parse("[[6]]");
        pairs.add(divide1);
        pairs.add(divide2);

        pairs.sort(Collections.reverseOrder(this::checkOrder));
        for(Pair p : pairs) {
            System.out.println(p);
        }

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
        return Integer.compare(right.contents.size(), left.contents.size());
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day13 day13 = new Day13();
        day13.readFile();
        day13.part1();
        day13.part2();
    }
}
