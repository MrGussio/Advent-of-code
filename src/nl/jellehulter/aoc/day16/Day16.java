package nl.jellehulter.aoc.day16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day16 {

    static class BitStream {

        private final byte[] data;
        private int index;

        public BitStream(byte[] data) {
            this.data = data;
            index = 0;
        }

        /**
         * Gets the bit at the given index
         * @param index Index of the bit to be retrieved
         * @return The bit represented in a byte (either 0b0 or 0b1)
         */
        public byte at(int index) {
            int byteIndex = index / 8;
            int bitShift = 7 - index % 8;
            return (byte) ((data[byteIndex] >> bitShift) & 1);
        }

        /**
         * Peek to the next upcoming bit in this stream
         * @return The bit represented in a byte (either 0b0 or 0b1)
         */
        public byte peek() {
            return at(index);
        }

        /**
         * Peek ahead at the next upcoming bits
         * @param n The amount of upcoming bits to peek ahead at
         * @return The bits represented as an integer
         */
        public int peek(int n) {
            int number = 0b0;
            for (int i = index; i < n + index; i++) {
                number = (number << 1) | at(i);
            }
            return number;
        }

        /**
         * Gets the next bit in this stream.
         * @return The bit represented in a byte (either 0b0 or 0b1)
         */
        public byte nextBit() {
            byte response = at(index);
            index++;
            return response;
        }

        /**
         * Get the next n bits in this stream
         * @param n The amount of bits to be retrieved from the stream
         * @return The bits represented as an integer
         */
        public int nextBits(int n) {
            int response = peek(n);
            index += n;
            return response;
        }
    }


    static abstract class Packet {

        protected int version;
        protected int type;
        protected BitStream stream;

        public Packet(BitStream bitStream) {
            this.stream = bitStream;
            version = bitStream.nextBits(3);
            type = bitStream.nextBits(3);
        }

        /**
         * Parses a single packet from the stream, then stops.
         * @param bitStream BitStream to retrieve the packet from
         * @return The parsed packet
         */
        public static Packet parse(BitStream bitStream) {
            int firstSix = bitStream.peek(6);
            int id = (firstSix >> 3) & 0b111; //First three integers
            int type = firstSix & 0b00000111;
            if (type == 4) {
                return new LiteralPacket(bitStream);
            }
            return new OperatorPacket(bitStream);
        }

        /**
         * Get the sum of the versions of all subpackets and this packet.
         * @return The sum of versions
         */
        public abstract int sumVersions();

        /**
         * Get the value this packet represents
         * @return The value
         */
        public abstract long getValue();
    }

    static class LiteralPacket extends Packet {

        List<Byte> numbers;

        public LiteralPacket(BitStream bitStream) {
            super(bitStream);
            numbers = new ArrayList<>();
            parseNumbers();
        }

        @Override
        public int sumVersions() {
            return this.version;
        }

        @Override
        public long getValue() {
            long result = 0;
            for(int i = 0; i < numbers.size(); i++) {
                byte current = numbers.get(i);
                result = (result << 4) | current;
            }
            return result;
        }

        /**
         * Parse the next literal number
         * @return The next literal number represented in a byte.
         *         Will only use the last 4 bits.
         */
        private byte nextNumber() {
            byte number = 0b0;
            for (int i = 0; i < 4; i++) {
                number = (byte) ((number << 1) | stream.nextBit());
            }
            return number;
        }

        /**
         * Parses all numbers in this literal packet.
         */
        private void parseNumbers() {
            while (true) {
                byte next = stream.nextBit();
                numbers.add(nextNumber());
                if (next == 0) {
                    break;
                }
            }
        }
    }

    static class OperatorPacket extends Packet {

        boolean I;
        int length;
        List<Packet> packets;

        public OperatorPacket(BitStream bitStream) {
            super(bitStream);
            I = stream.nextBit() == 1;
            this.length = parseLength();
            packets = new ArrayList<>();
            getPackets();
        }

        @Override
        public int sumVersions() {
            int sum = 0;
            for(Packet p : packets) {
                sum += p.sumVersions();
            }
            return sum + version;
        }

        @Override
        public long getValue() {
            List<Long> values = packets.stream().map(Packet::getValue).collect(Collectors.toList());
            switch (type){
                case 0:
                    return values.stream().reduce(0L, Long::sum);
                case 1:
                    return values.stream().reduce(1L, (a, b) -> a * b);
                case 2:
                    return values.stream().min(Long::compare).orElseThrow();
                case 3:
                    return values.stream().max(Long::compare).orElseThrow();
                case 5:
                    return values.get(0) > values.get(1) ? 1 : 0;
                case 6:
                    return values.get(0) < values.get(1) ? 1 : 0;
                case 7:
                    return Objects.equals(values.get(0), values.get(1)) ? 1 : 0;
            }
            return -1;
        }

        /**
         * Parses the length of this OperatorPacket
         * @return The length of this operator packet.
         */
        private int parseLength() {
            int x = 0;
            int n = I ? 11 : 15;
            for (int i = 0; i < n; i++) {
                x = (x << 1) | stream.nextBit();
            }
            return x;
        }

        /**
         *  Parses all subpackets of this OperationalPacket
         */
        private void getPackets() {
            if (I) {
                for (int i = 0; i < this.length; i++) {
                    packets.add(Packet.parse(stream));
                }
            } else {
                int end = length += stream.index;
                while(stream.index < end) {
                    packets.add(Packet.parse(stream));
                }
            }

        }
    }

    private byte[] data;
    public void readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src/nl/jellehulter/aoc/day16/input.txt"));
        String line = s.nextLine();
        data = hexStringToByteArray(line);
    }

    public void part1() {
        BitStream bitStream = new BitStream(data);
        Packet packet = Packet.parse(bitStream);
        System.out.println(packet.sumVersions());
    }

    public void part2() {
        BitStream bitStream = new BitStream(data);
        Packet packet = Packet.parse(bitStream);
        System.out.println(packet.getValue());
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day16 day16 = new Day16();
        day16.readFile();
        day16.part2();
    }

}
