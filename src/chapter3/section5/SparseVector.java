package chapter3.section5;

import chapter3.section4.SeparateChainingHashTable;

/**
 * Created by rene on 04/08/17.
 */
public class SparseVector {

    private int dimension;
    private SeparateChainingHashTable<Integer, Double> hashTable;

    public SparseVector(int dimension) {
        hashTable = new SeparateChainingHashTable<>();
        this.dimension = dimension;
    }

    public int size() {
        return hashTable.size();
    }

    public double get(int key) {
        if(!hashTable.contains(key)) {
            return 0;
        } else {
            return hashTable.get(key);
        }
    }

    public void put(int key, double value) {
        hashTable.put(key, value);
    }

    public void delete(int key) {
        hashTable.delete(key);
    }

    public SparseVector plus(SparseVector sparseVector) {
        if(dimension != sparseVector.dimension) {
            throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
        }

        SparseVector result = new SparseVector(dimension);

        //Copy values
        for(int key : hashTable.keys()) {
            result.put(key, get(key));
        }
        //Sum values
        for(int key : sparseVector.hashTable.keys()) {
            double sum = get(key) + sparseVector.get(key);

            if(sum != 0) {
                result.put(key, sum);
            } else {
                result.delete(key);
            }
        }

        return result;
    }

    public double dot(SparseVector sparseVector) {
        if(dimension != sparseVector.dimension) {
            throw new IllegalArgumentException("Sparse vector dimensions must be the same.");
        }

        double sum = 0;

        //Iterate over the vector with the fewest nonzeros
        if(size() <= sparseVector.size()) {
            for(int key : hashTable.keys()) {
                if(sparseVector.hashTable.contains(key)) {
                    sum += get(key) * sparseVector.get(key);
                }
            }
        } else {
            for(int key : sparseVector.hashTable.keys()) {
                if(hashTable.contains(key)) {
                    sum += get(key) * sparseVector.get(key);
                }
            }
        }

        return sum;
    }

    public double dot(double[] that) {
        double sum = 0.0;

        for(int key : hashTable.keys()) {
            sum += this.get(key) * that[key];
        }

        return sum;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int key : hashTable.keys()) {
            stringBuilder.append("(").append(key).append(", ").append(get(key)).append(") ");
        }

        return stringBuilder.toString();
    }
}