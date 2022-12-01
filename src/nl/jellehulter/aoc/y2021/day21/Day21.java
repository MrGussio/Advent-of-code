package nl.jellehulter.aoc.y2021.day21;

import java.util.*;

public class Day21 {

    abstract class Die {

        public abstract long roll();

        public abstract long peek();

        public abstract long rolls();
    }

    class DeterministicDie extends Die {

        private long current;
        private long rolls;

        public DeterministicDie() {
            this.current = 1;
        }

        @Override
        public long roll() {
            long response = this.current;
            current++;
            if (current > 100) {
                current = 1;
            }
            rolls++;
            return response;
        }

        @Override
        public long peek() {
            return current;
        }

        @Override
        public long rolls() {
            return rolls;
        }
    }

    int score[];
    int position[];
    int boardSize = 10;

    int turn = 0;

    public void part1() {
        position = new int[]{1, 2};
        score = new int[2];
        Die die = new DeterministicDie();
        while (score[0] < 1000 && score[1] < 1000) {
            long moves = 0;
            System.out.print("P" + turn + " rolls ");
            for (int i = 0; i < 3; i++) {
                long roll = die.roll();
                moves += roll;
                System.out.print(roll + " ");
            }
            position[turn] += moves;
            while (position[turn] > 10) {
                position[turn] -= 10;
            }
            score[turn] += position[turn];
            System.out.println("and moves to space " + position[turn] + " for a total score of " + score[turn]);
            turn = (turn + 1) % 2;
        }
        System.out.println(score[turn]);
        System.out.println(die.rolls());
        System.out.println(score[turn] * die.rolls());


    }

    class Game {

        public int[] position;
        public int[] score;
        public int turn;
        public int rolls;

        public Game() {
            this.position = new int[2];
            this.score = new int[2];
            this.turn = 0;
            this.rolls = 0;
        }

        @Override
        protected Game clone() throws CloneNotSupportedException {
            Game game = (Game) super.clone();
            game.position = position.clone();
            game.score = score.clone();
            game.turn = turn;
            game.rolls = rolls;
            return game;
        }

        public Game copy() {
            Game game = new Game();
            game.position = Arrays.copyOf(position, 2);
            game.score = Arrays.copyOf(score, 2);
            game.turn = turn;
            return game;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Game game = (Game) o;
            return turn == game.turn && Arrays.equals(position, game.position) && Arrays.equals(score, game.score);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(turn);
            result = 31 * result + Arrays.hashCode(position);
            result = 31 * result + Arrays.hashCode(score);
            return result;
        }
    }

    public void part2() throws CloneNotSupportedException {
        long[] wins = new long[2];
        Map<Game, Long> games = new HashMap<>();
        Game start = new Game();
        start.position = new int[]{1, 2};
        games.put(start, 1L);
        while (!games.isEmpty()) {
            Map<Game, Long> newMap = new HashMap<>();
            for (Game g : games.keySet()) {
                for (int i = 1; i <= 3; i++) {
                    for(int j = 1; j <= 3; j++) {
                        for(int k = 1; k <= 3; k++) {
                            Game newGame = g.copy();
                            newGame.position[newGame.turn] += i + j + k;
                            while(newGame.position[newGame.turn] > 10) {
                                newGame.position[newGame.turn] -= 10;
                            }
                            newGame.score[newGame.turn] += newGame.position[newGame.turn];
                            if(newGame.score[newGame.turn] >= 21) {
                                wins[newGame.turn] += games.get(g);
                            } else {
                                newGame.turn = (newGame.turn + 1) % 2;
                                if(newMap.containsKey(newGame)) {
                                    newMap.put(newGame,newMap.get(newGame) + games.get(g));
                                } else {
                                    newMap.put(newGame, games.get(g));
                                }
                            }
                        }
                    }
                }
            }
            games = newMap;
        }
        System.out.println(wins[0]);
        System.out.println(wins[1]);


    }


    public static void main(String[] args) throws CloneNotSupportedException {
        Day21 day21 = new Day21();
        day21.part1();
        day21.part2();
    }

}
