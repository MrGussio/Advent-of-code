package nl.jellehulter.aoc.y2021.day18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day18 {

    private List<Pair> input;

    static abstract class Item {

        protected int depth;
        private Pair parent;

        public Item(int depth) {
            this.depth = depth;
        }

        public int getDepth() {
            return depth;
        }

        public Pair getParent() {
            return parent;
        }

        public void setParent(Pair parent) {
            this.parent = parent;
        }

        public abstract long magnitude();

        public abstract Item deepCopy();

        public static Item parse(String input, int depth, Pair parent) {
            if (!input.startsWith("[") || !input.endsWith("]")) {
                return new Number(Integer.parseInt(input), depth);
            }
            input = input.substring(1, input.length() - 1);
            int level = 0;
            for (int i = 0; i < input.length(); i++) {
                switch (input.charAt(i)) {
                    case '[':
                        level++;
                        break;
                    case ']':
                        level--;
                        break;
                    case ',':
                        if (level == 0) {
                            Item left = Pair.parse(input.substring(0, i), depth + 1, parent);
                            Item right = Pair.parse(input.substring(i + 1), depth + 1, parent);
                            assert left != null && right != null;
                            Pair pair = new Pair(left, right, depth);
                            left.setParent(pair);
                            right.setParent(pair);
                            pair.setParent(parent);
                            return pair;
                        }
                }
            }
            return null;
        }
    }

    static class Pair extends Item {

        private Item left;
        private Item right;

        public Pair(Item left, Item right, int depth) {
            super(depth);
            this.left = left;
            this.right = right;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "[" + left.toString() + "," + right.toString() + "]";
        }

        @Override
        public long magnitude() {
            return 3*left.magnitude() + 2*right.magnitude();
        }

        public Pair deepCopy() {
            Item left = this.left.deepCopy();
            Item right = this.right.deepCopy();
            Pair pair = new Pair(left, right, depth);
            left.setParent(pair);
            right.setParent(pair);
            return pair;
        }
    }

    static class Number extends Item {

        private int number;

        private Number(int number, int depth) {
            super(depth);
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Pair split() {
            Number left = new Number((int) Math.floor((double) number / 2), depth + 1);
            Number right = new Number((int) Math.ceil((double) number /2), depth + 1);
            Pair pair = new Pair(left, right, depth);
            pair.setParent(getParent());
            left.setParent(pair);
            right.setParent(pair);
            return pair;
        }

        @Override
        public String toString() {
            return String.valueOf(number);
        }

        @Override
        public long magnitude() {
            return number;
        }

        public Number deepCopy() {
            return new Number(this.number, depth);
        }
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day18/input.txt"));
        input = new ArrayList<>();
        while (s.hasNextLine()) {
            Pair i = (Pair) Item.parse(s.nextLine(), 0, null);
            input.add(i);
        }
    }

    public Number findLeftNumber(Item i) {
        Pair parent = i.getParent();
        if (parent == null) {
            return null;
        }

        if (parent.left == i) {
            return findLeftNumber(parent);
        }

        Item x = parent.left;
        while(x instanceof Pair) {
            x = ((Pair) x).right;
        }
        return (Number) x;
    }

    public Number findRightNumber(Item i) {
        Pair parent = i.getParent();
        if (parent == null) {
            return null;
        }
        if (parent.right == i) { //Keep on going up until common ancestor
            return findRightNumber(parent);
        }
        //Common ancestor found, keep on going left until a number is found.
        Item x = parent.right;
        while (x instanceof Pair) {
            x = ((Pair) x).left;
        }
        return (Number) x;
    }

    public boolean explode(Pair p) {
        if(p.left instanceof Pair) {
            if(explode((Pair) p.left)) return true;
        }
        if(p.depth == 4) {
            Number left = findLeftNumber(p);
            if(left != null) {
                left.setNumber(left.getNumber() + ((Number) p.left).getNumber());
            }
            Number right = findRightNumber(p);
            if(right != null) {
                right.setNumber(right.getNumber() + ((Number) p.right).getNumber());
            }
            if(p.getParent().left == p) {
                Number number  = new Number(0, p.getDepth());
                p.getParent().left = number;
                number.setParent(p.getParent());
            } else {
                Number number = new Number(0, p.getDepth());
                p.getParent().right = number;
                number.setParent(p.getParent());
            }
            return true;
        }

        if(p.right instanceof Pair) {
            if(explode((Pair) p.right)) return true;
        }
        return false;
    }

    public boolean split(Pair p) {
        if(p.left instanceof Pair) {
            if(split((Pair) p.left)) return true;
        }

        if(p.left instanceof Number) {
            Number left = (Number) p.left;
            if(left.getNumber() >= 10) {
                p.left = left.split();
                return true;
            }
        }

        if(p.right instanceof Number) {
            Number right = (Number) p.right;
            if(right.getNumber() >= 10) {
                p.right = right.split();
                return true;
            }
        }

        if(p.right instanceof Pair) {
            if(split((Pair) p.right)) return true;
        }
        return false;
    }

    public boolean reduce(Pair p) {
        return explode(p) || split(p);
    }

    public void incrementDepth(Pair p) {
        if(p.left instanceof Pair) {
            incrementDepth((Pair) p.left);
        } else {
            p.left.depth++;
        }
        if(p.right instanceof Pair) {
            incrementDepth((Pair) p.right);
        } else {
            p.right.depth++;
        }
        p.depth++;
    }

    public Pair add(Pair p1, Pair p2) {
        if(p1 == null) {
            return p2;
        }
        Pair pair = new Pair(p1, p2, -1);
        p1.setParent(pair);
        p2.setParent(pair);
        incrementDepth(pair);
        return pair;
    }

    public void part1() {
        Pair current = input.get(0);
        Pair next;
        for(int i = 1; i < input.size(); i++) {
            next = input.get(i);
            Pair sum = add(current, next);
            while(reduce(sum));
            current = sum;
        }
        System.out.println(current.toString());
        System.out.println(current.magnitude());
    }

    public void part2() {
        long max = 0;
        for(int i = 0; i < input.size(); i++) {
            for(int j = 0; j < input.size(); j++) {
                if(i == j)
                    continue;
                Pair p1 = input.get(i).deepCopy();
                Pair p2 = input.get(j).deepCopy();
                Pair sum = add(p1, p2);
                while(reduce(sum));
                max = Math.max(sum.magnitude(), max);
            }
        }
        System.out.println(max);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day18 day18 = new Day18();
        day18.readFile();
        day18.part2();
        day18.toString();
    }

}
