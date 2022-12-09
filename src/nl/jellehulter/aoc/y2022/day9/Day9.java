package nl.jellehulter.aoc.y2022.day9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day9 {

    class Knot {
        int x;
        int y;
        int id;

        Knot follows;

        public Knot(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
            this.follows = null;
        }

        public void move() {
            int dx = follows.x - x;
            int dy = follows.y - y;
            if(Math.pow(dx, 2) + Math.pow(dy, 2) > 2) {
                if(dx > 0) x++;
                if(dx < 0) x--;
                if(dy > 0) y++;
                if(dy < 0) y--;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Knot knot = (Knot) o;
            return id == knot.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day9/day9.txt"));
        printGrid(0, 0, 0, 0);
        int hx = 0, hy = 0;
        int tx = 0, ty = 0;
        Set<String> positions = new HashSet<>();
        positions.add("0,0");
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            char dir = split[0].charAt(0);
            int moves = Integer.parseInt(split[1]);
            for(int i = 0; i < moves; i++) {
                switch (dir) {
                    case 'U' -> hy++;
                    case 'D' -> hy--;
                    case 'L' -> hx--;
                    case 'R' -> hx++;
                }
                int dx = hx - tx;
                int dy = hy - ty;
                if(Math.pow(dx, 2) + Math.pow(dy, 2) > 2) {
                    if(dx > 0) tx++;
                    if(dx < 0) tx--;
                    if(dy > 0) ty++;
                    if(dy < 0) ty--;
                }
                System.out.println(line);
                printGrid(hx, hy, tx, ty);
                System.out.println("=============");
                String pos = tx + "," + ty;
                positions.add(pos);
            }
        }
        System.out.println(positions.size());
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day9/day9.txt"));
        List<Knot> knots = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            knots.add(new Knot(0, 0, i));
            if(i > 0)
                knots.get(i).follows = knots.get(i - 1);
        }
        Knot head = knots.get(0);
        Knot tail = knots.get(9);
        Set<String> positions = new HashSet<>();
        positions.add("0,0");
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            char dir = split[0].charAt(0);
            int moves = Integer.parseInt(split[1]);
            for(int i = 0; i < moves; i++) {
                switch (dir) {
                    case 'U' -> head.y++;
                    case 'D' -> head.y--;
                    case 'L' -> head.x--;
                    case 'R' -> head.x++;
                }
                for(int j = 1; j < knots.size(); j++) {
                    knots.get(j).move();
                }
                String pos = tail.x + "," + tail.y;
                positions.add(pos);
            }
        }
        System.out.println(positions.size());
    }

    public void printGrid(int hx, int hy, int tx, int ty) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int y = 5; y >= -5; y--) {
            for(int x = -5; x <= 5; x++) {
                if(x == hx && y == hy) {
                    stringBuilder.append("H");
                } else if(x == tx && y == ty) {
                    stringBuilder.append("T");
                } else {
                    stringBuilder.append(".");
                }
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day9 day9 = new Day9();
        day9.part2();
    }

}
