package nl.jellehulter.aoc.y2022.day3;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class DayThree {

    public void part1() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day3/daythree.txt"));
        int points = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String first = line.substring(0, line.length() / 2);
            String second = line.substring(line.length() / 2);
            for (int i = 0; i < first.length(); i++) {
                String c = String.valueOf(first.charAt(i));
                if (second.contains(c)) {
                    if (Character.isUpperCase(first.charAt(i))) {
                        points += first.charAt(i) - 'A' + 27;
                    } else {
                        points += first.charAt(i) - 'a' + 1;
                    }
                    break;
                }
            }
        }
        System.out.println(points);
    }

    public void part2() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day3/daythree.txt"));
        int points = 0;
        while (scanner.hasNext()) {

            List<Character> line1 = scanner.nextLine().chars().mapToObj(c -> (char) c).toList();
            List<Character> line2 = scanner.nextLine().chars().mapToObj(c -> (char) c).toList();
            List<Character> line3 = scanner.nextLine().chars().mapToObj(c -> (char) c).toList();
            Optional<Character> t = line1.stream().filter(line2::contains).filter(line3::contains).findFirst();
            char c = t.orElseThrow();
            if (Character.isUpperCase(c)) {
                points +=  c - 'A' + 27;
            } else {
                points += c - 'a' + 1;
            }
        }
        System.out.println(points);
    }


    public static void main(String[] args) throws FileNotFoundException {
        DayThree dayThree = new DayThree();
        dayThree.part1();
        dayThree.part2();
    }

}
