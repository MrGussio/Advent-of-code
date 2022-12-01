package nl.jellehulter.aoc.y2021.day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Day4 {

    class Card {

        public static final int SIZE = 5;
        private int[][] numbers;
        private boolean[][] marked;

        public Card() {
            this.numbers = new int[SIZE][SIZE];
            this.marked = new boolean[SIZE][SIZE];
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    marked[y][x] = false;
                }
            }
        }

        public int getNumber(int x, int y) {
            return numbers[y][x];
        }

        public void setNumber(int x, int y, int number) {
            numbers[y][x] = number;
        }

        public void markNumber(int x, int y) {
            this.marked[y][x] = true;
        }

        public void markNumber(int number) {
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (getNumber(x, y) == number) {
                        markNumber(x, y);
                    }
                }
            }
        }

        public boolean isMarked(int x, int y) {
            return this.marked[y][x];
        }

        public boolean hasBingo() {
            //Check rows
            outer:
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (!isMarked(x, y))
                        continue outer;
                }
                return true;
            }

            //Check columns
            outer:
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    if (!isMarked(x, y))
                        continue outer;
                }
                return true;
            }

            return false;
        }

        public int unmarkedSum() {
            int sum = 0;
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (!isMarked(x, y)) {
                        sum += getNumber(x, y);
                    }
                }
            }
            return sum;
        }

        @Override
        public String toString() {
            StringBuilder output = new StringBuilder();
            for (int y = 0; y < numbers.length; y++) {
                for (int x = 0; x < numbers[y].length; x++) {
                    output.append(getNumber(x, y)).append(" ");
                }
                output.append("\n");
            }
            return output.toString();
        }

    }


    private List<Integer> drawnNumbers;
    private CopyOnWriteArrayList<Card> cards;

    public Day4() {
        drawnNumbers = new ArrayList<>();
        cards = new CopyOnWriteArrayList<>();
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day4/input.txt"));
        drawnNumbers = Arrays.stream(s.nextLine().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        while (s.hasNextLine()) {
            s.nextLine(); //Empty line
            Card card = new Card();
            cards.add(card);
            for (int y = 0; y < Card.SIZE; y++) {
                String[] numbers = s.nextLine().trim().split("[ ]+");
                assert numbers.length == Card.SIZE;
                for (int x = 0; x < Card.SIZE; x++) {
                    card.setNumber(x, y, Integer.parseInt(numbers[x]));
                }
            }
        }
    }

    public void start() {
        for (Integer number : drawnNumbers) {
            for (Card card : cards) {
                card.markNumber(number);
                if (card.hasBingo()) {
                    System.out.println(card);
                    System.out.println(card.unmarkedSum() * number);
                    return;
                }
            }
        }
    }

    public void startPart2() {
        for (Integer number : drawnNumbers) {
            for (Card card : cards) {
                card.markNumber(number);
                if (card.hasBingo()) {
                    cards.remove(card);
                    if(cards.isEmpty()) {
                        System.out.println(card);
                        System.out.println(card.unmarkedSum() * number);
                        return;
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Day4 day4 = new Day4();
        day4.readFile();
        day4.start();
        day4.startPart2();
    }

}
