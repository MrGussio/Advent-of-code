package nl.jellehulter.aoc.y2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day {

    protected List<String> readLines(String file){
        try {
            Scanner s = new Scanner(new File(file));
            List<String> lines = new ArrayList<>();
            while(s.hasNextLine()){
                lines.add(s.nextLine());
            }
            return lines;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
