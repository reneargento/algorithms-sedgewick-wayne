package chapter2.section4;

import edu.princeton.cs.algs4.StdOut;
import util.ArrayUtil;

/**
 * Created by Rene Argento on 18/03/17.
 */
public class Exercise15 {

    public static void main(String[] args) {
        Comparable[] minOrientedHeap = {0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        StdOut.println("Certification 1: " + certification(minOrientedHeap) + " Expected: true");

        Comparable[] notMinOrientedHeap = {0, 0, 1, 2, -1, -5, 99, 6, 7, 8};
        StdOut.println("Certification 2: " + certification(notMinOrientedHeap) + " Expected: false");
    }

    private static boolean certification(Comparable[] pq) {

        for(int i = 1; i < pq.length / 2; i++) {
            //Check left child
            if (!ArrayUtil.less(pq[i], pq[i * 2])) {
                return false;
            }

            //Check right child
            if (i * 2 + 1 < pq.length && !ArrayUtil.less(pq[i], pq[i * 2 + 1])) {
                return false;
            }
        }

        return true;
    }

}
