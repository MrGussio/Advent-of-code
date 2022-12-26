package nl.jellehulter.aoc.y2022.day18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 {

    class Point {

        final int x;
        final int y;
        final int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        public Set<Point> getNeighbours() {
            Set<Point> result = new HashSet<>();
            result.add(new Point(x - 1, y, z));
            result.add(new Point(x + 1, y, z));
            result.add(new Point(x, y - 1, z));
            result.add(new Point(x, y + 1, z));
            result.add(new Point(x, y, z - 1));
            result.add(new Point(x, y, z + 1));
            return result;
        }

        public boolean connected(Point other) {
            return getNeighbours().contains(other);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    public void part1() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day18/day18.txt"));
        Set<Point> points = new HashSet<>();
        while(s.hasNextLine()) {
            String input = s.nextLine();
            int[] split = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
            points.add(new Point(split[0], split[1], split[2]));
        }

        int total = 0;
        for(Point p : points) {
            Set<Point> freeSides = p.getNeighbours();
            freeSides.removeAll(points);
            total += freeSides.size();
        }

        System.out.println(total);
    }

    public void part2() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day18/day18.txt"));
        Set<Point> points = new HashSet<>();
        while(s.hasNextLine()) {
            String input = s.nextLine();
            int[] split = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
            points.add(new Point(split[0], split[1], split[2]));
        }

        //Determine a set of reachable points for the water.
        //This basically creates a mold of the droplet
        Set<Point> reachablePoints = new HashSet<>();
        reachablePoints.add(new Point(-1, -1, -1));
        int maxX = points.stream().max(Comparator.comparingInt(p -> p.x)).get().x+5;
        int maxY = points.stream().max(Comparator.comparingInt(p -> p.y)).get().y+5;
        int maxZ = points.stream().max(Comparator.comparingInt(p -> p.z)).get().z+5;
        while(true) {
            Set<Point> newPoints = new HashSet<>();
            for(Point p : reachablePoints) {
                Set<Point> neighboursInRange = p.getNeighbours().stream()
                        .filter(n -> !points.contains(n) &&
                                !reachablePoints.contains(n) &&
                                n.x >= -1 && n.x <= maxX &&
                                n.y >= -1 && n.y <= maxY &&
                                n.z >= -1 && n.z <= maxZ)
                        .collect(Collectors.toSet());
                newPoints.addAll(neighboursInRange);
            }
            if(newPoints.isEmpty())
                break;
            reachablePoints.addAll(newPoints);
        }

        //Reverse the mold to get the actual droplet
        Set<Point> filledDroplet = new HashSet<>();
        for(int x = -1; x <= maxX; x++) {
            for(int y = -1; y <= maxY; y++) {
                for(int z = -1; z <= maxZ; z++) {
                    Point p = new Point(x, y, z);
                    if(!reachablePoints.contains(p)) {
                        filledDroplet.add(p);
                    }
                }
            }
        }

        //Now count the free sides of this reverse mold
        int total = 0;
        for(Point p : filledDroplet) {
            Set<Point> freeSides = p.getNeighbours();
            freeSides.removeAll(filledDroplet);
            total += freeSides.size();
        }

        System.out.println(total);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day18 day18 = new Day18();
        day18.part1();
        day18.part2();
    }

}
