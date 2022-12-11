package nl.jellehulter.aoc.y2022.day11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11 {

    class Monkey {

        Deque<Long> items;
        String operation;
        int test;
        int ifTrue;
        int ifFalse;
        long seenItems;

        public Monkey(String items, String operation, String test, String ifTrue, String ifFalse) {
            this.items = new ArrayDeque<>();
            items = items.trim().substring("Starting items: ".length());
            this.items.addAll(Arrays.stream(items.split(",")).map(String::trim).map(Long::parseLong).toList());
            this.operation = operation.trim().substring("Operation: new = ".length());
            this.test = Integer.parseInt(test.trim().substring("Test: divisible by ".length()));
            this.ifTrue = Integer.parseInt(ifTrue.trim().substring("If true: throw to monkey ".length()));
            this.ifFalse = Integer.parseInt(ifFalse.trim().substring("If false: throw to monkey ".length()));
            seenItems = 0   ;
        }

        public long doOperation(long i) {
            if(operation.equals("old * old"))
                return i*i;

            if(operation.startsWith("old + "))
                return i + Integer.parseInt(operation.substring("old + ".length()));

            if(operation.startsWith("old * "))
                return i * Integer.parseInt(operation.substring("old * ".length()));

            throw new RuntimeException("Invalid operation: "  + operation);
        }

        @Override
        public String toString() {
            return items.stream().map(e -> e.toString()).collect(Collectors.joining(", "));
        }
    }

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day11/day11.txt"));
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            monkeys.add(new Monkey(
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine()
            ));
            if(scanner.hasNextLine())
                scanner.nextLine();
        }
        //20 rounds
        for(int i = 0; i < 20; i++) {
            for(Monkey monkey : monkeys) {
                while(!monkey.items.isEmpty()) {
                    long j = monkey.items.pop();
                    j = monkey.doOperation(j);
                    monkey.seenItems++;
                    j /= 3;
                    int nextMonkey = j % monkey.test == 0 ? monkey.ifTrue : monkey.ifFalse;
                    monkeys.get(nextMonkey).items.add(j);
                }
            }
            System.out.println("Round " + (i+1) + ":");
            for(Monkey m : monkeys) {
                System.out.println(m);
            }
            System.out.println("=================");
        }

        for(Monkey m : monkeys) {
            System.out.println(m.seenItems);
        }

        List<Long> seenItems = monkeys.stream().mapToLong(m -> m.seenItems).boxed().sorted(Collections.reverseOrder()).toList();
        System.out.println(seenItems.get(0) * seenItems.get(1));
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day11/day11.txt"));
        List<Monkey> monkeys = new ArrayList<>();
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            monkeys.add(new Monkey(
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine(),
                    scanner.nextLine()
            ));
            if(scanner.hasNextLine())
                scanner.nextLine();
        }
        int lcm = lcm(monkeys.stream().map(m -> m.test).toList());

        for(int i = 0; i < 10000; i++) {
            for(Monkey monkey : monkeys) {
                while(!monkey.items.isEmpty()) {
                    long j = monkey.items.pop();
                    j = monkey.doOperation(j) % lcm;
                    monkey.seenItems++;
                    int nextMonkey = j % monkey.test == 0 ? monkey.ifTrue : monkey.ifFalse;
                    monkeys.get(nextMonkey).items.add(j);
                }
            }
        }

        for(Monkey m : monkeys) {
            System.out.println(m.seenItems);
        }

        List<Long> seenItems = monkeys.stream().mapToLong(m -> m.seenItems).boxed().sorted(Collections.reverseOrder()).toList();
        System.out.println(seenItems.get(0) * seenItems.get(1));
    }

    public int gcd(int a, int b) {
        if(b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public int lcm(List<Integer> ints) {
        int lcm = ints.get(0);
        for(int i = 1; i < ints.size(); i++) {
            int x = ints.get(i);
            lcm = (lcm * x) / gcd(lcm, x);
        }
        return lcm;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day11 day11 = new Day11();
        day11.part2();
    }
}
