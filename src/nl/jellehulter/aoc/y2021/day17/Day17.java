package nl.jellehulter.aoc.y2021.day17;

import java.awt.*;

public class Day17 {


    public void part1() {
        //target area: x=70..125, y=-159..-121
        long maxY = 0;
        long differentVelocities = 0;
        for(int vxStart = 1; vxStart < 140; vxStart++) {
            outer:
            for(int vyStart = -170; vyStart < 1500; vyStart++) {
//                System.out.println(vxStart + "," +vyStart);
                int x = 0;
                int y = 0;
                int vx = vxStart;
                int vy = vyStart;
                long currentMaxY = 0;
                for(int t = 0; t < 5000; t++) {
                    x += vx;
                    y += vy;
                    if(vx > 0) vx --;
                    if(vx < 0) vx++;
                    vy--;
                    currentMaxY = Math.max(currentMaxY, y);
                    if(x >= 70 && x <= 125 && y >= -159 && y <= -121) {
                        differentVelocities++;
                        if(currentMaxY > maxY) {
                            System.out.println("New maxY " + currentMaxY + " for ("+vxStart + "," + vyStart + ")");
                        }
                        maxY = Math.max(maxY, currentMaxY);
                        continue outer;
                    }
                }
            }
        }
        System.out.println(maxY);
        System.out.println(differentVelocities);

    }

    public static void main(String[] args) {
        Day17 day17 = new Day17();
        day17.part1();
    }

}
