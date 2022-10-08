package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rene Argento on 16/04/17.
 */
// Thanks to ckwastra (https://github.com/ckwastra) for suggesting a way to sort the files only once.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/269
public class Exercise29_SortFilesBySizeAndDate {

    public static void main(String[] args) {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();

        File directory = new File(path);

        //Input example: -s -d -n -t
        File[] sortedFiles = new Exercise29_SortFilesBySizeAndDate().readFlagsAndSortFiles(directory, args);
        if (sortedFiles == null) {
            return;
        }

        for (File file : sortedFiles) {
            StdOut.println(file);
        }
    }

    private static final Comparator<File> NAME_ORDER = Comparator.comparing(File::getName);
    private static final Comparator<File> SIZE_ORDER = Comparator.comparingLong(File::length);
    private static final Comparator<File> TIME_ORDER = Comparator.comparingLong(File::lastModified);
    private static final Comparator<File> NAME_REVERSED_ORDER = NAME_ORDER.reversed();
    private static final Comparator<File> SIZE_REVERSED_ORDER = SIZE_ORDER.reversed();
    private static final Comparator<File> TIME_REVERSED_ORDER = TIME_ORDER.reversed();

    private File[] readFlagsAndSortFiles(File directory, String[] flags) {
        File[] filesInCurrentDirectory = directory.listFiles();
        if (filesInCurrentDirectory == null) {
            return null;
        }

        List<File> allFiles = new ArrayList<>();
        for (File file : filesInCurrentDirectory) {
            if (!file.isDirectory()) {
                allFiles.add(file);
            }
        }

        File[] files = new File[allFiles.size()];
        allFiles.toArray(files);

        /**
         * Flags:
         * -s size
         * -t timestamp
         * -n name
         *
         * -sd size decreasing
         * -td timestamp decreasing
         * -nd name decreasing
         *
         * Usage example:
         * -s -td -n
         * First sort by file size in increasing order, then by timestamp in decreasing order and
         * then by file name in increasing order
         */
        switch (flags[0]) {
            // No duplicate file names
            case "-n":
                // Sort by file name in increasing order
                Arrays.sort(files, NAME_ORDER);
                break;
            case "-nd":
                // Sort by file name in decreasing order
                Arrays.sort(files, NAME_REVERSED_ORDER);
                break;
            default:
                Comparator<File> comparator;
                switch (flags[0]) {
                    case "-t":
                        // Sort by timestamp in increasing order
                        comparator = TIME_ORDER;
                        break;
                    case "-td":
                        // Sort by timestamp in decreasing order
                        comparator = TIME_REVERSED_ORDER;
                        break;
                    case "-s":
                        // Sort by file size in increasing order
                        comparator = SIZE_ORDER;
                        break;
                    case "-sd":
                        // Sort by file size in decreasing order
                        comparator = SIZE_REVERSED_ORDER;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid flag " + flags[0]);
                }
                // Accept up to 3 flags
                int flagsToProcess = Math.min(3, flags.length);
                outer: for (int i = 1; i < flagsToProcess; i++) {
                    switch (flags[i]) {
                        case "-n":
                            comparator = comparator.thenComparing(NAME_ORDER);
                            break outer;
                        case "-nd":
                            comparator = comparator.thenComparing(NAME_REVERSED_ORDER);
                            break outer;
                        case "-t":
                            comparator = comparator.thenComparing(TIME_ORDER);
                            break;
                        case "-td":
                            comparator = comparator.thenComparing(TIME_REVERSED_ORDER);
                            break;
                        case "-s":
                            comparator = comparator.thenComparing(SIZE_ORDER);
                            break;
                        case "-sd":
                            comparator = comparator.thenComparing(SIZE_REVERSED_ORDER);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid flag " + flags[i]);
                    }
                }
                Arrays.sort(files, comparator);
        }
        return files;
    }
}
