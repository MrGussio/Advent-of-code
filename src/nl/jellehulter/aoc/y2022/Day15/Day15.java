package nl.jellehulter.aoc.y2022.Day15;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class Day15 {

    class Interval implements Comparable<Interval>{
        int a;
        int b;

        public Interval(int a, int b) {
            if(a > b) {
                throw new RuntimeException();
            }
            this.a = a;
            this.b = b;
        }

        public boolean contains(int x) {
            return x >= a && x <= b;
        }

        public boolean neighbouring(Interval other) {
            if(this.compareTo(other) < 0) {//this is smaller
                return Math.abs(this.b - other.a) <= 1;
            } else {//other is smaller
                return Math.abs(this.a - other.b) <= 1;
            }
        }

        public boolean overlap(Interval interval) {
            return this.contains(interval.a) || this.contains(interval.b) || interval.contains(a) || interval.contains(b);
        }

        public Interval combine(Interval interval) {
            if(!overlap(interval) && ! neighbouring(interval)) throw new RuntimeException();
            return new Interval(Math.min(interval.a, a), Math.max(interval.b, b));
        }

        public int length() {
            return Math.abs(a - b) + 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Interval interval = (Interval) o;
            return a == interval.a && b == interval.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }

        @Override
        public int compareTo(Interval o) {
            return (o.a != a) ? Integer.compare(a, o.a) : Integer.compare(b, o.b);
        }

        @Override
        public String toString() {
            return "(" + a + "," + b + ")";
        }
    }

    class IntervalRange {
        Set<Interval> intervals = new TreeSet<>();

        public void add(Interval interval) {
            Optional<Interval> overlap = intervals.stream().filter(x -> interval.overlap(x) || interval.neighbouring(x)).findFirst();
            if(overlap.isEmpty()) {
                intervals.add(interval);
            } else {
                intervals.remove(overlap.get());
                this.add(interval.combine(overlap.get()));
            }
        }

        public int length() {
            return intervals.stream().mapToInt(Interval::length).sum();
        }
    }

    Map<Point, Point> sensorMap = new HashMap<>();
    Set<Point> noBeacons = new HashSet<>();
    Set<Point> beacons = new HashSet<>();
    final int DESIRED_Y = 2000000;
//    final int DESIRED_Y = 10;

    final int MIN_Y = 0;
//    final int MAX_Y = 20;
    final int MAX_Y = 4000000;

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/Day15/day15.txt"));
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.substring("Sensor at ".length());
            String[] split = line.split(": closest beacon is at ");
            String[] sensorString = split[0].split(", ");
            int sensorX = Integer.parseInt(sensorString[0].substring("x=".length()));
            int sensorY = Integer.parseInt(sensorString[1].substring("y=".length()));
            String[] beaconString = split[1].split(", ");
            int beaconX = Integer.parseInt(beaconString[0].substring("x=".length()));
            int beaconY = Integer.parseInt(beaconString[1].substring("y=".length()));

            Point sensor = new Point(sensorX, sensorY);
            Point beacon = new Point(beaconX, beaconY);
            sensorMap.put(sensor, beacon);
            beacons.add(beacon);
        }
    }

    public void part1() throws FileNotFoundException {

        for(Map.Entry<Point, Point> entry : sensorMap.entrySet()) {
            Point sensor = entry.getKey();
            Point beacon = entry.getValue();
//            int distance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
//            for(int y = sensor.y - distance; y <= sensor.y + distance; y++) {
//                for(int x = 0; x <= distance; x++) {
//                    Point p = new Point(sensor.x+x, y);
//                    if(!beacons.containsKey(p)) {
//                        noBeacons.add(p);
//                    }
//                    Point p2 = new Point(sensor.x - x, y);
//                    if(!beacons.containsKey(p)) {
//                        noBeacons.add(p);
//                    }
//                }
//            }
//            for(int x = sensor.x - distance; x <= sensor.x + distance; x++) {
//                for(int y = sensor.y - distance; y <= sensor.y + distance; y++) {
//                    Point p = new Point(x, y);
//                    if(!beacons.containsKey(p)) {
//                        noBeacons.add(p);
//                    }
//                }
//            }
            int distance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
            Set<Point> vonNeumann = getManhattenPointsAtY(sensor, distance, DESIRED_Y);
            for(Point p : vonNeumann) {
                if(!beacons.contains(p)) {
                    noBeacons.add(p);
                }
            }
        }
//        printVertical(noBeacons, 10);
        System.out.println(noBeacons.stream().filter(point -> point.y == DESIRED_Y).count());
    }

    public void printVertical(Set<Point> noBeacons, int y) {
        double minX = noBeacons.stream().filter(point -> point.y == y).mapToDouble(Point::getX).min().getAsDouble();
        double maxX = noBeacons.stream().filter(point -> point.y == y).mapToDouble(Point::getX).max().getAsDouble();
        for(int x = (int) minX; x <= maxX; x++) {
            System.out.print(noBeacons.contains(new Point(x, y)) ? "#" : ".");
        }
        System.out.println();
    }

    public Set<Point> getManhattenPointsAtY(Point p, int distance, int y) {
        Set<Point> points = new HashSet<>();
        if(y >= p.y-distance && y <= p.y+distance) {
            for(int x = p.x - distance; x <= p.x + distance; x++) {
                int localDist = Math.abs(p.x - x) + Math.abs(p.y - y);
                if(localDist <= distance) {
                    Point q = new Point(x, y);
                    points.add(q);
                }
            }
        }
        return points;
    }

    public Set<Point> getManhattenPoints(Point p, int distance) {
        Set<Point> points = new HashSet<>();
        for(int y = p.y - distance; y <= p.y + distance; y++) {
            for(int x = p.x - distance; x <= p.x + distance; x++) {
                int localDist = Math.abs(p.x - x) + Math.abs(p.y - y);
                if(localDist <= distance) {
                    Point q = new Point(x, y);
                    points.add(q);
                }
            }
        }
        return points;
    }

    public void part2() {
        Map<Integer, IntervalRange> intervalsPerY = new HashMap<>();

        sensorMap.values().stream().distinct().forEach(p -> {
           IntervalRange intervalRange = intervalsPerY.getOrDefault(p.y, new IntervalRange());
           intervalRange.add(new Interval(p.x, p.x));
           intervalsPerY.put(p.y, intervalRange);
        });
        for(Map.Entry<Point, Point> entry : sensorMap.entrySet()) {
            Point sensor = entry.getKey();
            Point beacon = entry.getValue();
            int distance = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
//            for(int y = 0; y <= distance; y++) {
//                IntervalRange intervals = intervalsPerY.getOrDefault(sensor.y + y, new IntervalRange());
//                Interval interval = new Interval(sensor.x - (distance - y), sensor.x + (distance - y));
//                intervals.add(interval);
//                intervalsPerY.put(sensor.y + y, intervals);
//
//                intervals = intervalsPerY.getOrDefault(sensor.y - y, new IntervalRange());
//                intervals.add(new Interval(sensor.x - (distance - y), sensor.x + (distance - y)));
//                intervalsPerY.put(sensor.y - y, intervals);
//            }
            for(int y = Math.max(MIN_Y, sensor.y - distance); y <= Math.min(MAX_Y, sensor.y + distance); y++) {
                int width = distance - Math.abs(y-sensor.y);
                Interval interval = new Interval(sensor.x - width, sensor.x + width);
                IntervalRange intervalRange = intervalsPerY.getOrDefault(y, new IntervalRange());
                intervalRange.add(interval);
                intervalsPerY.put(y, intervalRange);
            }
        }

//        List<Interval> desired = intervalsPerY.get(DESIRED_Y);
//        desired.sort((i1, i2) -> (i1.a != i2.a) ? Integer.compare(i1.a, i2.a) : Integer.compare(i1.b, i2.b));
//        //reduce intervals where applicable
//        desired.stream().di

        int part1 = intervalsPerY.get(DESIRED_Y).length() - (int) beacons.stream().filter(p -> p.y == DESIRED_Y).count();
        System.out.println(part1);


        Map.Entry<Integer, IntervalRange> intervalPerYEntry = intervalsPerY.entrySet().stream().filter(entry -> entry.getValue().intervals.size() == 2)
                .findFirst()
                .orElseThrow();
        List<Interval> distressIntervals = intervalPerYEntry.getValue().intervals.stream().toList();

        long part2 = 4000000L * (distressIntervals.get(0).b + 1) + intervalPerYEntry.getKey();
        System.out.println(part2);

        for(Map.Entry<Integer, IntervalRange> entry : intervalsPerY.entrySet()) {
            if(entry.getValue().intervals.size() >= 2) {
                System.out.print(entry.getKey() + " ");
                System.out.println(entry.getValue().intervals);
//                int part2 =
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day15 day15 = new Day15();
//        day15.part1();
        day15.readFile();
        day15.part2();
    }
}
