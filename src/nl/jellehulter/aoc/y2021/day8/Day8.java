package nl.jellehulter.aoc.y2021.day8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Day8 {

    ArrayList<String> inputLines = new ArrayList<>();

    static final String ZERO = "abcefg";
    static final String ONE = "cf";
    static final String TWO = "acdeg";
    static final String THREE = "acdfg";
    static final String FOUR = "bcdf";
    static final String FIVE = "abdfg";
    static final String SIX = "abdefg";
    static final String SEVEN = "acf";
    static final String EIGHT = "abcdefg";
    static final String NINE = "abcdfg";

    enum Segment {
        A,
        B,
        C,
        D,
        E,
        F,
        G;

        public static Segment parse(String input) {
            assert input.length() == 1;
            return Segment.parse(input.charAt(0));
        }

        public static Segment parse(Character input) {
            switch (input) {
                case 'a':
                    return A;
                case 'b':
                    return B;
                case 'c':
                    return C;
                case 'd':
                    return D;
                case 'e':
                    return E;
                case 'f':
                    return F;
                case 'g':
                    return G;
                default:
                    return null;
            }
        }

        public Character toChar() {
            return this.name().toLowerCase().charAt(0);
        }
    }

    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day8/input.txt"));
        while (s.hasNext()) {
            inputLines.add(s.nextLine());
        }
    }

    /**
     * Calculates part 1 of the puzzle by reading the input stream and calculating the occurrences
     * of the numbers 1, 4, 7 and 8, since these have unique segment size.
     */
    public void part1() {
        int sum = 0;
        for (String line : inputLines) {
            String[] firstSplit = line.split("\\|");
            String[] outputValues = firstSplit[1].trim().split(" ");
            sum += Arrays.stream(outputValues).filter(
                    str -> List.of(2, 3, 4, 7).contains(str.length())).count(); //Segment sizes 2, 3, 4, and 7 represent the numbers 1, 4, 7 and 8 respectively
        }
        System.out.println("Result is: " + sum);
    }

    /**
     * Determine part 2 by first deducting the mapping used for every input line, and then
     * solving the encoded parts after the "|"-pipe, and finally summing all instances.
     */
    public void part2() {
        int sum = 0;
        for (String line : inputLines) {
            HashMap<Segment, Segment> mapping = new HashMap<>();
            HashMap<Integer, List<String>> sizeMap = new HashMap<>();
            String[] firstSplit = line.split("\\|");
            String[] stringSignals = firstSplit[0].trim().split(" ");
            String[] outputValues = firstSplit[1].trim().split(" ");

            for (String signal : stringSignals) {
                if (!sizeMap.containsKey(signal.length())) {
                    sizeMap.put(signal.length(), new ArrayList<>());
                }
                sizeMap.get(signal.length()).add(signal);
            }
            mapping = deduct(sizeMap);

            StringBuilder result = new StringBuilder();
            for (String output : outputValues) {
                result.append(determineValue(mapping, output.chars().mapToObj(c -> Segment.parse((char) c)).collect(Collectors.toList())));
            }
            sum += Integer.parseInt(result.toString());
        }
        System.out.println(sum);
    }

    /**
     * Deducts the mapping which has been used for the signal values.
     * @param sizeMap A map from segment size to the signals of this size (represented as a List of segments)
     * @return A mapping from original signal to mapped signal.
     */
    public HashMap<Segment, Segment> deduct(HashMap<Integer, List<String>> sizeMap) {
        HashMap<Segment, Segment> mapping = new HashMap<>();

        //Determine A by removing the nodes of the number 1 from the number 7
        if (sizeMap.containsKey(2) && sizeMap.containsKey(3)) { //Numbers 1 and 7
            List<Character> one = sizeMap.get(2).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            List<Character> seven = sizeMap.get(3).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            seven.removeAll(one);
            mapping.put(Segment.A, Segment.parse(seven.iterator().next()));
        }

        //from 9-4-a gives G
        if (sizeMap.containsKey(4) && sizeMap.containsKey(6)) { //Numbers 4 and 9
            for (String nines : sizeMap.get(6)) { //Loop through 0, 6 and 9 and determine the number 6
                List<Character> potentialNine = nines.chars().mapToObj(c -> (char) c).collect(Collectors.toList()); //Potential number 9
                List<Character> four = sizeMap.get(4).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList()); //Number 4
                potentialNine.removeAll(four);
                potentialNine.remove(mapping.get(Segment.A).toChar());
                if (potentialNine.size() == 1) {
                    mapping.put(Segment.G, Segment.parse(potentialNine.iterator().next()));
                }
            }
        }


        //We now have A and G
        //3-1-a-g becomes D
        if (sizeMap.containsKey(5) && sizeMap.containsKey(2)) { //for potential number 3 and number 1 respectively
            for (String potentialThreesString : sizeMap.get(5)) {
                List<Character> potentialThree = potentialThreesString.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                List<Character> one = sizeMap.get(2).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                potentialThree.removeAll(one);
                potentialThree.remove(mapping.get(Segment.A).toChar());
                potentialThree.remove(mapping.get(Segment.G).toChar());

                if (potentialThree.size() == 1) {
                    mapping.put(Segment.D, Segment.parse(potentialThree.iterator().next()));
                }
            }
        }


        //We now have A, D, G
        //2-1-4-A-G = E
        if (sizeMap.containsKey(5) && sizeMap.containsKey(2)) { //Potential 2 and number 1
            for (String potentialTwoString : sizeMap.get(5)) {
                List<Character> potentialTwo = potentialTwoString.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                List<Character> one = sizeMap.get(2).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                List<Character> four = sizeMap.get(4).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                potentialTwo.removeAll(one);
                potentialTwo.removeAll(four);
                potentialTwo.remove(mapping.get(Segment.A).toChar());
                potentialTwo.remove(mapping.get(Segment.G).toChar());

                if (potentialTwo.size() == 1) {
                    mapping.put(Segment.E, Segment.parse(potentialTwo.iterator().next()));
                }
            }
        }

        //We now have A, D, E, G
        //2-A-D-E-G=C
        if (sizeMap.containsKey(5)) { //Potential 2
            for (String potentialTwoString : sizeMap.get(5)) {
                List<Character> potentialTwo = potentialTwoString.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                List<Character> adeg = List.of(
                        mapping.get(Segment.A).toChar(),
                        mapping.get(Segment.D).toChar(),
                        mapping.get(Segment.E).toChar(),
                        mapping.get(Segment.G).toChar()
                );
                potentialTwo.removeAll(adeg);

                if (potentialTwo.size() == 1) {
                    mapping.put(Segment.C, Segment.parse(potentialTwo.iterator().next()));
                }
            }
        }

        //We now have A, C, D, E, G
        //7-A-C=F
        if (sizeMap.containsKey(3)) {
            List<Character> seven = sizeMap.get(3).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            List<Character> ac = List.of(
                    mapping.get(Segment.A).toChar(),
                    mapping.get(Segment.C).toChar()
            );
            seven.removeAll(ac);

            mapping.put(Segment.F, Segment.parse(seven.iterator().next()));
        }

        //We now have A, C, D, E, F, G
        //4-C-D-F=B
        if (sizeMap.containsKey(4)) {
            List<Character> four = sizeMap.get(4).get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList());
            List<Character> cdf = List.of(
                    mapping.get(Segment.C).toChar(),
                    mapping.get(Segment.D).toChar(),
                    mapping.get(Segment.F).toChar()
            );
            four.removeAll(cdf);

            mapping.put(Segment.B, Segment.parse(four.iterator().next()));
        }
        //B is wrong,F is wrong
        return mapping;

    }

    /**
     * Converts a list of segments to an integer
     * @param input The list of segments to be converted
     * @return The integer that the list represents
     */
    public int segmentsToInt(List<Segment> input) {
        String segmentString = input.stream().map(Segment::toChar).sorted().map(Object::toString).collect(Collectors.joining());
        switch (segmentString) {
            case ZERO:
                return 0;
            case ONE:
                return 1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            default:
                return -1;
        }
    }

    /**
     * Determine the value of a given input with its mapping
     * @param mapping The mapping that should be applied to the input
     * @param input The input where the value should be determined of
     * @return The integer this input represents
     */
    public int determineValue(Map<Segment, Segment> mapping, List<Segment> input) {
        mapping = mapping.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        List<Segment> mapped = input.stream().map(mapping::get).collect(Collectors.toList());
        return segmentsToInt(mapped);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day8 day8 = new Day8();
        day8.readFile();
        day8.part1();
        day8.part2();
    }


}
