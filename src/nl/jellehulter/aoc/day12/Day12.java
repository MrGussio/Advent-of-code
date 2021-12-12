package nl.jellehulter.aoc.day12;

import nl.jellehulter.aoc.day11.Day11;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    private Node start;
    private Node end;

    private List<Node> nodes;
    private List<Edge> edges;

    public Day12() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    class Node {

        private String name;

        public Node(String name) {
            this.name = name;
        }

        public boolean isSmall() {
            return this.name.chars().allMatch(Character::isLowerCase);
        }

        public boolean isStart() {
            return this.name.equals("start");
        }

        public boolean isEnd() {
            return this.name.equals("end");
        }

        public String getName() {
            return this.name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    class Edge {

        private Node node1;
        private Node node2;

        public Edge(Node node1, Node node2) {
            this.node1 = node1;
            this.node2 = node2;
        }

        public boolean contains(Node node) {
            return this.node1.equals(node) || this.node2.equals(node);
        }

        public Node other(Node node) {
            if (node1.equals(node))
                return node2;
            else if (node2.equals(node))
                return node1;
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge path = (Edge) o;
            return (Objects.equals(node1, path.node1) && Objects.equals(node2, path.node2))
                    || (Objects.equals(node1, path.node2) && Objects.equals(node2, path.node1));
        }

        @Override
        public int hashCode() {
            return Objects.hash(node1, node2);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    node1 +
                    "," + node2 +
                    '}';
        }
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day12/input.txt"));
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] split = line.split("-");
            Node node1 = new Node(split[0]);
            Node node2 = new Node(split[1]);
            Edge path = new Edge(node1, node2);
            if (!nodes.contains(node1)) {
                nodes.add(node1);
            }
            if (!nodes.contains(node2)) {
                nodes.add(node2);
            }
            if (!edges.contains(path)) {
                edges.add(path);
            }

            if (node1.isStart()) {
                this.start = node1;
            }

            if (node1.isEnd()) {
                this.end = node1;
            }

            if (node2.isStart()) {
                this.start = node2;
            }

            if (node2.isEnd()) {
                this.end = node2;
            }
        }
    }



    public void part1() {
        List<List<Node>> paths = new ArrayList<>();
        List<Node> initial = new ArrayList<>();
        initial.add(start);
        Stack<List<Node>> stack = new Stack<>();
        stack.add(initial);
        while(!stack.empty()) {
            List<Node> currentPath = stack.pop();
            Node last = currentPath.get(currentPath.size() - 1);

            //Get list of available nodes by iterating over all edges
            List<Edge> available = this.edges.stream()
                    .filter(edge -> edge.contains(last))
                    .filter(edge -> !edge.other(last).isSmall() || !currentPath.contains(edge.other(last)))
                    .collect(Collectors.toList());

            //Add node to path
            available.forEach(edge -> {
                List<Node> newPath = new ArrayList<>(currentPath);
                newPath.add(edge.other(last));
                if(edge.other(last).equals(end)) {
                    paths.add(newPath);
                } else {
                    stack.add(newPath);
                }
            });
        }

        System.out.println("Paths:" + paths.size());
    }

    public void part2() {
        List<List<Node>> paths = new ArrayList<>();
        List<Node> initial = new ArrayList<>();
        initial.add(start);
        Stack<List<Node>> stack = new Stack<>();
        stack.add(initial);
        while(!stack.empty()) {
            List<Node> currentPath = stack.pop();
            Node last = currentPath.get(currentPath.size() - 1);

            List<Edge> available = this.edges.stream()
                    .filter(edge -> edge.contains(last))
                    .filter(edge -> !edge.other(last).equals(start))
                    .filter(edge -> {
                        if (!edge.other(last).isSmall())
                            return true;
                        List<Node> smallCaves = currentPath.stream().filter(Node::isSmall).collect(Collectors.toList());
                        //If the distinct count is not equals to the original size, we know we already have a duplicate yet.
                        //If it is unequal, we know we have a duplicate and only allow a small cave when it is not in the current
                        //path yet.
                        return smallCaves.stream().distinct().count() == smallCaves.size() || !currentPath.contains(edge.other(last));
                    })
                    .collect(Collectors.toList());

            available.forEach(edge -> {
                List<Node> newPath = new ArrayList<>(currentPath);
                newPath.add(edge.other(last));
                if(edge.other(last).equals(end)) {
                    paths.add(newPath);
                } else {
                    stack.add(newPath);
                }
            });
        }

        System.out.println("Paths:" + paths.size());
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day12 day12 = new Day12();
        day12.readFile();
        day12.part1();
        day12.part2();
    }

}
