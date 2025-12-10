package nl.jellehulter.aoc.y2025.day9;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.awt.geom.Line2D.linesIntersect;

public class Day9 {
    
    private final String testInput = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""";
    
    public void part1() throws IOException {
        String input = Files.readString(Path.of("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day9\\input.txt"));
        List<Coord> coords = input.lines()
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Coord(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }).toList();
        long max = 0;
        for(int i = 0; i < coords.size(); i++) {
            for(int j = i + 1; j < coords.size(); j++) {
                Coord a = coords.get(i);
                Coord b = coords.get(j);
                long size = a.size(b);
                System.out.println(a + " to " + b + ": " + size);
                max = Math.max(max,size);
            }
        }
        System.out.println(max);
    }
    
    public void part2() throws IOException {
        String input = Files.readString(Path.of("C:\\Users\\JelleH\\IdeaProjects\\Advent-of-code\\src\\nl\\jellehulter\\aoc\\y2025\\day9\\input.txt"));
        List<Coord> coords = input.lines()
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Coord(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }).toList();
        long max = 0;
        System.out.println(max);
        Path2D polygon = new Path2D.Double();
        for(Coord p : coords) {
            if(p == coords.getFirst()) {
                polygon.moveTo(p.x, p.y);
            } else {
                polygon.lineTo(p.x, p.y);
            }
        }
        polygon.closePath();

        for(int i = 0; i < coords.size(); i++) {
            for(int j = i + 1; j < coords.size(); j++) {
                Coord a = coords.get(i);
                Coord b = coords.get(j);
                Rectangle2D rect = new Rectangle2D.Double(
                        Math.min(a.x, b.x),
                        Math.min(a.y, b.y),
                        Math.abs(a.x - b.x),
                        Math.abs(a.y - b.y)
                );
                if(polygon.contains(rect)) {
                    long size = a.size(b);
                    System.out.println(a + " to " + b + ": " + size);
                    max = Math.max(max,size);
                }
            }
        }
        System.out.println(max);
    }
    
    record Coord(long x, long y) {
        
        public long size(Coord other) {
            return (Math.abs(x - other.x) + 1) * (Math.abs(y - other.y) + 1);
        }
        
    }
//    
//    record Polygon(List<Coord> points) {
//        
//        boolean contains(Coord c) {
//            //Ray-casting algorithm
//            //https://math.stackexchange.com/questions/3210317/how-to-check-if-a-given-point-lies-inside-a-rectilinear-figure
//            int intersections = 0;
//            for(int i = 0; i < points.size(); i++) {
//                Coord a = points.get(i);
//                Coord b = points.get((i + 1) % points.size());
//                if(isOnLine(c, a, b)) {
//                    return true;
//                }
//            }
//            
//
//            
//            
//            long startX = 0;
//            for(long x = 0; x < c.x; x++) {
//                Coord rayEnd = new Coord(x, c.y);
//                for(int i = 0; i < points.size(); i++) {
//                    Coord a = points.get(i);
//                    Coord b = points.get((i + 1) % points.size());
//                    if(linesIntersect(c, rayEnd, a, b)) {
//                        intersections++;
//                    }
//                }
//            }
//            return intersections % 2 == 1;
//        }
//        
//        private boolean isOnLine(Coord p, Coord a, Coord b) {
//            return (p.x <= Math.max(a.x, b.x) && p.x >= Math.min(a.x, b.x) &&
//                    p.y <= Math.max(a.y, b.y) && p.y >= Math.min(a.y, b.y));
//        }
//        
//        
//        
//        
//    }

    public static void main(String[] args) throws IOException {
        Day9 day9 = new Day9();
        day9.part1();
        day9.part2();
    }
    
    
}
