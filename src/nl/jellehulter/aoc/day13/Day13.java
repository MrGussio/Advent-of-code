package nl.jellehulter.aoc.day13;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    private List<Point> points;
    private List<Fold> folds;

    static class Fold {

        enum Direction {
            X,
            Y;

            public static Direction parse(String input) {
                if (input.equals("x"))
                    return Direction.X;
                if (input.equals("y"))
                    return Direction.Y;
                return null;
            }
        }

        private int value;
        private Direction direction;

        public Fold(Direction direction, int value) {
            this.direction = direction;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public Direction getDirection() {
            return direction;
        }
    }


    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day13/input.txt"));
        points = new ArrayList<>();
        folds = new ArrayList<>();
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.equals("")) {
                break;
            }
            String[] split = line.split(",");
            Point p = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            points.add(p);
        }

        while (s.hasNextLine()) {
            String line = s.nextLine();
            line = line.replace("fold along ", "");
            String[] split = line.split("=");
            folds.add(new Fold(Fold.Direction.parse(split[0]), Integer.parseInt(split[1])));
        }
    }

    public void printGrid() {
        int width = points.stream().max(Comparator.comparingInt(p -> p.x)).get().x + 1;
        int height = points.stream().max(Comparator.comparingInt(p -> p.y)).get().y + 1;
        int[] grid = new int[width*height];
        points.forEach(point -> grid[point.y * width + point.x] = 1);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(grid[y*width + x] == 1 ? "#" : ".");
            }
            System.out.println();
        }
    }

    public void fold(Fold fold) {
        if(fold.getDirection().equals(Fold.Direction.X)) {
           List<Point> toBeFolded = points.stream().filter(point -> point.getX() > fold.getValue()).collect(Collectors.toList());
           toBeFolded.forEach(point -> point.translate((int) (-2*Math.abs(point.getX() - fold.getValue())), 0));
        }
        if(fold.getDirection().equals(Fold.Direction.Y)) {
            List<Point> toBeFolded = points.stream().filter(point -> point.getY() > fold.getValue()).collect(Collectors.toList());
            toBeFolded.forEach(point -> point.translate(0, (int) (-2*Math.abs(point.getY() - fold.getValue()))));
        }
        points = points.stream().distinct().collect(Collectors.toList());
    }

    public void part1() {
        fold(folds.get(0));
        System.out.println("Dots: " + points.size());
    }

    public void part2() {
        for(Fold f : folds) {
            fold(f);
        }
        printGrid();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day13 day13 = new Day13();
        day13.readFile();
        day13.part1();
        day13.part2();
    }
}
