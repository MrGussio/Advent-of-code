package nl.jellehulter.aoc.y2021.day22;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day22 {

    static class Point {

        public int x;
        public int y;
        public int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
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
    }

    static class Cube {

        public Point p1;
        public Point p2;

        public Cube(int x1, int y1, int z1, int x2, int y2, int z2) {
            this.p1 = new Point(x1, y1, z1);
            this.p2 = new Point(x2, y2, z2);
            assert x1 <= x2;
            assert y1 <= y2;
            assert z1 <= z2;
        }

        public Cube(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public boolean contains(Point p) {
            return p.x >= p1.x && p.y >= p1.y && p.z >= p1.z &&
                    p.x <= p2.x && p.y <= p2.y && p.z <= p2.z;
        }

        public boolean intersects(Cube other) {
            return other.contains(p1) || other.contains(p2) || contains(other.p1) || contains(other.p2);
        }

        public long getVolume() {
            long volume = (long) (Math.abs(p2.x - p1.x) + 1) * (Math.abs(p2.y - p1.y) + 1) * (Math.abs(p2.z - p1.z) + 1);
            return volume;
        }

        @Override
        public String toString() {
            return "Cube{" +
                    "p1=" + p1 +
                    ", p2=" + p2 +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return Objects.equals(p1, cube.p1) && Objects.equals(p2, cube.p2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p1, p2);
        }

        public Cube intersection(Cube other) {
            Point p1 = new Point(Math.max(this.p1.x, other.p1.x), Math.max(this.p1.y, other.p1.y), Math.max(this.p1.z, other.p1.z));
            Point p2 = new Point(Math.min(this.p2.x, other.p2.x), Math.min(this.p2.y, other.p2.y), Math.min(this.p2.z, other.p2.z));
            if (p1.x > p2.x || p1.y > p2.y || p1.z > p2.z)
                return null;
            return new Cube(p1, p2);
        }
    }

    static class Instruction {

        boolean on;
        public Cube cube;

        public Instruction(boolean on, Cube cube) {
            this.on = on;
            this.cube = cube;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "on=" + on +
                    ", cube=" + cube +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Instruction that = (Instruction) o;
            return on == that.on && Objects.equals(cube, that.cube);
        }

        @Override
        public int hashCode() {
            return Objects.hash(on, cube);
        }
    }

    List<Instruction> instructionList;

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/day22/input.txt"));
        instructionList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split1 = line.split(" ");

            String[] split2 = split1[1].split(",");
            String[] split3 = split2[0].replace("x=", "").split("\\.\\.");
            int x1 = Integer.parseInt(split3[0]);
            int x2 = Integer.parseInt(split3[1]);
            split3 = split2[1].replace("y=", "").split("\\.\\.");
            int y1 = Integer.parseInt(split3[0]);
            int y2 = Integer.parseInt(split3[1]);
            split3 = split2[2].replace("z=", "").split("\\.\\.");
            int z1 = Integer.parseInt(split3[0]);
            int z2 = Integer.parseInt(split3[1]);
            Cube cube = new Cube(x1, y1, z1, x2, y2, z2);
            instructionList.add(new Instruction(split1[0].equals("on"), cube));
        }


    }


    public void part1() {
        Cube initialArea = new Cube(-50, -50, -50, 50, 50, 50);
        Set<Point> turnedOn = new HashSet<>();
        for (Instruction instruction : instructionList) {
            Cube cube = instruction.cube;
            if (!cube.intersects(initialArea)) {
                System.out.println("Skipped " + instruction);
                continue;
            }
            for (int x = cube.p1.x; x <= cube.p2.x; x++) {
                for (int y = cube.p1.y; y <= cube.p2.y; y++) {
                    for (int z = cube.p1.z; z <= cube.p2.z; z++) {
                        Point p = new Point(x, y, z);
                        if (initialArea.contains(p)) {
                            if (instruction.on) {
                                turnedOn.add(p);
                            } else {
                                turnedOn.remove(p);
                            }
                        }
                    }
                }
            }

        }
        System.out.println(turnedOn.size());

    }

    public void part2() {
        HashMap<Cube, Integer> cubes = new HashMap<>();
        for (Instruction i : instructionList) {
            HashMap<Cube, Integer> newMap = new HashMap<>(cubes);
            //Loop over all existing cubes to add the complement of their current value,
            //such that eventually after adding an "on" cube only the new cubes are added,
            //or when using "off" all these negations went into effect.
            for (Map.Entry<Cube, Integer> entry : cubes.entrySet()) {
                Cube intersection = i.cube.intersection(entry.getKey());
                if (intersection == null)
                    continue;
                //If C is positive, remove the intersection such that the total stays the same
                //If C is negative, add the intersection such that the total stays the same
                newMap.put(intersection, newMap.getOrDefault(intersection, 0) - entry.getValue());

            }
            //Add the current cube if it is "on"
            if (i.on)
                newMap.put(i.cube, newMap.getOrDefault(i.cube, 0) + 1);
            cubes = newMap;
            System.out.println(cubes.size() + " - " + (instructionList.indexOf(i)+1) + "/" + instructionList.size());
        }
        long volume = 0;
        for (Map.Entry<Cube, Integer> entry : cubes.entrySet()) {
            volume += entry.getKey().getVolume() * entry.getValue();
        }
        System.out.println(volume);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day22 day22 = new Day22();
        day22.readFile();
//        day22.part1();
        day22.part2();
    }


}
