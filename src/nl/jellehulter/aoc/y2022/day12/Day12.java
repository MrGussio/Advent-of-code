package nl.jellehulter.aoc.y2022.day12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    Node start;

    Node end;

    static class Node {

        Map<Node, Integer> neighbours = new HashMap<>();
        List<Node> path = new ArrayList<>();
        int value;
        int distance = Integer.MAX_VALUE;

        public Node(int value) {
            this.value = value;
        }

        public void addNeighbour(Node node) {
            //Only add node if the difference is at most 1 (e.g., can only go one step up)
            if(node.value - this.value <= 1)
                neighbours.put(node, 1);
        }

    }

    final static int WIDTH = 70;
    final static int HEIGHT = 41;
//    final static int WIDTH = 8;
//    final static int HEIGHT = 5;
    Node[][] nodes = new Node[HEIGHT][];

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/nl/jellehulter/aoc/y2022/day12/day12.txt"));
        int y = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            nodes[y] = new Node[WIDTH];

            for(int x = 0; x < line.length(); x++) {
                int c = line.charAt(x);
                int value = c - 'a';
                if(c == 'S')  value = 0;
                if(c == 'E') value = 25;
                nodes[y][x] = new Node(value);
                if(c == 'S') start = nodes[y][x];
                if(c == 'E') end = nodes[y][x];
            }
            y++;
        }

        for(y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                Node current = nodes[y][x];
                if(y > 0)
                    current.addNeighbour(nodes[y -1][x]);
                if(y < HEIGHT - 1)
                    current.addNeighbour(nodes[y + 1][x]);
                if(x > 0)
                    current.addNeighbour(nodes[y][x-1]);
                if(x < WIDTH - 1)
                    current.addNeighbour(nodes[y][x+1]);
            }
        }
    }


    public void part1() throws FileNotFoundException {
        System.out.println(findShortestPath(start, end).size());
    }

    public void part2() {
        Set<Node> graph = Arrays.stream(nodes).flatMap(Arrays::stream).collect(Collectors.toSet());
        int lowestValue = Integer.MAX_VALUE;
        for(Node n : graph.stream().filter(n -> n.value == 0).toList()) {
            resetGraph(graph);
            int s = findShortestPath(n, end).size();
            //If the path is empty, that means that the path does not exist
            if(s > 0)
                lowestValue = Math.min(lowestValue, s);
        }

        System.out.println(lowestValue);
    }

    /**
     * Resets the path properties of all nodes
     * @param graph The graph to be reset
     */
    public void resetGraph(Set<Node> graph) {
        for(Node n : graph) {
            n.path = new ArrayList<>();
            n.distance = Integer.MAX_VALUE;
        }
    }

    /**
     * Find shortest path using Dijkstra
     * @param start Starting node
     * @param end Target node
     * @return The path from start to end.
     */
    public List<Node> findShortestPath(Node start, Node end) {
        Set<Node> visited = new HashSet<>();
        Set<Node> unvisited = new HashSet<>();

        unvisited.add(start);
        start.distance = 0;

        while(!unvisited.isEmpty()) {
            Node n = unvisited.stream().min(Comparator.comparingInt(o -> o.distance)).get();
            unvisited.remove(n);
            for(Map.Entry<Node, Integer> neighbour : n.neighbours.entrySet()) {
                Node neighbourNode = neighbour.getKey();
                if(!visited.contains(neighbourNode)) {
                    if(n.distance + neighbour.getValue() < neighbourNode.distance) {
                        neighbourNode.distance = n.distance + neighbour.getValue();
                        List<Node> path = new LinkedList<>(n.path);
                        path.add(n);
                        neighbourNode.path = path;
                    }
                    unvisited.add(neighbour.getKey());
                }
            }
            visited.add(n);
        }
       return end.path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day12 day12 = new Day12();
        day12.readFile();
        day12.part1();
        day12.part2();
    }
}
