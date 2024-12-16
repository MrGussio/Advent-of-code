package nl.jellehulter.aoc.y2024.day6;

import nl.jellehulter.aoc.y2024.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DaySix extends Day {

    enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        Direction rotateClockwise() {
            Direction[] directions = Direction.values();
            int newOrdinal = (this.ordinal() + 1) % directions.length;
            return directions[newOrdinal];
        }
    }

    record Coord(int x, int y, Direction d) {
    }

    private static final char UP = '^';
    private static final char DOT = '.';
    private static final char OBSTACLE = '#';

    public void part1() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day6/test.txt");
        List<List<Character>> chars = toListOfChars(lines);
        Coord coord = getStartingCoord(chars);
        int width = chars.get(0).size();
        int height = chars.size();

        List<Coord> history = new ArrayList<>();
        history.add(coord);
        while (true) {
            Coord newCoord = getNewCoord(coord);

            if (outsideMap(newCoord, width, height)) {
                break;
            }

            char c = chars.get(newCoord.y).get(newCoord.x);
            coord = move(c, coord, newCoord, history);

        }

        long distinctCoordinates = history.stream()
                .map(c -> new Coord(c.x, c.y, Direction.UP))
                .distinct()
                .count();
        System.out.println(distinctCoordinates);
    }

    private static List<List<Character>> toListOfChars(List<String> lines) {
        return lines.stream().map(s -> s.chars().mapToObj(c -> (char) c)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private Coord move(char c, Coord coord, Coord newCoord, List<Coord> history) {
        switch (c) {
            case DOT:
            case UP:
                coord = newCoord;
                history.add(newCoord);
                break;
            case OBSTACLE:
                coord = new Coord(coord.x, coord.y, coord.d.rotateClockwise());
                break;
            default:
                throw new RuntimeException();
        }
        return coord;
    }

    public void part2() {
        List<String> lines = readLines("src/nl/jellehulter/aoc/y2024/day6/daysix.txt");
        List<List<Character>> chars = toListOfChars(lines);
        int width = chars.get(0).size();
        int height = chars.size();

        long count = IntStream.range(0, height).boxed()
                .flatMap(y ->
                        IntStream.range(0, width).mapToObj(x -> {
                            if (chars.get(y).get(x) != DOT) {
                                return false;
                            }
                            Coord coord = getStartingCoord(chars);
                            List<List<Character>> mutated = mutateLines(y, x, chars);
                            List<Coord> history = new ArrayList<>();
                            history.add(coord);

                            while (true) {
                                Coord newCoord = getNewCoord(coord);

                                if (outsideMap(newCoord, width, height)) {
                                    return false;
                                }

                                if (history.contains(newCoord)) {
                                    return true;
                                }

                                char c = mutated.get(newCoord.y).get(newCoord.x);
                                coord = move(c, coord, newCoord, history);
                            }
                        }))
                .filter(b -> b)
                .count();
        System.out.println(count);
    }

    private List<List<Character>> mutateLines(int y, int x, List<List<Character>> chars) {
        List<List<Character>> copy = chars.stream().map(
                a -> (List<Character>) new ArrayList<>(a))
                .toList();
        copy.get(y).set(x, OBSTACLE);
        return copy;
    }

    private Coord getStartingCoord(List<List<Character>> lines) {
        int y = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).contains(UP))
                .findFirst()
                .orElseThrow();
        int x = lines.get(y).indexOf(UP);
        Coord coord = new Coord(x, y, Direction.UP);
        return coord;
    }

    private Coord getNewCoord(Coord coord) {
        return switch (coord.d) {
            case UP:
                yield new Coord(coord.x, coord.y - 1, coord.d);
            case LEFT:
                yield new Coord(coord.x - 1, coord.y, coord.d);
            case DOWN:
                yield new Coord(coord.x, coord.y + 1, coord.d);
            case RIGHT:
                yield new Coord(coord.x + 1, coord.y, coord.d);
        };
    }

    private boolean outsideMap(Coord coord, int width, int height) {
        if (coord.x < 0 || coord.x >= width)
            return true;
        return coord.y < 0 || coord.y >= height;
    }

    private List<String> deepCopyLines(List<String> lines) {
        return lines.stream().map(String::new)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        DaySix daySix = new DaySix();
        daySix.part1();
        daySix.part2();
    }

}
