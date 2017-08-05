package chapter3.section5;

import chapter3.section4.SeparateChainingHashTable;

/**
 * Created by rene on 04/08/17.
 */
public class SparseVector {

    private SeparateChainingHashTable<Integer, Double> hashTable;

    public SparseVector() {
        hashTable = new SeparateChainingHashTable<>();
    }

    public int size() {
        return hashTable.size();
    }

    public void put(int key, double value) {
        hashTable.put(key, value);
    }

    public double get(int key) {
        if(!hashTable.contains(key)) {
            return 0;
        } else {
            return hashTable.get(key);
        }
    }

    public double dot(double[] that) {
        double sum = 0.0;

        for(int key : hashTable.keys()) {
            sum += that[key] * this.get(key);
        }

        return sum;
    }

}
