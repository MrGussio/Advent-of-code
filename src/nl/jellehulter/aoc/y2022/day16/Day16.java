package nl.jellehulter.aoc.y2022.day16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.LoggingPermission;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day16 {

    class Valve {

        final String name;

        final int flow;

        final Set<String> tunnels;

        public Valve(String name, int flow, Set<String> tunnels) {
            this.name = name;
            this.flow = flow;
            this.tunnels = tunnels;
        }

        @Override
        public String toString() {
            return "Valve{" +
                    "name='" + name + '\'' +
                    ", flow=" + flow +
                    ", tunnels=" + tunnels +
                    '}';
        }


    }

    class State {
        final Set<String> openedValves;
        String currentValve;

        public State(String currentValve, Set<String> openedValves) {
            this.currentValve = currentValve;
            this.openedValves = openedValves;
        }

        public int getPressure() {
            return openedValves.stream().mapToInt(valve -> valves.get(valve).flow).sum();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return openedValves.equals(state.openedValves) && currentValve.equals(state.currentValve);
        }

        @Override
        public int hashCode() {
            return Objects.hash(openedValves, currentValve);
        }

        @Override
        public State clone() {
            return new State(currentValve, new HashSet<>(openedValves));
        }

        @Override
        public String toString() {
            return currentValve + ", " + openedValves.toString() + ", " + getPressure();
        }
    }

    Map<String, Valve> valves;

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day16/day16.txt"));
        valves = new HashMap<>();
        Pattern pattern = Pattern.compile("Valve ([A-Z][A-Z]) has flow rate=(.+); tunnels? leads? to valves? (.*)");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                System.out.println("Found!");
                String valveName = matcher.group(1);
                int rate = Integer.parseInt(matcher.group(2));
                Set<String> tunnels = Set.of(matcher.group(3).split(", "));
                valves.put(valveName, new Valve(valveName, rate, tunnels));
            } else {
                System.out.println("Not found!");
                System.out.println(line);
            }
        }
        System.out.println();
    }

    public void part1() {
        Map<State, Integer> states = new HashMap<>();
        State start = new State("AA", new HashSet<>());
        states.put(start, 0);
        for (int i = 1; i <= 30; i++) {
            HashMap<State, Integer> next = new HashMap<>();
            HashMap<State, Integer> increased = new HashMap<>();
            for (Map.Entry<State, Integer> state : states.entrySet()) {
                increased.put(state.getKey(), state.getValue() + state.getKey().getPressure());
            }
            states = increased;
            Map.Entry<State, Integer> max = increased.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
            System.out.println("Minute " + i + ", max flow + " + max.getKey().getPressure() + ", max " + max.getValue());
            for (State s : states.keySet()) {
                int pressure = states.get(s);
                if (s.openedValves.size() == valves.size()) {
                    next.put(s, pressure);
                    continue;
                }
                //Check whether the current valve can be opened
                if (valves.get(s.currentValve).flow > 0 && !s.openedValves.contains(s.currentValve)) {
                    State openedState = s.clone();
                    openedState.openedValves.add(openedState.currentValve);
                    if (next.containsKey(openedState)) {
                        pressure = Math.max(pressure, next.get(openedState));
                    }
                    next.put(openedState, pressure);
                }
                for (String nextValveName : valves.get(s.currentValve).tunnels) {
                    pressure = states.get(s);
                    State nextState = s.clone();
                    nextState.currentValve = nextValveName;
                    if (next.containsKey(nextState)) {
                        pressure = Math.max(pressure, next.get(nextState));
                    }
                    next.put(nextState, pressure);
                }
            }
            states = next;
        }
        Map.Entry<State, Integer> winning = states.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        System.out.println(winning.getValue());
    }

    class TwoPlayerState {
        final Set<String> openedValves;
        String player1;
        String player2;

        public TwoPlayerState(Set<String> openedValves, String player1, String player2) {
            this.openedValves = openedValves;
            this.player1 = player1;
            this.player2 = player2;
        }

        public int getPressure() {
            return openedValves.stream().mapToInt(valve -> valves.get(valve).flow).sum();
        }

        @Override
        public TwoPlayerState clone() {
            return new TwoPlayerState(new HashSet<>(openedValves), player1, player2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TwoPlayerState that = (TwoPlayerState) o;
            return openedValves.equals(that.openedValves) &&
                    ((player1.equals(that.player1) && player2.equals(that.player2))
                            || (player1.equals(that.player2) && player2.equals(that.player1)));
        }

        @Override
        public int hashCode() {
            //https://stackoverflow.com/questions/15877914/java-overriding-equals-and-hashcode-for-two-interchangeable-integers
            int hash = 27644437 * (Objects.hash(player1) + Objects.hash(player2)) + Math.min(Objects.hash(player1), Objects.hash(player2));
            return Objects.hash(openedValves, hash);
        }

        @Override
        public String toString() {
            return player1 + ", " + player2 + ", " + openedValves.toString() + ", " + getPressure();
        }
    }

    public void part2() {
        Map<TwoPlayerState, Integer> states = new HashMap<>();
        TwoPlayerState start = new TwoPlayerState(new HashSet<>(), "AA", "AA");
        int valvesWithNonZero = (int) valves.values().stream().filter(v -> v.flow > 0).count();
        int maxPressure = valves.values().stream().mapToInt(v -> v.flow).sum();
        states.put(start, 0);
        for (int i = 1; i <= 26; i++) {
            HashMap<TwoPlayerState, Integer> next = new HashMap<>();
            HashMap<TwoPlayerState, Integer> increased = new HashMap<>();
            for (Map.Entry<TwoPlayerState, Integer> state : states.entrySet()) {
                increased.put(state.getKey(), state.getValue() + state.getKey().getPressure());
            }
            states = increased;
            Map.Entry<TwoPlayerState, Integer> max = increased.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
            System.out.println("Minute " + i + ", max flow + " + max.getKey().getPressure() + ", max " + max.getValue());
            for (TwoPlayerState s : states.keySet()) {
                final int pressure = states.get(s);
                if (s.openedValves.size() == valvesWithNonZero) {
                    next.merge(s, pressure, Math::max);
                    continue;
                }
                Set<String> p1States = new HashSet<>(valves.get(s.player1).tunnels);
                Set<String> p2States = new HashSet<>(valves.get(s.player2).tunnels);
                boolean p1CanOpen = valves.get(s.player1).flow > 0 && !s.openedValves.contains(s.player1);
                boolean p2CanOpen = valves.get(s.player2).flow > 0 && !s.openedValves.contains(s.player2);
                if(p1CanOpen) {
                    for(String nextP2Valve : p2States) {
                        TwoPlayerState nextState = s.clone();
                        nextState.player2 = nextP2Valve;
                        nextState.openedValves.add(s.player1);
                        next.merge(nextState, pressure, Math::max);
                    }
                }
                if(p2CanOpen) {
                    for(String nextP1Valve : p1States) {
                        TwoPlayerState nextState = s.clone();
                        nextState.player1 = nextP1Valve;
                        nextState.openedValves.add(s.player2);
                        next.merge(nextState, pressure, Math::max);
                    }
                }
                if(p1CanOpen && p2CanOpen && !s.player1.equals(s.player2)) {
                    TwoPlayerState nextState = s.clone();
                    nextState.openedValves.add(s.player1);
                    nextState.openedValves.add(s.player2);
                    next.merge(nextState, pressure, Math::max);
                }
                for(String nextP1Valve : p1States) {
                    for(String nextP2Valve : p2States) {
                        TwoPlayerState nextState = s.clone();
                        nextState.player1 = nextP1Valve;
                        nextState.player2 = nextP2Valve;
                        next.merge(nextState, pressure, Math::max);
                    }
                }
            }
            states = next;
            Optional<Map.Entry<TwoPlayerState, Integer>> maxedOut = states.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue));
            if(maxedOut.isPresent()) {
                Map.Entry<TwoPlayerState, Integer> entry = maxedOut.get();
                if(entry.getKey().openedValves.size() == valvesWithNonZero) {
                    System.out.println(entry.getKey() + ", " + entry.getValue());
                    System.out.println(entry.getValue() + (26-i) * entry.getKey().getPressure());
                    return;
                }
//                System.out.println(maxedOut.);
            }
        }
        Map.Entry<TwoPlayerState, Integer> winning = states.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        System.out.println(winning.getValue());
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day16 day16 = new Day16();
        day16.readFile();
        day16.part2();
    }

}
