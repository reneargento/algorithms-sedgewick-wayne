package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 03/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3 {

    private class BinarySearchSet<Key extends Comparable<Key>> {

        private Key[] keys;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchSet() {
            keys = (Key[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchSet(int capacity) {
            keys = (Key[]) new Comparable[capacity];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int rank(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int comparison = key.compareTo(keys[middle]);
                if (comparison < 0) {
                    high = middle - 1;
                } else if (comparison > 0) {
                    low = middle + 1;
                } else {
                    return middle;
                }
            }

            return low;
        }

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int rank = rank(key);

            if (rank < size && keys[rank].compareTo(key) == 0) {
                keys[rank] = key;
                return;
            }

            if (size == keys.length) {
                resize(keys.length * 2);
            }

            for(int i = size; i > rank; i--) {
                keys[i] = keys[i - 1];
            }
            keys[rank] = key;
            size++;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }

            if (isEmpty()) {
                return false;
            }

            int rank = rank(key);
            if (rank < size && keys[rank].compareTo(key) == 0) {
                return true;
            } else {
                return false;
            }
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            int rank = rank(key);
            for(int i = rank; i < size - 1; i++) {
                keys[i] = keys[i + 1];
            }

            keys[size - 1] = null;
            size--;

            if (size > 1 && size == keys.length / 4) {
                resize(keys.length / 2);
            }
        }

        public Key min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty set");
            }

            return keys[0];
        }

        public Key max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty set");
            }

            return keys[size - 1];
        }

        public Key select(int k) {
            if (isEmpty() || k >= size) {
                throw new IllegalArgumentException("Invalid argument: " + k);
            }

            return keys[k];
        }

        public Key ceiling(Key key) {
            int rank = rank(key);

            if (rank == size) {
                return null;
            }

            return keys[rank];
        }

        public Key floor(Key key) {
            if (contains(key)) {
                return key;
            }

            int rank = rank(key);

            if (rank == 0) {
                return null;
            }

            return keys[rank - 1];
        }

        public void deleteMin() {
            if (isEmpty()) {
                throw new NoSuchElementException("Set is empty");
            }

            delete(min());
        }

        public void deleteMax() {
            if (isEmpty()) {
                throw new NoSuchElementException("Set is empty");
            }

            delete(max());
        }

        public int size(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (high.compareTo(low) < 0) {
                return 0;
            } else if (contains(high)) {
                return rank(high) - rank(low) + 1;
            } else {
                return rank(high) - rank(low);
            }
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Queue<Key> queue = new Queue<>();

            for(int i = rank(low); i < rank(high); i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rank(high)]);
            }

            return queue;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        private void resize(int newSize) {
            Key[] tempKeys = (Key[]) new Comparable[newSize];
            System.arraycopy(keys, 0, tempKeys, 0, size);
            keys = tempKeys;
        }

        @Override
        public String toString() {
            if (isEmpty()) {
                return "{ }";
            }

            StringBuilder stringBuilder = new StringBuilder("{");

            boolean isFirstKey = true;
            for(Key key : keys()) {
                if (isFirstKey) {
                    isFirstKey = false;
                } else {
                    stringBuilder.append(",");
                }

                stringBuilder.append(" ").append(key);
            }

            stringBuilder.append(" }");
            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Exercise3 exercise3 = new Exercise3();
        BinarySearchSet<Integer> binarySearchSet = exercise3.new BinarySearchSet<>();

        binarySearchSet.add(5);
        binarySearchSet.add(1);
        binarySearchSet.add(9);
        binarySearchSet.add(2);
        binarySearchSet.add(0);
        binarySearchSet.add(99);
        binarySearchSet.add(-1);
        binarySearchSet.add(-2);
        binarySearchSet.add(3);
        binarySearchSet.add(-5);

        StdOut.println("Keys() test");

        for(Integer key : binarySearchSet.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99");

        StdOut.println("\ntoString() test: " + binarySearchSet);

        //Test min()
        StdOut.println("\nMin key: " + binarySearchSet.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + binarySearchSet.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchSet.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchSet.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchSet.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchSet.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + binarySearchSet.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + binarySearchSet.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + binarySearchSet.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        binarySearchSet.delete(2);

        for(Integer key : binarySearchSet.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMin()
        StdOut.println("\n\nDelete min (key -5)");
        binarySearchSet.deleteMin();

        for(Integer key : binarySearchSet.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMax()
        StdOut.println("\n\nDelete max (key 99)");
        binarySearchSet.deleteMax();

        for(Integer key : binarySearchSet.keys()) {
            StdOut.print(key + " ");
        }

        //Test keys() with range
        StdOut.println("\n\nKeys in range [2, 10]");
        for(Integer key : binarySearchSet.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [-4, -1]");
        for(Integer key : binarySearchSet.keys(-4, -1)) {
            StdOut.print(key + " ");
        }

        //Delete all
        StdOut.println("\n\nDelete all");
        while (binarySearchSet.size() > 0) {
            for(Integer key : binarySearchSet.keys()) {
                StdOut.print(key + " ");
            }

            //binarySearchSet.delete(binarySearchSet.select(0));
            binarySearchSet.delete(binarySearchSet.select(binarySearchSet.size() - 1));
            StdOut.println();
        }
    }

}
