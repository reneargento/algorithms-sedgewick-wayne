package chapter3.section5;

import chapter3.section3.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/08/17.
 */
public class Exercise15 {

    private RedBlackBST<String, Integer> produceKGrams(String string, int k) {
        if (k > string.length()) {
            throw new IllegalArgumentException("k cannot be higher than string length");
        }

        RedBlackBST<String, Integer> kGrams = new RedBlackBST<>();

        for(int i = 0; i <= string.length() - k; i++) {
            String kGram = string.substring(i, i + k);
            kGrams.put(kGram, i);
        }

        return kGrams;
    }

    private void test() {
        int k = 4;
        String string = "algorithms";

        StdOut.println();
        RedBlackBST<String, Integer> kGrams = produceKGrams(string, k);
        for(String kGram : kGrams.keys()) {
            StdOut.println(kGram + " " + kGrams.get(kGram));
        }

        StdOut.println("\nExpected:\nalgo 0\ngori 2\nithm 5\nlgor 1\norit 3\nrith 4\nthms 6");
    }

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String string = StdIn.readString();

        RedBlackBST<String, Integer> kGrams = new Exercise15().produceKGrams(string, k);
        for(String kGram : kGrams.keys()) {
            StdOut.println(kGram + " " + kGrams.get(kGram));
        }

        new Exercise15().test();
    }

}
