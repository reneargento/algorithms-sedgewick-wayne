package chapter3.section5;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 06/08/17.
 */
// Thanks to dragon-dreamer (https://github.com/dragon-dreamer) for improving the sum() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/131
public class Exercise16 {

    private class SparseVectorSum {

        private static final double FLOATING_POINT_EPSILON = 1E-6;
        private SeparateChainingHashTable<Integer, Double> hashTable;

        public SparseVectorSum() {
            hashTable = new SeparateChainingHashTable<>();
        }

        public int size() {
            return hashTable.size();
        }

        public void put(int key, double value) {
            hashTable.put(key, value);
        }

        public double get(int key) {
            if (!hashTable.contains(key)) {
                return 0;
            } else {
                return hashTable.get(key);
            }
        }

        public void delete(int key) {
            hashTable.delete(key);
        }

        public SparseVectorSum sum(SparseVectorSum sparseVectorToSum) {
            for(Integer key : sparseVectorToSum.hashTable.keys()) {
                if (!hashTable.contains(key)) {
                    put(key, sparseVectorToSum.get(key));
                } else {
                    double sum = get(key) + sparseVectorToSum.get(key);

                    if (Math.abs(sum) <= FLOATING_POINT_EPSILON) {
                        delete(key);
                    } else {
                        put(key, sum);
                    }
                }
            }
            return this;
        }

        public double dot(double[] that) {
            double sum = 0.0;

            for(int key : hashTable.keys()) {
                sum += that[key] * this.get(key);
            }

            return sum;
        }
    }

    public static void main(String[] args) {
        //Sparse vector
        // 2.2 1.5 0 3.0 1.2
        // Sparse vector to sum
        // 0 -1.5 2.25 -3.0 -1.1999 4.2 0 9.8
        // Expected sparse vector result
        // 2.2 0 2.25 0 0.0001 4.2 0 9.8

        Exercise16 exercise16 = new Exercise16();
        SparseVectorSum sparseVector = exercise16.new SparseVectorSum();
        sparseVector.put(0, 2.2);
        sparseVector.put(1, 1.5);
        sparseVector.put(3, 3.0);
        sparseVector.put(4, 1.2);

        SparseVectorSum sparseVectorToSum = exercise16.new SparseVectorSum();
        sparseVectorToSum.put(1, -1.5);
        sparseVectorToSum.put(2, 2.25);
        sparseVectorToSum.put(3, -3.0);
        sparseVectorToSum.put(4, -1.1999);
        sparseVectorToSum.put(5, 4.2);
        sparseVectorToSum.put(7, 9.8);

        SparseVectorSum sparseVectorSumResult = sparseVector.sum(sparseVectorToSum);
        int maxKey = 0;
        for(Integer key : sparseVectorSumResult.hashTable.keys()) {
            if (key > maxKey) {
                maxKey = key;
            }
        }

        StdOut.println("Sparse Vector Result:");
        for(int i = 0; i <= maxKey; i++) {
            StdOut.printf("%.4f", sparseVectorSumResult.get(i));

            if (i != maxKey) {
                StdOut.print(" ");
            }
        }

        StdOut.println("\nExpected:\n2.2000 0.0000 2.2500 0.0000 0.0001 4.2000 0.0000 9.8000");
    }

}
