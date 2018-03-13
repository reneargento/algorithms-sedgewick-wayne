package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Rene Argento on 16/04/17.
 */
public class Exercise28_SortFilesByName {

    // Parameter example: [any file path]
    public static void main(String[] args) {
        String directoryPath = args[0];
        String[] sortedFiles = new Exercise28_SortFilesByName().fileSorter(directoryPath);

        if (sortedFiles == null) {
            return;
        }

        for(String fileName : sortedFiles) {
            StdOut.println(fileName);
        }
    }

    private String[] fileSorter(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            throw new IllegalArgumentException(directoryPath + " does not exist");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directoryPath + " is not a directory");
        }

        File[] allFiles = directory.listFiles();
        if (allFiles == null || allFiles.length == 0) {
            return null;
        }

        String[] fileNames = new String[allFiles.length];
        for(int i = 0; i < fileNames.length; i++) {
            fileNames[i] = allFiles[i].getName();
        }

        Arrays.sort(fileNames);
        return fileNames;
    }

}
