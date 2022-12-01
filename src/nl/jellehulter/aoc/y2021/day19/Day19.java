package nl.jellehulter.aoc.y2021.day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 {

    static class Point {

        int x;
        int y;
        int z;

        public Point(int[] arr) {
            this.x = arr[0];
            this.y = arr[1];
            this.z = arr[2];
        }

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point add(Point p) {
            this.x += p.x;
            this.y += p.y;
            this.z += p.z;
            return this;
        }

        public Point sub(Point p) {
            this.x -= p.x;
            this.y -= p.y;
            this.z -= p.z;
            return this;
        }

        /**
         * Rotate over the X-axis
         * @return Copy of this point but then rotated over the X-axis
         */
        public Point rotateX() {
            return new Point(x, -z, y);
        }

        /**
         * Rotate over the Y-axis
         * @return Copy of this point but then rotated over the Y-axis
         */
        public Point rotateY() {
            return new Point(z, y, -x);
        }

        /**
         * Rotate over the Z-axis
         * @return Copy of this point but then rotated over the Z-axis
         */
        public Point rotateZ() {
            return new Point(-y, x, z);
        }

        /**
         * Get all possible rotations and orientations of this Point
         * @return List of all possible rotations and orientations
         */
        public List<Point> getAllRotations() {
            List<Point> rotations = new ArrayList<>();
            Point current = this.copy();
            //First rotate around y axis and get all orientations
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateZ();
            }

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateX();
            }

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateZ();
            }

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateX();
            }
            current = current.rotateY();

            //Now do "top and bottom" of cube
            current = current.rotateX();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateY();
            }

            current = current.rotateX().rotateX();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateY();
            }
            return rotations;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        public Point copy() {
            return new Point(x, y, z);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }

    public class Scanner {

        List<Point> points;

        public Scanner(List<Point> points) {
            this.points = points;
        }

        public Scanner() {
            this(new ArrayList<>());
        }

        public List<Scanner> getAllRotations() {
            Set<List<Point>> rotationsByPoint = points.stream().map(Point::getAllRotations).collect(Collectors.toSet());
            return IntStream.range(0, 24).mapToObj(i -> new Scanner(rotationsByPoint.stream().map(l -> l.get(i)).collect(Collectors.toList()))).collect(Collectors.toList());
        }

    }

    private Set<Scanner> scanners;

    public void readFile() throws FileNotFoundException {
        java.util.Scanner s = new java.util.Scanner(new File("src/nl/jellehulter/aoc/day19/input.txt"));
        scanners = new HashSet<>();
        Scanner scanner = new Scanner();
        s.nextLine();
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (line.equals("")) {
                scanners.add(scanner);
                scanner = new Scanner();
                s.nextLine();
                continue;
            }
            scanner.points.add(new Point(Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray()));
        }
        scanners.add(scanner);

    }

    public void part1() {
        Set<Point> beacons;
        Stack<Scanner> stack = new Stack<>(); //Stack of scanners left to be scanned
        Map<Scanner, Point> scannerLocations = new HashMap<>(); //Locations of all scanners
        stack.addAll(scanners);
        Scanner perspective = stack.pop(); //The scanner which is going to be used as main perspective
        beacons = new HashSet<>(perspective.points); //All unique beacons
        scannerLocations.put(perspective, new Point(0, 0, 0));

        stackLoop:
        while (!stack.isEmpty()) {
            for (Scanner scanner : stack) {
                List<Scanner> rotations = scanner.getAllRotations();
                for (Scanner rotation : rotations) {
                    Map<Point, Integer> diffs = new HashMap<>();
                    for (Point p1 : rotation.points) {
                        for (Point p2 : beacons) {
                            Point diff = p2.copy().sub(p1.copy());
                            diffs.put(diff, diffs.getOrDefault(diff, 0) + 1);
                        }
                    }
                    //If there are more than or equal to 12 distances between points between the perspective of the first scanner
                    //the same, we know that this rotation/orientation is correct, hence translate the points and then add them
                    //to the list of beacons.
                    //Over time more perspectives get added, slowly expanding the entire grid.
                    if (diffs.values().stream().anyMatch(i -> i >= 12)) {
                        Point diff = diffs.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).orElseThrow();
                        beacons.addAll(rotation.points.stream().map(p -> p.copy().add(diff)).collect(Collectors.toList()));
                        stack.remove(scanner);
                        scannerLocations.put(scanner, diff);
                        System.out.println(diff);
                        continue stackLoop;//Redo outer loop such that we do not get a ConcurrentModificationException
                    }
                }
            }
        }
        System.out.println(beacons.size());

        //Calculate maximum Manhattan distance
        int max = 0;
        for(Point p1 : scannerLocations.values()) {
            for(Point p2 : scannerLocations.values()) {
                int distance = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z);
                max = Math.max(distance, max);
            }
        }
        System.out.println(max);
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day19 day19 = new Day19();
        day19.readFile();
        day19.part1();
        System.out.println();
    }

}
