package util;

import edu.princeton.cs.algs4.StdOut;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rene Argento on 23/04/17.
 */
public class FileUtil {

    public static String[] getAllStringsFromFile(String filePath) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            StdOut.println(e.getMessage());
            return null;
        }

        List<String> wordsList = new ArrayList<>();

        for(String line : lines) {
            String[] wordsInCurrentLine = line.split(" ");

            for(String wordInCurrentLine : wordsInCurrentLine) {
                if (wordInCurrentLine.equals("")) {
                    continue;
                }

                wordsList.add(wordInCurrentLine);
            }
        }

        String[] words = new String[wordsList.size()] ;
        wordsList.toArray(words);

        return words;
    }

    public static String getAllCharactersFromFile(String filePath, boolean includeWhitespaces,
                                                  boolean includeLineBreaks) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            StdOut.println(e.getMessage());
            return null;
        }

        StringBuilder allCharacters = new StringBuilder();
        int lineNumber = 0;

        for(String line : lines) {
            if (lineNumber != 0 && includeLineBreaks) {
                allCharacters.append("\n");
            }

            for (char currentChar : line.toCharArray()) {
                if (currentChar == ' ' && !includeWhitespaces) {
                    continue;
                }

                allCharacters.append(currentChar);
            }

            lineNumber++;
        }

        return allCharacters.toString();
    }

    public static List<String> getAllLinesFromFile(String filePath) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException exception) {
            StdOut.println(exception.getMessage());
            return null;
        }

        return lines;
    }

    // Create and write file. Replaces it if it already exists.
    public static void writeFile(String filePath, List<String> data){
        StringBuilder output = new StringBuilder();

        for(String line : data) {
            output.append(line).append("\n");
        }

        byte[] dataBytes = output.toString().getBytes();

        try {
            Files.write(Paths.get(filePath), dataBytes);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteFile(String filePath) {
        try {
            Files.delete(Paths.get(filePath));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
