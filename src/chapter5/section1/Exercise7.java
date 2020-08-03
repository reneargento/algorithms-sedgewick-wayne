package chapter5.section1;

import chapter1.section3.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.StringJoiner;

/**
 * Created by Rene Argento on 08/01/18.
 */
// Thanks to AdamShamaa (https://github.com/AdamShamaa) for suggesting an optimization to this exercise.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/173

// Assumes that all Strings have the same length
@SuppressWarnings("unchecked")
public class Exercise7 {

    public void keyIndexedCountWithQueue(String[] array, int stringsLength) {
        int alphabetSize = 256; // Extended ASCII characters

        Queue<String> count[] = new Queue[alphabetSize + 1];

        for(int r = 0; r < count.length; r++) {
            count[r] = new Queue();
        }

        for (int digit = stringsLength - 1; digit >= 0; digit--) {

            // Compute frequency counts
            for(int i = 0; i < array.length; i++) {
                int digitIndex = array[i].charAt(digit);
                count[digitIndex].enqueue(array[i]);
            }

            // Distribute and copy back
            int indexArray = 0;
            for(int r = 0; r < count.length; r++) {
                while (!count[r].isEmpty()) {
                    String string = count[r].dequeue();
                    array[indexArray++] = string;
                }
            }
        }
    }

    public static void main(String[] args) {
        Exercise7 exercise7 = new Exercise7();

        String[] array = {"4PGC938",
                "2IYE230",
                "3CIO720",
                "1ICK750",
                "1OHV845",
                "4JZY524",
                "1ICK750",
                "3CIO720",
                "1OHV845",
                "1OHV845",
                "2RLA629",
                "2RLA629",
                "3ATW723"};
        exercise7.keyIndexedCountWithQueue(array, 7);

        StringJoiner sortedArray = new StringJoiner(" ");

        for(String string : array) {
            sortedArray.add(string);
        }
        StdOut.println("Sorted array");
        StdOut.println(sortedArray);
        StdOut.println("Expected: \n" +
                "1ICK750 1ICK750 1OHV845 1OHV845 1OHV845 2IYE230 2RLA629 2RLA629 3ATW723 3CIO720 3CIO720 " +
                "4JZY524 4PGC938");
    }

}
