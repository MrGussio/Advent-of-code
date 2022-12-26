package nl.jellehulter.aoc.y2022.day17;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    static class Shape {

        protected Set<Point> points;

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

    class State {

        final int pattern;
        final int rockType;
        final int pointsHashcode;

        public State(int pattern, int rockType, int pointsHashcode) {
            this.pattern = pattern;
            this.rockType = rockType;
            this.pointsHashcode = pointsHashcode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return pattern == state.pattern && pointsHashcode == state.pointsHashcode;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pattern, pointsHashcode);
        }
    }

    class Value {

        final long i;
        final long value;

        public Value(long i, long value) {
            this.i = i;
            this.value = value;
        }
    }

    public static int MAX_X = 6;

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
        for(long i = 0; i < 2022; i++) {
            int highestPoint = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(-1) + 4;
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

                translated = shape.translate(0, -1);
                if(collides(grid, translated)) {
                    grid.addAll(shape.points);
                    break;
                }
                shape = translated;
            }

        }
        int part1 = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(0) + 1;
        System.out.println(part1);
    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day17/day17.txt"));
        final String pattern = s.nextLine();
        Set<Point> grid = new HashSet<>();
        Map<Long, Long> remainders = new HashMap<>();
        Map<State, Value> seenPatterns = new HashMap<>();
        long steps = 0;
        for(long i = 0; i < 2022; i++) {
            int highestPoint = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(-1) + 4;
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

                translated = shape.translate(0, -1);
                if(collides(grid, translated)) {
                    grid.addAll(shape.points);
                    break;
                }
                shape = translated;
            }

            //Get the new highest point after adding the current shape to the grid
            long newHighestPoint = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(-1) + 4;

            //Retrieve all points at the 20 highest y-values and hash this set to save it's current state with respect to
            //where in the pattern we currently are and which block we have just added.
            Set<Point> top20 = normalize(grid.stream().filter(p -> p.y >= newHighestPoint - 20).collect(Collectors.toSet()));
            State currentState = new State((int) (steps % pattern.length()), (int) (i%5), top20.hashCode());
            remainders.put(i, newHighestPoint);
            //If we have seen this pattern before, apply this pattern repetitively
            if(seenPatterns.containsKey(currentState)) {
                Value v = seenPatterns.get(currentState);
                long valueDifference = newHighestPoint - v.value; //Difference in height since seeing the pattern before
                long blockDifference = i - v.i;//Amount of blocks added since seeing the previous pattern occurance
                long repetitionsNeeded = (1000000000000L-i-1)/blockDifference; //Amount of repetitions possible until a trillion
                long remainder = (1000000000000L-i-1)%blockDifference;//Remainig steps left

                //Get the increase of height of the remainder steps of the pattern
                long remainderIncrease = remainders.get((remainder + v.i)) - (int) v.value;

                //Result = height until now + (repititionsNeeded * valueDifference) + the remaining increase
                System.out.println(highestPoint + repetitionsNeeded * valueDifference + remainderIncrease);
                return;
            }
            //Put the pattern in the seen patterns and let's wait until we see a match.
            seenPatterns.put(currentState, new Value(i, newHighestPoint));

        }
        int part1 = grid.stream().max(Comparator.comparingInt(p -> p.y)).map(p -> p.y).orElse(0) + 1;
        System.out.println(part1);
    }

    /**
     * Normalizes the set of points by moving the lowest point in the set to y=0
     * @param points The set of points to be normalized
     * @return A new Set with clones of the points but then normalized
     */
    public Set<Point> normalize(Set<Point> points) {
        int lowest = points.stream().mapToInt(p -> p.y).min().orElse(0);
        if(lowest == 0)
            return points;
        Set<Point> result = new HashSet<>();
        for(Point p : points) {
            result.add(new Point(p.x, p.y - lowest));
        }
        return result;
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
        Day17 day17 = new Day17();
        day17.part1();
        day17.part2();
    }
}
