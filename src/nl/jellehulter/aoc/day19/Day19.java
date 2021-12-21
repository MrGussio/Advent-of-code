package nl.jellehulter.aoc.day19;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {


    class Match {

        public int scannerA;
        public int scannerB;
        public Beacon difference;
        public int rotations;

        public Match(int scannerA, int scannerB, Beacon difference, int rotations) {
            this.scannerA = scannerA;
            this.scannerB = scannerB;
            this.difference = difference;
            this.rotations = rotations;
        }

    }

    static class Beacon {

        public int x;
        public int y;
        public int z;

        public Beacon(String string) {
            int[] split = Arrays.stream(string.split(",")).mapToInt(Integer::parseInt).toArray();
            this.x = split[0];
            this.y = split[1];
            this.z = split[2];
        }

        public Beacon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Beacon rotateX() {
            return new Beacon(x, -z, y);
        }

        public Beacon rotateY() {
            return new Beacon(z, y, -x);
        }

        public Beacon rotateZ() {
            return new Beacon(-y, x, z);
        }

        public Beacon copy() {
            return new Beacon(x, y, z);
        }

        public Beacon rotateFrom(int rotation) {
            List<Beacon> rotations = new ArrayList<>();
            Beacon beacon = copy();
            switch (rotation) {
                case 0:
                    return beacon;
                case 1:
                    return beacon.rotateZ().rotateZ().rotateZ();
                case 2:
                    return beacon.rotateZ().rotateZ();
                case 3:
                    return beacon.rotateZ();
                case 4:
                    return beacon.rotateY().rotateY().rotateY();
                case 5:
                    return beacon.rotateX().rotateX().rotateX().
                            rotateY().rotateY().rotateY();
                case 6:
                    return beacon.rotateX().rotateX().
                            rotateY().rotateY().rotateY();
                case 7:
                    return beacon.rotateX()
                            .rotateY().rotateY().rotateY();
                case 8:
                    return beacon.rotateY().rotateY();
                case 9:
                    return beacon.rotateZ().rotateZ().rotateZ()
                        .rotateY().rotateY();
                case 10:
                    return beacon.rotateZ().rotateZ()
                            .rotateY().rotateY();
                case 11:
                    return beacon.rotateZ()
                            .rotateY().rotateY();
                case 12:
                    return beacon.rotateY();
                case 13:
                    return beacon.rotateX().rotateX().rotateX()
                            .rotateY();
                case 14:
                    return beacon.rotateX().rotateX()
                            .rotateY();
                case 15:
                    return beacon.rotateX()
                            .rotateY();
                case 16:
                    return beacon.rotateX().rotateX().rotateX();
                case 17:
                    return beacon.rotateY().rotateY().rotateY()
                            .rotateX().rotateX().rotateX();
                case 18:
                    return beacon.rotateY().rotateY()
                            .rotateX().rotateX().rotateX();
                case 19:
                    return beacon.rotateY()
                            .rotateX().rotateX().rotateX();
                case 20:
                    return beacon.rotateX();
                case 21:
                    return beacon.rotateX()
                            .rotateY().rotateY().rotateY();
                case 22:
                    return beacon.rotateX()
                            .rotateY().rotateY();
                case 23:
                    return beacon.rotateX()
                            .rotateY();
            }
            return null;
        }

        public List<Beacon> getRotations() {
            List<Beacon> rotations = new ArrayList<>();
            Beacon current = this.copy();
            //First rotate around y axis and get all orientations
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateZ();
            }
//            current = current.rotateZ();

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateX();
            }
//            current = current.rotateX();

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateZ();
            }
//            current = current.rotateZ();
//            current = current.rotateY();

            current = current.rotateY();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateX();
            }
            current.rotateY();
//            current = current.rotateX();

            current = current.rotateX();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateY();
            }
