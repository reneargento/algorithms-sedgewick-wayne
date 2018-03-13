package chapter2.section5;

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rene Argento on 09/04/17.
 */
public class Exercise4 {

    public static void main(String[] args) {
        String[] input = {"Algorithms", "Sedgewick", "Wayne", "Argento", "Djikstra", "Wayne", "Argento", "Prim"};

        Exercise4 exercise4 = new Exercise4();
        String[] dedupStrings = exercise4.dedup(input);

        StdOut.println("Strings:");
        for(String string : dedupStrings) {
            StdOut.println(string);
        }

        StdOut.println();
        StdOut.println("Expected: \n" +
                "Algorithms\n" +
                "Argento\n" +
                "Djikstra\n" +
                "Prim\n" +
                "Sedgewick\n" +
                "Wayne");
    }

    private String[] dedup(String[] strings) {
        if (strings == null || strings.length == 0) {
            return new String[0];
        }

        Arrays.sort(strings);

        List<String> dedupStringList = new ArrayList<>();

        String currentString = strings[0];
        dedupStringList.add(strings[0]);

        for(int i = 1; i < strings.length; i++) {
            if (strings[i].equals(currentString)) {
                continue;
            } else {
                currentString = strings[i];
            }

            dedupStringList.add(strings[i]);
        }

        String[] dedupStringArray = new String[dedupStringList.size()];
        for(int i = 0; i < dedupStringArray.length; i++) {
            dedupStringArray[i] = dedupStringList.get(i);
        }

        return dedupStringArray;
    }
}
