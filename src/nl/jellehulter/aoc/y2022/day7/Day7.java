package nl.jellehulter.aoc.y2022.day7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day7 {

    class File {
        long size;
        String name;

        public File(String name, long size) {
            this.size = size;
            this.name = name;
        }

    }
    class Directory extends File {
        Map<String, File> contents = new HashMap<>();

        Directory root;

        public Directory(Directory root, String name) {
            super(name, 0);
            this.root = root;
        }

        public void addFile(File file) {
            contents.put(file.name, file);
        }

        public List<Directory> getDirectories() {
            List<Directory> results = new ArrayList<>();
            results.add(this);
            for(File f : contents.values()) {
                if(f instanceof Directory d) {
                    results.addAll(d.getDirectories());
                }
            }
            return results;
        }

        public long getSize() {
            long currentSize = 0;
            for(File f : contents.values()) {
                if(f instanceof Directory d) {
                    currentSize += d.getSize();
                } else {
                    currentSize += f.size;
                }
            }
            return currentSize;
        }
    }

    public List<Directory> dirs;
    public Directory rootDir;

    public void readFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(new java.io.File("src/nl/jellehulter/aoc/y2022/day7/day7.txt"));
        Map<String, Long> files = new HashMap<>();
        rootDir = new Directory(null, "/");
        Directory currentDir = rootDir;
        rootDir.root = rootDir;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(!line.startsWith("$")) {
                if(line.startsWith("dir")) continue;
                String[] split = line.split(" ");
                currentDir.addFile(new File(split[1], Long.parseLong(split[0])));
            } else if(line.startsWith("$ cd")) {
                String newDir = line.substring("$ cd ".length());
                if(newDir.equals("/")) {
                    currentDir = rootDir;
                } else if(newDir.equals("..")) {
                    currentDir = currentDir.root;
                } else {
                    Directory newCd = new Directory(currentDir, newDir);
                    currentDir.addFile(newCd);
                    currentDir = newCd;
                }
            }
        }
        dirs = rootDir.getDirectories();
    }
    public void part1() {
        long result = dirs.stream().mapToLong(Directory::getSize).filter(l -> l <= 100000).sum();
        System.out.println(result);
    }

    public void part2() {
        long requiredSpace = 30000000 - (70000000 - rootDir.getSize());
        long smallestDir = 30000000;
        for(Directory d : dirs) {
            long size = d.getSize();
            if(size < requiredSpace) continue;;
            smallestDir = Math.min(smallestDir, size);
        }
        System.out.println(smallestDir);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day7 day7 = new Day7();
        day7.readFile();
        day7.part1();
        day7.part2();
    }
}
