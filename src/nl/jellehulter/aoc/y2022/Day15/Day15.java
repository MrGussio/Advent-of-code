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

    public void solve() {
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
            for(int y = Math.max(MIN_Y, sensor.y - distance); y <= Math.min(MAX_Y, sensor.y + distance); y++) {
                //The width of the interval at this height, seen from the center.
                //E.g., a width of 0 is an interval of (x,x), a width of 1 is (x-1,x+1)
                //Take the absolute so that it reverts at the level of the sensor (e.g., the maximum width)
                int width = distance - Math.abs(y-sensor.y);
                Interval interval = new Interval(sensor.x - width, sensor.x + width);
                IntervalRange intervalRange = intervalsPerY.getOrDefault(y, new IntervalRange());
                intervalRange.add(interval);
                intervalsPerY.put(y, intervalRange);
            }
        }
        //Determine the length of the intervals at the DESIRED_Y, and subtract any beacons which might be on that row.
        int part1 = intervalsPerY.get(DESIRED_Y).length() - (int) beacons.stream().filter(p -> p.y == DESIRED_Y).count();
        System.out.println(part1);

        //Check where there is an interval range which is not entirely closed (e.g., more than 1 interval in its set)
        Map.Entry<Integer, IntervalRange> intervalPerYEntry = intervalsPerY.entrySet().stream().filter(entry -> entry.getValue().intervals.size() == 2)
                .findFirst()
                .orElseThrow();
        List<Interval> distressIntervals = intervalPerYEntry.getValue().intervals.stream().toList();

        long part2 = 4000000L * (distressIntervals.get(0).b + 1) + intervalPerYEntry.getKey();
        System.out.println(part2);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day15 day15 = new Day15();
        day15.readFile();
        day15.solve();
    }
}
