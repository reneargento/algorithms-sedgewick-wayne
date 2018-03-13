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

        for(File file : sortedFiles) {
            StdOut.println(file);
        }
    }

    private File[] readFlagsAndSortFiles(File directory, String[] flags) {

        File[] filesInCurrentDirectory = directory.listFiles();
        if (filesInCurrentDirectory == null) {
            return null;
        }

        List<File> allFiles = new ArrayList<>();
        for(File file : filesInCurrentDirectory) {
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
         * -d decreasing
         *
         * Usage:
         * -s -t and -n can be followed by -d to specify a decreasing order
         *  if nothing is specified, the default is increasing order
         */
        for(int i = flags.length - 1; i >= 0; i--) {
            String flag = flags[i];

            boolean isDecreasing = flag.equals("-d");

            if (isDecreasing && i > 0) {
                i--;
                flag = flags[i];
            } else if (isDecreasing) {
                throw new IllegalArgumentException("Missing flag (-s -t or -n)");
            }

            switch (flag) {
                case "-s":
                    if (!isDecreasing) {
                        Arrays.sort(files, fileSizeIncreasingComparator());
                    } else {
                        Arrays.sort(files, fileSizeDecreasingComparator());
                    }
                    break;
                case "-t":
                    if (!isDecreasing) {
                        Arrays.sort(files, fileModifiedDateIncreasingComparator());
                    } else {
                        Arrays.sort(files, fileModifiedDateDecreasingComparator());
                    }
                    break;
                case "-n":
                    if (!isDecreasing) {
                        Arrays.sort(files, fileNameIncreasingComparator());
                    } else {
                        Arrays.sort(files, fileNameDecreasingComparator());
                    }
                    break;
                default: throw new IllegalArgumentException("Invalid flag. The only accepted flags are: -s -t -n -d");
            }
        }

        return files;
    }

    private Comparator<File> fileSizeIncreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                long size1 = file1.length();
                long size2 = file2.length();

                if (size1 < size2) {
                    return -1;
                } else if (size1 > size2) {
                    return 1;
                }
                return 0;
            }
        };
    }

    private Comparator<File> fileSizeDecreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                long size1 = file1.length();
                long size2 = file2.length();

                if (size1 > size2) {
                    return -1;
                } else if (size1 < size2) {
                    return 1;
                }
                return 0;
            }
        };
    }

    private Comparator<File> fileNameIncreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                return file1.getName().compareTo(file2.getName());
            }
        };
    }

    private Comparator<File> fileNameDecreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                int increasingCompare = file1.getName().compareTo(file2.getName());

                if (increasingCompare < 0) {
                    return 1;
                } else if (increasingCompare > 0) {
                    return -1;
                }

                return 0;
            }
        };
    }

    private Comparator<File> fileModifiedDateIncreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                long lastModified1 = file1.lastModified();
                long lastModified2 = file2.lastModified();

                if (lastModified1 < lastModified2) {
                    return -1;
                } else if (lastModified1 > lastModified2) {
                    return 1;
                }
                return 0;
            }
        };
    }

    private Comparator<File> fileModifiedDateDecreasingComparator() {
        return new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                long lastModified1 = file1.lastModified();
                long lastModified2 = file2.lastModified();

                if (lastModified1 > lastModified2) {
                    return -1;
                } else if (lastModified1 < lastModified2) {
                    return 1;
                }
                return 0;
            }
        };
    }

}
