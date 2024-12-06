package nl.jellehulter.aoc.y2024.day4;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import nl.jellehulter.aoc.y2024.Day;

public class DayFour extends Day {

    public void part1() {
        String word = "XMAS";
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day4/dayfour.txt");
        String reverse = word.chars().boxed().toList().reversed().stream().map(Character::toString)
                .reduce("", (a, b) -> a + b);
        int fieldSize = lines.size();
        int count = 0;
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                String horizontal = getHorizontal(lines, y, x, word);
                String vertical = getVertical(y, word, lines, x);
                String firstDiagonal = getFirstDiagonal(word, lines, y, x);
                String secondDiagonal = getSecondDiagonal(word, lines, y, x);

                List<String> words = List.of(horizontal, vertical, firstDiagonal, secondDiagonal);
                List<Predicate<String>> predicates =
                        List.of(s -> s.equals(word), s -> s.equals(reverse));

                long result =
                        words.stream().map(s -> predicates.stream().filter(p -> p.test(s)).count())
                                .reduce(0L, Long::sum);
                count += result;
            }
        }
        System.out.println(count);
    }

    private String getFirstDiagonal(String word, List<String> lines, int tempY, int tempX) {
        try {
            return IntStream.range(0, word.length())
                    .map(i -> lines.get(tempY + i).charAt(tempX + i)).boxed()
                    .reduce(new StringBuilder(), (sb, i) -> sb.append(Character.toString(i)),
                            StringBuilder::append).toString();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getSecondDiagonal(String word, List<String> lines, int tempY, int tempX) {
        try {
            return IntStream.range(0, word.length())
                    .map(i -> lines.get(tempY + i).charAt(tempX - i)).boxed()
                    .reduce(new StringBuilder(), (sb, i) -> sb.append(Character.toString(i)),
                            StringBuilder::append).toString();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getVertical(int y, String word, List<String> lines, int tempX) {
        try {
            return IntStream.range(y, y + word.length())
                    .map(i -> lines.get(i).charAt(tempX)).boxed()
                    .reduce(new StringBuilder(), (sb, i) -> sb.append(Character.toString(i)),
                            StringBuilder::append).toString();
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private String getHorizontal(List<String> lines, int y, int x, String word) {
        try {
            return lines.get(y).substring(x, x + word.length());
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public void part2() {
        List<String> lines = this.readLines("src/nl/jellehulter/aoc/y2024/day4/dayfour.txt");
        String word = "MAS";
        String reverse = "SAM";
        int count = 0;
        for(int y = 0; y < lines.size(); y++) {
            for(int x = 0; x < lines.size(); x++) {
                String firstDiagonal = getFirstDiagonal(word, lines, y, x);
                String secondDiagonal = getSecondDiagonal(word, lines, y, x + word.length() - 1);
                List<String> words = List.of(firstDiagonal, secondDiagonal);
                List<Predicate<String>> predicates =
                        List.of(s -> s.equals(word), s -> s.equals(reverse));
                long result =
                        words.stream().map(s -> predicates.stream().filter(p -> p.test(s)).count())
                                .reduce(0L, Long::sum);
                if(result >= 2) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    public static void main(String[] args) {
        DayFour day = new DayFour();
        day.part1();
        day.part2();
    }
}
