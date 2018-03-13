package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise10 {

    class Version implements Comparable<Version>{

        private String version;

        Version(String version) {
            String[] versionSplit = version.split("\\.");
            if (versionSplit.length < 3) {
                throw new IllegalArgumentException("Incorrect version format. A version requires 3 parts such as 115.10.1");
            }

            this.version = version;
        }

        @Override
        public int compareTo(Version that) {
            String[] splitVersion = version.split("\\.");
            String[] otherSplitVersion = that.version.split("\\.");

            if (Integer.parseInt(splitVersion[0]) < Integer.parseInt(otherSplitVersion[0])) {
                return -1;
            } else if (Integer.parseInt(splitVersion[0]) > Integer.parseInt(otherSplitVersion[0])) {
                return 1;
            } else if (Integer.parseInt(splitVersion[1]) < Integer.parseInt(otherSplitVersion[1])) {
                return -1;
            } else if (Integer.parseInt(splitVersion[1]) > Integer.parseInt(otherSplitVersion[1])) {
                return 1;
            } else if (Integer.parseInt(splitVersion[2]) < Integer.parseInt(otherSplitVersion[2])) {
                return -1;
            } else if (Integer.parseInt(splitVersion[2]) > Integer.parseInt(otherSplitVersion[2])) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return version;
        }
    }

    public static void main(String[] args) {
        Exercise10 exercise10 = new Exercise10();
        Version version1 = exercise10.new Version("115.1.1");
        Version version2 = exercise10.new Version("115.10.1");
        Version version3 = exercise10.new Version("115.10.2");

        if (version1.compareTo(version2) < 0) {
            StdOut.println(version1 + " is less than " + version2 + " - Correct!");
        } else {
            StdOut.println(version1 + " is more than " + version2 + " - Wrong!");
        }
        StdOut.println("Expected: Correct!\n");

        if (version2.compareTo(version3) < 0) {
            StdOut.println(version2 + " is less than " + version3 + " - Correct!");
        } else {
            StdOut.println(version2 + " is more than " + version3 + " - Wrong!");
        }
        StdOut.println("Expected: Correct!");
    }

}
