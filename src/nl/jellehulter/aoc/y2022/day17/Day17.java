package nl.jellehulter.aoc.y2022.day17;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    static class Shape {

        protected Set<Point> points;

        private Shape() {
            this(new HashSet<>());
        }

        private Shape(Set<Point> points) {
            this.points = points;
        }

        private Shape translate(int dx, int dy) {
            Set<Point> newPoints = new HashSet<>();
            for (Point p : points) {
                newPoints.add(new Point(p.x + dx, p.y + dy));
            }
            return new Shape(newPoints);
        }

        public void left() {
            translate(-1, 0);
        }

        public void right() {
            translate(-1, 0);
        }

        public void down() {
            translate(0, -1);
        }

        public Set<Point> getPoints() {
            return new TreeSet<>(points);
        }

        public static Shape minus(int y) {
            Set<Point> points = new HashSet<>();
            for (int x = 2; x <= 5; x++) {
                points.add(new Point(x, y));
            }
            return new Shape(points);
        }

        public static Shape plus(int y) {
            return new Shape(Set.of(
                    new Point(3, y),
                    new Point(2, y + 1),
                    new Point(3, y + 1),
                    new Point(4, y + 1),
                    new Point(3, y + 2)
                )
            );
        }

        public static Shape l(int y){
            Set<Point> points = new HashSet<>();
            for (int x = 2; x <= 4; x++)
                points.add(new Point(x, y));
            points.add(new Point(4, y + 1));
            points.add(new Point(4, y + 2));
            return new Shape(points);
        }

        public static Shape pipe(int y) {
            Set<Point> points = new HashSet<>();
            for (int i = y; i <= y + 3; i++) {
                points.add(new Point(2, i));
            }
            return new Shape(points);
        }

        public static Shape square(int y) {
            return new Shape(Set.of(
                new Point(2, y),
                new Point(3, y),
                new Point(2, y + 1),
                new Point(3, y + 1)
            ));
        }

        public Shape clone() {
            return new Shape(points);
        }
    }

    public static int MAX_X = 6;

    enum Move {
        LEFT,
        RIGHT,
        DOWN
    }

    public boolean collides(Set<Point> grid, Shape s) {
        if(s.points.stream().filter(p -> p.x < 0 || p.y < 0 || p.x > MAX_X).count() > 0)
            return true;
        Set<Point> copy = new HashSet<>(grid);
        copy.removeAll(s.points);
        return copy.size() != grid.size();
    }

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day17/day17.txt"));
        final String pattern = s.nextLine();
        Set<Point> grid = new HashSet<>();
        long steps = 0;
        long total = 0;
        for(long i = 999999999196L; i < 1000000000000L; i++) {
            int highestPoint = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(-1) + 4;
//            if(i % pattern.length() == 0) {
//                int part1 = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(0) + 1;
//                System.out.println(1000000000000L/pattern.length() * (total + part1));
//                System.out.println(i);
//                System.out.println(grid.size());
//            }
            Shape shape = switch((int) (i % 5L)) {
                case 0: yield Shape.minus(highestPoint);
                case 1: yield Shape.plus(highestPoint);
                case 2: yield Shape.l(highestPoint);
                case 3: yield Shape.pipe(highestPoint);
                case 4: yield Shape.square(highestPoint);
                default:
                    throw new IllegalStateException("Unexpected value: " + i % 5L);
            };

            while(true) {
                int dx = pattern.charAt((int) (steps % pattern.length())) == '<' ? -1 : 1;
                Shape translated = shape.translate(dx, 0);
                if(!collides(grid, translated)) {
                    shape = translated;
                }
                steps++;
//                printGrid(grid, shape);

                translated = shape.translate(0, -1);
                if(collides(grid, translated)) {
                    grid.addAll(shape.points);
                    int increasedHeight =  removeFloors(shape, grid);
                    total += increasedHeight;
                    if(increasedHeight > 0) {
                        if((i%5) == 4 && (steps % pattern.length() == 690))
                            System.out.println(total + "," + (steps % pattern.length()) + "," + (i%5) + "," + steps + "," + i);
                        if(steps % pattern.length() == 0) {
                            System.out.println("END");
                            return;
                        }
                    }
//                    printGrid(grid, null);
                    break;
                }
                shape = translated;
//                printGrid(grid, shape);
            }

            if(steps % pattern.length() == 0) {
                printGrid(grid, null);
                System.out.println();
            }

        }
        int part1 = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(0) + 1;
        System.out.println(total + part1);
    }

    public int removeFloors(Shape s, Set<Point> grid) {
        int minY = s.points.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = s.points.stream().mapToInt(p -> p.y).max().orElse(0);
        for(int y = maxY; y >= minY; y--) {
            int finalY = y;
            if(grid.stream().filter(p -> p.y == finalY).count() == 7) {
//                printGrid(grid, null);
                Set<Point> toBeRemoved = grid.stream().filter(p -> p.y <= finalY).collect(Collectors.toSet());
                grid.removeAll(toBeRemoved);
                final Set<Point> translatedPoints = new HashSet<>();
                grid.stream().filter(p -> p.y > finalY).forEach(p -> { p.translate(0, -finalY - 1); translatedPoints.add(p); });
                grid.clear();
                grid.addAll(translatedPoints);
//                printGrid(grid, null);
                return finalY + 1;
            }
        }
        return 0;
    }

    public void printGrid(Set<Point> points, Shape s) {
        int maxY = points.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(3);
        if(s != null) {
            maxY = Math.max(maxY, s.points.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(0));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int y = maxY; y >= 0; y--) {
            stringBuilder.append("|");
            for(int x = 0; x <= 6; x++) {
                Point p = new Point(x, y);
                if(s != null && s.points.contains(p)) {
                    stringBuilder.append("@");
                } else if(points.contains(p)) {
                    stringBuilder.append("#");
                } else {
                    stringBuilder.append(".");
                }
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append("+").append("-".repeat(7)).append("+\n\n");
        System.out.println(stringBuilder);
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Starting");
        Day17 day17 = new Day17();
        day17.part1();
    }
}
