package nl.jellehulter.aoc.day2;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DayTwo {

    public static class Movement {

        public enum Direction {
            FORWARD,
            DOWN,
            UP
        }

        private int movementSize;
        private Direction direction;

        public Movement(String line) throws Exception {
            String[] split = line.split(" ");
            assert split.length == 2;
            switch (split[0]) {
                case "forward":
                    this.direction = Direction.FORWARD;
                    break;
                case "down":
                    this.direction = Direction.DOWN;
                    break;
                case "up":
                    this.direction = Direction.UP;
                    break;
                default:
                    throw new Exception("Unknown direction " + split[0]);
            }

            this.movementSize = Integer.parseInt(split[1]);
        }

        public int getMovementSize() {
            return movementSize;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    private ArrayList<Movement> movements;
    private int depth;
    private int horizontalPosition;
    private int aim;

    public DayTwo() throws Exception {
        this.movements = new ArrayList<>();
        this.depth = 0;
        this.horizontalPosition = 0;
        this.aim = 0;
        readFile();
    }

    public void readFile() throws Exception {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/daytwo.txt"));
        while (s.hasNext()) {
            movements.add(new Movement(s.nextLine()));
        }
    }

    public int processMoves() {
        for (Movement move : movements) {
            switch (move.getDirection()) {
                case DOWN:
                    depth += move.getMovementSize();
                    break;
                case UP:
                    depth -= move.getMovementSize();
                    break;
                case FORWARD:
                    horizontalPosition += move.getMovementSize();
                    break;
            }
        }
        return depth * horizontalPosition;
    }

    public int processMovesPart2() {
        for (Movement move : movements) {
            switch (move.getDirection()) {
                case DOWN:
                    aim += move.getMovementSize();
                    break;
                case UP:
                    aim -= move.getMovementSize();
                    break;
                case FORWARD:
                    horizontalPosition += move.getMovementSize();
                    depth += aim * move.getMovementSize();
                    break;
            }
        }
        return depth * horizontalPosition;
    }

    public static void main(String[] args) throws Exception {
        DayTwo dayTwo = new DayTwo();
        System.out.println(dayTwo.processMoves());
        dayTwo = new DayTwo();
        System.out.println(dayTwo.processMovesPart2());
    }

}
