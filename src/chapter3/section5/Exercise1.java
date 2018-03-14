package chapter3.section5;

import chapter3.section3.RedBlackBST;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 02/08/17.
 */
public class Exercise1 {

    private class Set<Key extends Comparable<Key>> {

        private RedBlackBST<Key, Boolean> set;

        Set() {
            set = new RedBlackBST<>();
        }

        public boolean isEmpty() {
            return set.isEmpty();
        }

        public int size() {
            return set.size();
        }

        public boolean contains(Key key) {
            return set.contains(key);
        }

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            set.put(key, false);
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (set.isEmpty() || !contains(key)) {
                return;
            }

            set.delete(key);
        }

        public Key min() {
            if (isEmpty()) {
                return null;
            }

            return set.min();
        }

        public Key max() {
            if (isEmpty()) {
                return null;
            }

            return set.max();
        }

        public Key floor(Key key) {
            return set.floor(key);
        }

        public Key ceiling(Key key) {
            return set.ceiling(key);
        }

        public Key select(int index) {
            if (index >= size()) {
                throw new IllegalArgumentException("Index is higher than set size");
            }

            return set.select(index);
        }

        public int rank(Key key) {
            return set.rank(key);
        }

        public void deleteMin() {
            set.deleteMin();
        }

        public void deleteMax() {
            set.deleteMax();
        }

        public Iterable<Key> keys() {
            return set.keys();
        }

        public Iterable<Key> keys(Key low, Key high) {
            return set.keys(low, high);
        }

        public int size(Key low, Key high) {
            return set.size(low, high);
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

    private class HashSet<Key> {

        private SeparateChainingHashTable<Key, Boolean> hashTable;

        HashSet() {
            hashTable = new SeparateChainingHashTable<>();
        }

        public boolean isEmpty() {
            return hashTable.isEmpty();
        }

        public int size() {
            return hashTable.size();
        }

        public boolean contains(Key key) {
            return hashTable.contains(key);
        }

        public void add(Key key) {
            hashTable.put(key, false);
        }

        public void delete(Key key) {
            hashTable.delete(key);
        }

        public Iterable<Key> keys() {
            return hashTable.keys();
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
        Exercise1 exercise1 = new Exercise1();
        exercise1.testSet();
        exercise1.testHashSet();
    }

    private void testSet() {
        StdOut.println("Set tests");
        Set<Integer> set = new Set<>();

        set.add(5);
        set.add(1);
        set.add(9);
        set.add(2);
        set.add(0);
        set.add(99);
        set.add(-1);
        set.add(-2);
        set.add(3);
        set.add(-5);

        StdOut.println("Keys() test");

        for(Integer key : set.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99");

        StdOut.println("\ntoString() test: " + set);

        //Test min()
        StdOut.println("\nMin key: " + set.min() + " Expected: -5");

        //Test max()
        StdOut.println("Max key: " + set.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + set.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + set.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + set.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + set.ceiling(15) + " Expected: 99");

        //Test select()
        StdOut.println("Select key of rank 4: " + set.select(4) + " Expected: 1");

        //Test rank()
        StdOut.println("Rank of key 9: " + set.rank(9) + " Expected: 8");
        StdOut.println("Rank of key 10: " + set.rank(10) + " Expected: 9");

        //Test delete()
        StdOut.println("\nDelete key 2");
        set.delete(2);

        for(Integer key : set.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMin()
        StdOut.println("\n\nDelete min (key -5)");
        set.deleteMin();

        for(Integer key : set.keys()) {
            StdOut.print(key + " ");
        }

        //Test deleteMax()
        StdOut.println("\n\nDelete max (key 99)");
        set.deleteMax();

        for(Integer key : set.keys()) {
            StdOut.print(key + " ");
        }

        //Test keys() with range
        StdOut.println("\n\nKeys in range [2, 10]");
        for(Integer key : set.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [-4, -1]");
        for(Integer key : set.keys(-4, -1)) {
            StdOut.print(key + " ");
        }

        //Delete all
        StdOut.println("\n\nDelete all");
        while (set.size() > 0) {
            for(Integer key : set.keys()) {
                StdOut.print(key + " ");
            }

            //set.delete(set.select(0));
            set.delete(set.select(set.size() - 1));
            StdOut.println();
        }
    }

    private void testHashSet() {
        StdOut.println("\nHashSet tests");
        HashSet<Integer> hashSet = new HashSet<>();

        hashSet.add(5);
        hashSet.add(1);
        hashSet.add(9);
        hashSet.add(2);
        hashSet.add(0);
        hashSet.add(99);
        hashSet.add(-1);
        hashSet.add(-2);
        hashSet.add(3);
        hashSet.add(-5);

        StdOut.println("Keys() test");

        for(Integer key : hashSet.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 -2 -1 0 1 2 3 5 9 99");

        StdOut.println("\ntoString() test: " + hashSet);

        //Test delete()
        StdOut.println("\nDelete key 2");
        hashSet.delete(2);

        for(Integer key : hashSet.keys()) {
            StdOut.print(key + " ");
        }

        StdOut.println("\nDelete key 99");
        hashSet.delete(99);

        for(Integer key : hashSet.keys()) {
            StdOut.print(key + " ");
        }

        StdOut.println("\nDelete key -5");
        hashSet.delete(-5);

        for(Integer key : hashSet.keys()) {
            StdOut.print(key + " ");
        }
    }

}
