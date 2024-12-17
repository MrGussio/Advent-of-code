package nl.jellehulter.aoc.y2024.day8;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import nl.jellehulter.aoc.y2024.Day;

public class DayEight extends Day {

    record Vector(int x, int y) {

        Vector add(Vector v) {
            return new Vector(this.x + v.x, this.y + v.y);
        }

        Vector mul(int a) {
            return new Vector(this.x * a, this.y * a);
        }

        Vector diff(Vector v) {
            return new Vector(this.x - v.x, this.y - v.y);
        }

    }

    public void part1() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day8/dayeight.txt");
        List<List<Character>> chars =
                lines.stream().map(s -> s.chars().mapToObj(c -> (char) c).toList()).toList();

        Map<Character, List<Vector>> vectorToChar = createCharacterToVectorsMap(chars);

        long hashes = vectorToChar.values().stream()
                .flatMap(vectors -> determineLocations(vectors).stream())
                .distinct()
                .filter(v -> isInBox(v, lines.size()))
                .count();

        System.out.println(hashes);
    }

    private static Map<Character, List<Vector>> createCharacterToVectorsMap(List<List<Character>> chars) {
        return IntStream.range(0, chars.size()).boxed()
                .flatMap(y -> IntStream.range(0, chars.get(y).size()).mapToObj(x -> {
                    Character c = chars.get(y).get(x);
                    if (c == '.') {
                        return null;
                    }
                    Vector v = new Vector(x, y);
                    return Map.entry(c, v);
                })).filter(Objects::nonNull).collect(Collectors.groupingBy(Map.Entry::getKey,
                                                                           Collectors.mapping(
                                                                                   Map.Entry::getValue,
                                                                                   Collectors.toList())));
    }

    public void part2() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day8/dayeight.txt");
        List<List<Character>> chars =
                lines.stream().map(s -> s.chars().mapToObj(c -> (char) c).toList()).toList();

        Map<Character, List<Vector>> vectorToChar = createCharacterToVectorsMap(chars);


        long hashes = vectorToChar.values().stream()
                .flatMap(vectors -> determineLocationsInBox(vectors, lines.size()).stream())
                .distinct()
                .count();

        System.out.println(hashes);
    }

    private static boolean isInBox(Vector v, int size) {
        return v.x >= 0 && v.x < size && v.y >= 0 && v.y < size;
    }

    private List<Vector> determineLocations(List<Vector> vectors) {
        return IntStream.range(0, vectors.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, vectors.size()).boxed().flatMap(j -> {
                             Vector v1 = vectors.get(i);
                             Vector v2 = vectors.get(j);
                             Vector diff = v1.diff(v2);
                             Vector loc1 = v1.add(diff);
                             Vector loc2 = v2.add(diff.mul(-1));
                             return Stream.of(loc1, loc2);
                         })).toList();
    }

    private List<Vector> determineLocationsInBox(List<Vector> vectors, int boxSize) {
        return IntStream.range(0, vectors.size()).boxed()
                .flatMap(i -> IntStream.range(i + 1, vectors.size()).boxed().flatMap(j -> {
                    Vector v1 = vectors.get(i);
                    Vector v2 = vectors.get(j);
                    Vector diff = v1.diff(v2);
                    Stream.Builder<Vector> stream = Stream.builder();

                    Vector loc1 = v1;
                    while(isInBox(loc1, boxSize)) {
                        stream.add(loc1);
                        loc1 = loc1.add(diff);
                    }

                    Vector loc2 = v2;
                    while(isInBox(loc2, boxSize)) {
                        stream.add(loc2);
                        loc2 = loc2.add(diff.mul(-1));
                    }

                    return stream.build();
                })).toList();
    }

    public static void main(String[] args) {
        DayEight dayEight = new DayEight();
        dayEight.part1();
        dayEight.part2();
    }

}
