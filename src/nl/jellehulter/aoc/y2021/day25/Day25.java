package nl.jellehulter.aoc.y2021.day25;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Day25 {

    Map<Point, Character> map = new ConcurrentHashMap<>();
    int width = 0;
    int height = 0;

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day25/input.txt"));
        int y = 0;
        while (s.hasNextLine()) {
            char[] line = s.nextLine().toCharArray();
            for (int x = 0; x < line.length; x++) {
                if (line[x] == '.') {
                    continue;
                }
                map.put(new Point(x, y), line[x]);
            }
            width = Math.max(line.length - 1, width);
            height = Math.max(y, height);
            y++;
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for(int y = 0; y <= height; y++) {
            for(int x = 0; x <= width; x++) {
                Point p = new Point(x, y);
                if(map.containsKey(p)) {
                    sb.append(map.get(p));
                } else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void part1() {
        boolean changed = true;
        int count = 0;
//        System.out.println(print());
        while (changed) {
            Map<Point, Character> next = new ConcurrentHashMap<>();
            changed = false;
            for (Map.Entry<Point, Character> entry : map.entrySet()) {
                if (entry.getValue() == '>') { //First only east
                    Point target = new Point((entry.getKey().x + 1) % (width + 1), entry.getKey().y);
                    if (!map.containsKey(target)) {
                        next.put(target, entry.getValue());
                        changed = true;
                    } else {
                        next.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    next.put(entry.getKey(), entry.getValue());
                }
            }
            map = next;
//            System.out.println(print());
            next = new HashMap<>();
            for (Map.Entry<Point, Character> entry : map.entrySet()) {
                if (entry.getValue() == 'v') { //First only east
                    Point target = new Point(entry.getKey().x, (entry.getKey().y + 1) % (height + 1));
                    if (!map.containsKey(target)) {
                        next.put(target, entry.getValue());
                        changed = true;
                    } else {
                        next.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    next.put(entry.getKey(), entry.getValue());
                }
            }
            map = next;
            count++;
            System.out.println(print());
            System.out.println(count);
        }
        System.out.println(count);
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day25 day25 = new Day25();
        day25.readFile();
        day25.part1();
    }

}
