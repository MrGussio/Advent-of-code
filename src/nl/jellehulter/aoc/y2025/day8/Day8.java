package nl.jellehulter.aoc.y2025.day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day8 {

    private final String testInput = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689""";

    public void part1() throws IOException {
        String input = Files.readString(Path.of("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day8\\day8.txt"));
        List<JunctionBox> boxes = new ArrayList<>();
        for (String line : input.lines().toList()) {
            String[] parts = line.split(",");
            boxes.add(new JunctionBox(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }
        

        Map<String, Double> distances = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {
                JunctionBox boxA = boxes.get(i);
                JunctionBox boxB = boxes.get(j);
                String name = getSortedName(boxA, boxB);
                double distance = boxA.getDistance(boxB);
                distances.put(name, distance);
            }
        }

        List<Map.Entry<String, Double>> sorted = distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();

        Map<String, String> circuits = new HashMap<>();
        for (JunctionBox b : boxes) {
            circuits.put(b.toString(), b.toString());
        }


//        int pairs = 10;
//        int pairs = 1000;
        int pairs = sorted.size();

        sorted.forEach(stringDoubleEntry -> System.out.println(stringDoubleEntry.getKey() + ": " + stringDoubleEntry.getValue()));
        for (int i = 0; i < pairs; i++) {
            var entry = sorted.get(i);
            String[] parts = entry.getKey().split("-");
            String boxA = circuits.get(parts[0]);
            String boxB = circuits.get(parts[1]);
            if (!boxA.equals(boxB)) {
                System.out.println("Connecting " + parts[0] + " and " + parts[1] + " with distance " + entry.getValue());
                for (var key : circuits.keySet()) { //Merge the circuits
                    if (circuits.get(key).equals(boxB)) {
                        circuits.put(key, boxA);
                    }
                }
            }
            //are all boxes in the same circuit?
            Set<String> uniqueCircuits = new HashSet<>(circuits.values());
            if (uniqueCircuits.size() == 1) {
                int a = Integer.parseInt(parts[0].split(",")[0]);
                int b = Integer.parseInt(parts[1].split(",")[0]);
                System.out.println("Part 2: " + a*b);
                break;
            }
        }

        //Determine the size of the circuits by counting the values of the map
        Map<String, Integer> circuitSizes = circuits.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.groupingBy(Function.identity())) // this makes {3.2=[3.2], 9.5=[9.5], 4.9=[4.9, 4.9]}
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().size())
                );

        List<Integer> sortedResult = circuitSizes.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getValue)
                .toList();

        System.out.println(sortedResult);
        
        var result = sortedResult.stream()
                .limit(3)
                .reduce((a, b) -> a*b)
                .orElseThrow();

        System.out.println(result);
    }

    record JunctionBox(int x, int y, int z) {

        public double getDistance(JunctionBox other) {
            double i = Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
            return Math.sqrt(i);
        }

        public int compare(JunctionBox other) {
            return toString().compareTo(other.toString());
        }

        public String toString() {
            return x + "," + y + "," + z;
        }

    }

    public String getSortedName(JunctionBox a, JunctionBox b) {
        if (a.compare(b) < 0) {
            return a + "-" + b;
        } else {
            return b + "-" + a;
        }
    }

    public static void main(String[] args) throws IOException {
        Day8 day8 = new Day8();
        day8.part1();
    }

}
