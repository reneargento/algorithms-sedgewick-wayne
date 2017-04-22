package chapter3.section1;

import edu.princeton.cs.algs4.Queue;

/**
 * Created by rene on 22/04/17.
 */
@SuppressWarnings("unchecked")
public class BinarySearchSymbolTable<Key extends Comparable<Key>, Value> {

    private Key[] keys;
    private Value[] values;
    private int size;

    public BinarySearchSymbolTable(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        values = (Value[]) new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Value get(Key key) {
        if(isEmpty()) {
            return null;
        }

        int rank = rank(key);
        if( rank < size && keys[rank].compareTo(key) == 0) {
            return values[rank];
        } else {
            return null;
        }
    }

    public int rank(Key key) {
        int low = 0;
        int high = size - 1;

        while (low <= high) {
            int middle = low + (high - low) / 2;

            int comparison = key.compareTo(keys[middle]);
            if(comparison < 0) {
                high = middle - 1;
            } else if (comparison > 0) {
                low = middle + 1;
            } else {
                return middle;
            }
        }

        return low;
    }

    public void put(Key key, Value value) {
        int rank = rank(key);

        if(rank < size && keys[rank].compareTo(key) == 0) {
            values[rank] = value;
            return;
        }

        for(int i = size; i > rank; i--) {
            keys[i] = keys[i - 1];
            values[i] = values[i - 1];
        }
        keys[rank] = key;
        values[rank] = value;
        size++;
    }

    public boolean contains(Key key) {
        int rank = rank(key);

        if(rank < size && keys[rank].compareTo(key) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void delete(Key key) {
        //Exercise 3.1.16
    }

    public Key min() {
        if(isEmpty()) {
            return null;
        }

        return keys[0];
    }

    public Key max() {
        if(isEmpty()) {
            return null;
        }

        return keys[size - 1];
    }

    public Key select(int k) {
        if(isEmpty() || k >= size) {
            return null;
        }

        return keys[k];
    }

    public Key ceiling(Key key) {
        int rank = rank(key);

        if(rank == size) {
            return keys[rank - 1];
        }

        return keys[rank];
    }

    public Key floor(Key key) {
        //Exercise 3.1.17
        return null;
    }

    public Iterable<Key> keys(Key low, Key high) {
        Queue<Key> queue = new Queue<>();

        for(int i = rank(low); i < rank(high); i++) {
            queue.enqueue(keys[i]);
        }

        if(contains(high)) {
            queue.enqueue(keys[rank(high)]);
        }

        return queue;
    }
}
