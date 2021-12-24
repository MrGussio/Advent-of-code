package nl.jellehulter.aoc.day24;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day24 {

    enum Operation {
        INP,
        ADD,
        MUL,
        DIV,
        MOD,
        EQL
    }

    enum Variable {
        W,
        X,
        Y,
        Z;
    }

    class Instruction {

        Operation operation;
        Variable var1;
        Variable var2;
        int integerInput;

        public Instruction(Operation operation, String op) {
            if (operation != Operation.INP) {
                throw new IllegalArgumentException(operation.name() + " instruction expects tw operands, received one");
            }
            this.var1 = Variable.valueOf(op.toUpperCase(Locale.ROOT));
            this.operation = operation;
        }

        public Instruction(Operation operation, String op1, String op2) {
            if (operation == Operation.INP) {
                throw new IllegalArgumentException("inp instruction expects one operand, received two");
            }
            this.operation = operation;
            var1 = Variable.valueOf(op1.toUpperCase(Locale.ROOT));
            try {
                var2 = Variable.valueOf(op2.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                integerInput = Integer.parseInt(op2);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(operation).append(" ").append(var1);

            if (!operation.equals(Operation.INP)) {
                if (var2 != null) {
                    sb.append(" ").append(var2);
                } else {
                    sb.append(" ").append(integerInput);
                }
            }
            return sb.toString();
        }
    }

    int[] memory = new int[4];
    List<Instruction> instructionList = new ArrayList<>();
    Stack<Integer> inputs = new Stack<>();

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day24/input.txt"));
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] split = line.split(" ");
            if (split.length == 2) {
                instructionList.add(new Instruction(Operation.valueOf(split[0].toUpperCase(Locale.ROOT)), split[1]));
            } else {
                instructionList.add(new Instruction(Operation.valueOf(split[0].toUpperCase(Locale.ROOT)), split[1], split[2]));
            }
        }
    }

    public void compute(Instruction instruction) {
        switch (instruction.operation) {
            case INP:
                memory[instruction.var1.ordinal()] = inputs.pop();
                break;
            case ADD:
                if (instruction.var2 == null) {
                    memory[instruction.var1.ordinal()] += instruction.integerInput;
                } else {
                    memory[instruction.var1.ordinal()] += memory[instruction.var2.ordinal()];
                }
                break;
            case MUL:
                if (instruction.var2 == null) {
                    memory[instruction.var1.ordinal()] *= instruction.integerInput;
                } else {
                    memory[instruction.var1.ordinal()] *= memory[instruction.var2.ordinal()];
                }
                break;
            case DIV:
                if (instruction.var2 == null) {
                    memory[instruction.var1.ordinal()] /= instruction.integerInput;
                } else {
                    memory[instruction.var1.ordinal()] /= memory[instruction.var2.ordinal()];
                }
                break;
            case MOD:
                if (instruction.var2 == null) {
                    memory[instruction.var1.ordinal()] %= instruction.integerInput;
                } else {
                    memory[instruction.var1.ordinal()] %= memory[instruction.var2.ordinal()];
                }
                break;
            case EQL:
                if (instruction.var2 == null) {
                    memory[instruction.var1.ordinal()] = memory[instruction.var1.ordinal()] == instruction.integerInput ? 1 : 0;
                } else {
                    memory[instruction.var1.ordinal()] = memory[instruction.var1.ordinal()] == memory[instruction.var2.ordinal()] ? 1 : 0;
                }
                break;
        }
    }

    public void computeAll() {
        memory = new int[4];
        for (Instruction instruction : instructionList) {
            compute(instruction);
        }
    }

    public void part1() {
        String start = "92171126131911";
        inputs = new Stack<>();
        for(int i = start.length() - 1; i >= 0; i--) {
            inputs.push(Integer.parseInt(String.valueOf(start.charAt(i))));
        }
        computeAll();
        System.out.println(memory[Variable.Z.ordinal()]);
        if(memory[Variable.Z.ordinal()] == 0) {
            System.out.println(start);
            return;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day24 day24 = new Day24();
        day24.readFile();
        day24.part1();
        System.out.println();
    }

}
