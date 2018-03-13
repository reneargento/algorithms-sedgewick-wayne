package chapter1.section4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;

/**
 * Created by Rene Argento on 9/27/16.
 */
public class Exercise2 {

    public static int count(int[] array) {
        //Count triples that sum to 0.
        int length = array.length;
        int count = 0;

        BigInteger bigInteger;

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for(int k = j + 1; k < length; k++) {
                    bigInteger = BigInteger.valueOf(array[i]);
                    bigInteger = bigInteger.add(BigInteger.valueOf(array[j]));
                    bigInteger = bigInteger.add(BigInteger.valueOf(array[k]));

                    if (bigInteger.intValue() == 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] array = in.readAllInts();
        StdOut.println(count(array));
    }

}