//            current = current.rotateY();

            current = current.rotateX().rotateX();
            for (int i = 0; i < 4; i++) {
                rotations.add(current);
                current = current.rotateY();
            }
            return rotations;
        }

        @Override
        public String toString() {
            return "Beacon{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return x == beacon.x && y == beacon.y && z == beacon.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    class Scanner {

        List<Beacon> beacons;
        List<Scanner> rotations;

        public Scanner(List<Beacon> beacons) {
            this.beacons = beacons;
        }

        public Scanner() {
            this(new ArrayList<>());
        }

        public List<Scanner> getRotations() {

            List<Scanner> rotations = new ArrayList<>();
            List<List<Beacon>> rotationsPerBeacon = new ArrayList<>();
            for (Beacon beacon : beacons) {
                rotationsPerBeacon.add(beacon.getRotations());
            }

            for (int i = 0; i < 24; i++) {
                int finalI = i;
                rotations.add(new Scanner(rotationsPerBeacon.stream().map(l -> l.get(finalI)).collect(Collectors.toList())));
            }

            return rotations;
        }

        public Scanner copy() {
            return new Scanner(beacons.stream().map(Beacon::copy).collect(Collectors.toList()));
        }

    }

    List<Scanner> scanners;

    public void readFile() throws FileNotFoundException {
        scanners = new ArrayList<>();
        java.util.Scanner s = new java.util.Scanner(new File("src/nl/jellehulter/aoc/day19/example.txt"));
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
            scanner.beacons.add(new Beacon(line));
        }
        scanners.add(scanner);

    }

    public void part1() {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < scanners.size(); i++) {
            for (int j = 0; j < scanners.size(); j++) {
                if(i == j)
                    continue;
                System.out.println("Checking (i,j) = " + i + ',' + j);
                Scanner s1 = scanners.get(i).copy();
                Scanner s2 = scanners.get(j).copy();
                List<Scanner> s2List = s2.getRotations();
                for (Scanner sr2 : s2List) {
                    HashMap<Beacon, Integer> map = new HashMap<>();
                    for (Beacon b1 : s1.beacons) {
                        for (Beacon b2 : sr2.beacons) {
                            Beacon diff = new Beacon(b1.x - b2.x, b1.y - b2.y, b1.z - b2.z);
                            if (map.containsKey(diff)) {
                                map.put(diff, map.get(diff) + 1);
                            } else {
                                map.put(diff, 1);
                            }
                        }
                    }
                    if (map.values().stream().anyMatch(x -> x >= 12)) {
                        Beacon diff = map.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).map(Map.Entry::getKey).orElseThrow();
                        int index = s2List.indexOf(sr2);
                        System.out.println(diff + " " + index);
                        matches.add(new Match(i, j, diff, s2List.indexOf(sr2)));
                    }
                }
            }
        }

        Set<Integer> solved = new HashSet<>();
        Set<Beacon> beacons = new HashSet<>();
        Map<Integer, List<Integer>> requiredRotations = new HashMap<>();
        requiredRotations.put(0, List.of(0));
        solved.add(0);
        while(requiredRotations.size() < scanners.size()) {
            for(Match m : matches) {
                if(requiredRotations.containsKey(m.scannerA) && !requiredRotations.containsKey(m.scannerB)) {
                    List<Integer> rotations = new ArrayList<>(requiredRotations.get(m.scannerA));
                    rotations.add(m.rotations);
                    requiredRotations.put(m.scannerB,rotations);
                } else if(requiredRotations.containsKey(m.scannerB) &&!requiredRotations.containsKey(m.scannerA)) {
                    List<Integer> rotations = new ArrayList<>(requiredRotations.get(m.scannerB));
                    rotations.add(23 - m.rotations);
                    requiredRotations.put(m.scannerA,rotations);
                }
            }
        }
        for(Integer s : requiredRotations.keySet()) {
            List<Integer> rotations = requiredRotations.get(s);
            for(Beacon b : scanners.get(s).beacons) {
                for(Integer i : rotations) {
                    b = b.rotateFrom(i);
                }
                beacons.add(b);
            }
        }
        System.out.println(beacons.size());
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day19 day19 = new Day19();
        day19.readFile();
        day19.part1();
        List<Scanner> rotations = day19.scanners.get(0).getRotations();
        rotations.toArray();
    }

}
