package nl.jellehulter.aoc.day20;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day20 {

    String mapping;
    Set<Point> points;
    int minX;
    int maxX;
    int minY;
    int maxY;
    boolean isInifinitelyLit = false;

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day20/input.txt"));
        mapping = s.nextLine();
        s.nextLine();
        points = new HashSet<>();
        int y = 0;
        while(s.hasNextLine()) {
            String line = s.nextLine();
            for(int x = 0; x < line.length(); x++) {
                if(line.charAt(x) == '#') {
                    points.add(new Point(x, y));
                }
            }
            y++;
        }
        minX = points.stream().min(Comparator.comparingInt(o -> o.x)).orElseThrow().x;
        maxX = points.stream().max(Comparator.comparingInt(o -> o.x)).orElseThrow().x;
        minY = points.stream().min(Comparator.comparingInt(o -> o.y)).orElseThrow().y;
        maxY = points.stream().max(Comparator.comparingInt(o -> o.y)).orElseThrow().y;
        System.out.println();
    }

    public void printGrid() {
        StringBuilder builder = new StringBuilder();
        for(int y = minY; y <= maxY; y++) {
            for(int x = minX; x <= maxX; x++) {
                if(points.contains(new Point(x, y))) {
                    builder.append("#");
                } else {
                    builder.append(".");
                }
            }
            builder.append("\n");
        }
        System.out.println(builder);
    }

    public int calculateIndex(Point p) {
        List<Point> neighbors = new ArrayList<>();
        neighbors.addAll(List.of(
                new Point(p.x - 1, p.y - 1),
                new Point(p.x, p.y - 1),
                new Point(p.x + 1, p.y - 1),
                new Point(p.x - 1, p.y),
                new Point(p.x, p.y),
                new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y + 1),
                new Point(p.x, p.y + 1),
                new Point(p.x + 1, p.y + 1)
        ));
        int number = 0;
        boolean outsideGrid = p.x < minX || p.x >= maxX || p.y < minY || p.y >= maxY;
        for(int i = 0; i < 9; i++) {
            if(points.contains(neighbors.get(i)) || (outsideGrid && isInifinitelyLit)) {
                number += (1 << (8-i));
            }
        }
        return number;
    }

    public void part1(int iterations) {
        for(int i = 0; i < iterations; i++) {
            int margin = 3;
            Set<Point> newPoints = new HashSet<>();
            for (int y = minY - margin; y <= maxY + margin; y++) {
                for (int x = minX - margin; x <= maxX + margin; x++) {
                    int index = calculateIndex(new Point(x, y));
                    if (mapping.charAt(index) == '#') {
                        newPoints.add(new Point(x, y));
                    }
                }
            }
            points = newPoints;
            minX = points.stream().min(Comparator.comparingInt(o -> o.x)).orElseThrow().x;
            maxX = points.stream().max(Comparator.comparingInt(o -> o.x)).orElseThrow().x;
            minY = points.stream().min(Comparator.comparingInt(o -> o.y)).orElseThrow().y;
            maxY = points.stream().max(Comparator.comparingInt(o -> o.y)).orElseThrow().y;
            isInifinitelyLit = (!isInifinitelyLit && mapping.charAt(0) == '#') || (isInifinitelyLit && mapping.charAt(mapping.length() - 1) == '#');
//            printGrid();
        }
        System.out.println(points.size());
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day20 day20 = new Day20();
        day20.readFile();
        day20.printGrid();
        day20.part1(50);
        day20.printGrid();
    }


}
