package nl.jellehulter.aoc.y2022.day14;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day14 {

    Map<Point, Character> grid = new HashMap<>();


    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day14/day14.txt"));
        grid = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] coords = line.split(" -> ");
            Point previous = null;
            for(int i = 0; i < coords.length; i++) {
                String[] split = coords[i].split(",");
                Point current = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                if(previous == null) {
                    previous = current;
                    continue;
                }

                for(int x = Math.min(current.x, previous.x); x <= Math.max(current.x, previous.x); x++) {
                    for(int y = Math.min(current.y, previous.y); y <= Math.max(current.y, previous.y); y++) {
                        grid.put(new Point(x, y), '#');
                    }
                }

                previous = current;
            }
        }
    }

    public void part1() {
        int maxY = grid.keySet().stream().mapToInt((p -> (int) p.getY())).max().getAsInt() + 10;
        Point source = new Point(500, 0);
        outer:
        while (true) {
            Point sand = new Point(source);
            while(true) {
                Point below = new Point(sand);
                Point leftDiag = new Point(sand);
                Point rightDiag = new Point(sand);
                below.translate(0, 1);
                leftDiag.translate(-1, 1);
                rightDiag.translate(1, 1);
                if(!grid.containsKey(below)) {
                    sand = below;
                    if(below.getY() > maxY) {
                        break outer;
                    }
                } else if(!grid.containsKey(leftDiag)) {
                    sand = leftDiag;
                } else if(!grid.containsKey(rightDiag)) {
                    sand = rightDiag;
                } else {
                    grid.put(sand, 'O');
//                    printGrid();
                    break;
                }
            }
        }
        System.out.println(grid.values().stream().filter(c -> c == 'O').count());
    }

    public void part2() {
        int maxY = grid.keySet().stream().mapToInt((p -> (int) p.getY())).max().getAsInt() + 2;
        Point source = new Point(500, 0);
        outer:
        while (true) {
            Point sand = new Point(source);
            while(true) {
                Point below = new Point(sand);
                Point leftDiag = new Point(sand);
                Point rightDiag = new Point(sand);
                below.translate(0, 1);
                leftDiag.translate(-1, 1);
                rightDiag.translate(1, 1);
                if(!grid.containsKey(below)) {
                    if(below.getY() == maxY) {
                        grid.put(sand, 'O');
                        break;
                    }
                    sand = below;
                } else if(!grid.containsKey(leftDiag)) {
                    sand = leftDiag;
                } else if(!grid.containsKey(rightDiag)) {
                    sand = rightDiag;
                } else {
                    grid.put(sand, 'O');
//                    printGrid();
                    if(grid.containsKey(source)) {
                        break outer;
                    }
                    break;
                }
            }
        }
        System.out.println(grid.values().stream().filter(c -> c == 'O').count());
    }

    public void printGrid() {
        int minX = grid.keySet().stream().mapToInt(p -> (int) p.getX()).min().getAsInt();
        int minY = grid.keySet().stream().mapToInt(p -> (int) p.getY()).min().getAsInt();
        int maxX = grid.keySet().stream().mapToInt(p -> (int) p.getX()).max().getAsInt();
        int maxY = grid.keySet().stream().mapToInt(p -> (int) p.getY()).max().getAsInt();

        for(int y = minY; y <= maxY; y++) {
            for(int x = minX; x <= maxX; x++) {
                Point p = new Point(x, y);
                if(grid.containsKey(p)) {
                    Character c = grid.get(p);
                    System.out.print(c);
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println("".repeat(20));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day14 day14 = new Day14();
        day14.readFile();
        day14.part1();
        day14.readFile();
        day14.part2();
    }
}
