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
                if(wordInCurrentLine.equals("")) {
                    continue;
                }

                wordsList.add(wordInCurrentLine);
            }
        }

        String[] words = new String[wordsList.size()] ;
        wordsList.toArray(words);

        return words;
    }

}
