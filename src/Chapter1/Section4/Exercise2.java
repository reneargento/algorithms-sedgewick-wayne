package Chapter1.Section4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;

/**
 * Created by rene on 9/27/16.
 */
public class Exercise2 {

    public static int count(int[] a) {
        //Count triples that sum to 0.
        int N = a.length;
        int cnt = 0;

        BigInteger bigInteger;

        for (int i=0; i < N; i++) {
            for (int j=i+1; j < N; j++){
                for(int k=j+1; k < N; k++){
                    bigInteger = BigInteger.valueOf(a[i]);
                    bigInteger = bigInteger.add(BigInteger.valueOf(a[j]));
                    bigInteger = bigInteger.add(BigInteger.valueOf(a[k]));

                    if(bigInteger.intValue() == 0) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] a = in.readAllInts();
        StdOut.println(count(a));
    }

}
