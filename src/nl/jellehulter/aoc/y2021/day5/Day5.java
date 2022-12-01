package nl.jellehulter.aoc.y2021.day5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day5 {

    class Vent {

        Coordinate start;
        Coordinate end;

        public Vent(String input) {
            String[] split = input.split(" -> ");
            String[] start = split[0].split(",");
            String[] end = split[1].split(",");
            this.start = new Coordinate(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
            this.end = new Coordinate(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
        }

        /**
         * Gets the maximum {@code x} coordinate of this vent
         * @return the maximum {@code x} coordinate
         */
        public int maximumX() {
            return Math.max(start.getX(), end.getX());
        }

        /**
         * Gets the maximum {@code y} coordinate of this vent
         * @return the maximum {@code y} coordinate
         */
        public int maximumY() {
            return Math.max(start.getY(), end.getY());
        }

        public boolean intersects(int x, int y) {
            int smallX = Math.min(start.getX(), end.getX());
            int bigX = Math.max(start.getX(), end.getX());
            int smallY = Math.min(start.getY(), end.getY());
            int bigY = Math.max(start.getY(), end.getY());
            return x >= smallX && x <= bigX &&
                    y >= smallY && y <= bigY;
        }

        /**
         * Determines if the current vent is a straight one
         * @return True if it is a straight one, false if it is a diagonal one
         */
        public boolean isStraight() {
            return start.getX() == end.getX() || start.getY() == end.getY();
        }

        public ArrayList<Coordinate> getCoordinates() {
            int smallX = Math.min(start.getX(), end.getX());
            int bigX = Math.max(start.getX(), end.getX());
            int smallY = Math.min(start.getY(), end.getY());
            int bigY = Math.max(start.getY(), end.getY());
            ArrayList<Coordinate> coordinates = new ArrayList<>();
            if(isStraight()) {
                for(int x = smallX; x <= bigX; x++) {
                    for(int y = smallY; y <= bigY; y++) {
                        coordinates.add(new Coordinate(x, y));
                    }
                }
            } else {
                //y=ax+b
                int a = (start.getY() - end.getY()) / (start.getX() - end.getX()); //A
                int b = start.getY() - a * start.getX();
                //Loop over X coordinates and determine corresponding Y coordinate
                for(int x = smallX; x <= bigX; x++) {
                    coordinates.add(new Coordinate(x, a*x+b));
                }
            }
            return coordinates;
        }
    }

    class Coordinate {

        private int x;
        private int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coordinate{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private ArrayList<Vent> vents;
    private int X_SIZE;
    private int Y_SIZE;
    private int[][] grid;

    public Day5() throws FileNotFoundException {
        vents = new ArrayList<>();
        readFile();
        grid = new int[Y_SIZE + 1][X_SIZE + 1];
    }

    /**
     * Reads a certain input file to perform the exercise
     * @throws FileNotFoundException When the file is not found
     */
    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day5/input.txt"));
        while (s.hasNextLine()) {
            Vent vent = new Vent(s.nextLine());
            X_SIZE = Math.max(X_SIZE, vent.maximumX());
            Y_SIZE = Math.max(Y_SIZE, vent.maximumY());
            vents.add(vent);
        }

    }

    /**
     * Loops over all hydrothermal vents and creates the grid of how many
     * vents are at a certain coordinate, for only the straight vents
     */
    public void calculateGridPart1() {
        for(Vent vent : vents) {
            if(!vent.isStraight())
                continue;
            for(Coordinate coordinate : vent.getCoordinates()) {
                grid[coordinate.getY()][coordinate.getX()]++;
            }
        }
    }

    /**
     * Loops over all hydrothermal vents and creates the grid of how many
     * vents are at a certain coordinate, for straight and diagonal vents
     */
    public void calculateGridPart2() {
        for (Vent vent : vents) {
            for (Coordinate coordinate : vent.getCoordinates()) {
                grid[coordinate.getY()][coordinate.getX()]++;
            }
        }
    }

    /**
     * Calculates the overlap of the grid
     * @return The overlap
     */
    public int getOverlap() {
        int result = 0;
        for (int y = 0; y <= Y_SIZE; y++) {
            for (int x = 0; x <= X_SIZE; x++) {
                if(grid[y][x] > 1)
                    result++;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int y = 0; y <= Y_SIZE; y++) {
            for(int x = 0; x <= X_SIZE; x++) {
                stringBuilder.append(grid[y][x])
                        .append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day5 day5 = new Day5();
        day5.calculateGridPart1();
        System.out.println(day5);
        System.out.println(day5.getOverlap());
        day5 = new Day5();
        day5.calculateGridPart2();
        System.out.println(day5);
        System.out.println(day5.getOverlap());
    }

}
