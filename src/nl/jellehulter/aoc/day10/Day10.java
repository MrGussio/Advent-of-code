package nl.jellehulter.aoc.day10;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 {

    public void part1() throws IOException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day10/example.txt"));
        int sum = 0;
        while (s.hasNext()) {
            sum += getErrorValue(s.nextLine());
            System.out.println("=========");
        }
        System.out.println(sum);
    }

    public void part2() throws IOException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day10/input.txt"));
        List<BigInteger> scores = new ArrayList<>();
        while (s.hasNext()) {
            BigInteger score = BigInteger.ZERO;
            StringBuilder completion = new StringBuilder();
            String line = s.nextLine();
            String singleComplete;
            do {
                 singleComplete = getErrorValuePart2(line + completion.toString());
                 completion.append(singleComplete);
            } while(!singleComplete.equals(""));
            System.out.println(completion);
            for(char x : completion.toString().toCharArray()) {
                score = score.multiply(BigInteger.valueOf(5));
                switch (x) {
                    case ')':
                        score = score.add(BigInteger.valueOf(1));
                        break;
                    case ']':
                        score = score.add(BigInteger.valueOf(2));
                        break;
                    case '}':
                        score = score.add(BigInteger.valueOf(3));
                        break;
                    case '>':
                        score = score.add(BigInteger.valueOf(4));
                }
            }
            if(!score.equals(BigInteger.ZERO))
                scores.add(score);
        }

        scores = scores.stream().sorted().collect(Collectors.toList());
        System.out.println(scores.get(scores.size() / 2));
    }


    public int getErrorValue(String input) {
        final Day10Lexer lexer = new Day10Lexer(CharStreams.fromString(input));
        final Day10Parser parser = new Day10Parser(new CommonTokenStream(lexer));
        final int[] sum = {0};
        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charInLine, String msg, RecognitionException e) {
                CommonToken token = (CommonToken) offendingSymbol;
                int value = 0;
                switch (token.getType()) {
                    case Day10Lexer.PARENT_CLOSE:
                        value = 3;
                        break;
                    case Day10Lexer.SQUARE_CLOSE:
                        value = 57;
                        break;
                    case Day10Lexer.ACCOLADE_CLOSE:
                        value = 1197;
                        break;
                    case Day10Lexer.GREATER_THAN:
                        value = 25137;
                        break;
                }
                if (sum[0] == 0)
                    sum[0] = value;
                System.out.println("Found " + token.getText() + ", hence assign " + value);
            }

            @Override
            public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

            }

            @Override
            public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

            }

            @Override
            public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

            }
        });
        Day10Parser.StartContext startNode = parser.start();
        return sum[0];
    }

    public String getErrorValuePart2(String input) {
        final Day10Lexer lexer = new Day10Lexer(CharStreams.fromString(input));
        final Day10Parser parser = new Day10Parser(new CommonTokenStream(lexer));
        final StringBuilder completion = new StringBuilder();
        boolean[] skip = {false};
        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charInLine, String msg, RecognitionException e) {
                CommonToken token = (CommonToken) offendingSymbol;
                if(skip[0])
                    return;
                if(token.getType() != Day10Lexer.EOF)
                    skip[0] = true;

                List<Integer> tokens = recognizer.getATN().getExpectedTokens(recognizer.getState(), parser.getContext()).toList();
                for(int nextToken : tokens) {
                    switch (nextToken) {
                        case Day10Lexer.PARENT_CLOSE:
                            completion.append(')');
                            break;
                        case Day10Lexer.SQUARE_CLOSE:
                            completion.append(']');
                            break;
                        case Day10Lexer.ACCOLADE_CLOSE:
                            completion.append('}');
                            break;
                        case Day10Lexer.GREATER_THAN:
                            completion.append(">");
                            break;
                    }
                    if(completion.length() > 0)
                        break;
                }
            }

            @Override
            public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

            }

            @Override
            public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

            }

            @Override
            public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

            }
        });
        parser.start();
        if(skip[0]) {
            return "";
        }
        return completion.toString();
    }


    public static void main(String[] args) throws IOException {
        Day10 day10 = new Day10();
        day10.part2();
    }

}
