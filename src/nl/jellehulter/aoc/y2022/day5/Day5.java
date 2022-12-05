package nl.jellehulter.aoc.y2022.day5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day5 {

    List<Stack<Character>> stacks;
    List<Instruction> instructions;
    int amountOfStacks = 9;

    record Instruction(int amount, int from, int to) {
        Instruction(int amount, int from, int to) {
            this.amount = amount;
            this.from = from;
            this.to = to;
        }
    }
    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day5/day5.txt"));
        stacks = new ArrayList<>();
        instructions = new ArrayList<>();
        for(int i = 0; i < amountOfStacks; i++) {
            stacks.add(new Stack<>());
        }

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.charAt(1) == '1') { //Line of stack numbers
                break;
            }
            for(int i = 0; i < amountOfStacks; i++) {
                int index = 1 + 4 * i;
                if(index > line.length()) break;
                char c = line.charAt(index);
                if(c == ' ') continue;
                stacks.get(i).push(c);
            }
        }

        for(int i = 0; i < stacks.size(); i++) {
            Stack<Character> s = stacks.get(i);
            Stack<Character> reversed = new Stack<>();
            while(!s.empty()) {
                reversed.push(s.pop());
            }
            stacks.set(i, reversed);
        }
        scanner.nextLine(); //Skip empty line

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            instructions.add(new Instruction(
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[3]) - 1,
                    Integer.parseInt(split[5]) - 1
            ));
        }
    }

    public void part1() throws FileNotFoundException {
        readFile();
        for(Instruction instruction : instructions) {
            for(int i = 0; i < instruction.amount; i++) {
                char c = stacks.get(instruction.from). pop();
                stacks.get(instruction.to).push(c);
            }
        }
        StringBuilder result = new StringBuilder();
        for(Stack s : stacks) {
            result.append(s.peek());
        }
        System.out.println(result);
    }

    public void part2() throws FileNotFoundException {
        readFile();
        for(Instruction instruction: instructions) {
            Stack<Character> s = new Stack<>();
            for(int i = 0; i < instruction.amount; i++) {
                s.push(stacks.get(instruction.from).pop());
            }
            while(!s.empty()) {
                stacks.get(instruction.to).push(s.pop());
            }
        }

        StringBuilder result = new StringBuilder();
        for(Stack s : stacks) {
            result.append(s.peek());
        }
        System.out.println(result);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day5 day5 = new Day5();
        day5.part1();
        day5.part2();
    }

}
