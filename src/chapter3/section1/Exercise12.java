package chapter3.section1;

import chapter2.section2.TopDownMergeSort;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 23/04/17.
 */
@SuppressWarnings("unchecked")
public class Exercise12 {

    private class BinarySearchSymbolTable<Key extends Comparable<Key>, Value> {

        private class Item implements Comparable<Item> {
            private Key key;
            private Value value;

            Item(Key key, Value value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public int compareTo(Item that) {
                if (this.key.compareTo(that.key) < 0) {
                    return -1;
                } else if (this.key.compareTo(that.key) > 0) {
                    return 1;
                }

                return 0;
            }
        }

        private Item[] items;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchSymbolTable() {
            items = new BinarySearchSymbolTable.Item[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchSymbolTable(int capacity) {
            items = new BinarySearchSymbolTable.Item[capacity];
        }

        public BinarySearchSymbolTable(Item[] items) {
            TopDownMergeSort.mergeSort(items);
            this.items = items;

            size += items.length;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            int rank = rank(key);
            if (rank < size && items[rank].key.compareTo(key) == 0) {
                return items[rank].value;
            } else {
                return null;
            }
        }

        public int rank(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int comparison = key.compareTo(items[middle].key);
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

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            int rank = rank(key);

            if (rank < size && items[rank].key.compareTo(key) == 0) {
                items[rank].value = value;
                return;
            }

            if (size == items.length) {
                resize(items.length * 2);
            }

            for(int i = size; i > rank; i--) {
                items[i] = items[i - 1];
            }
            items[rank] = new Item(key, value);
            size++;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
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
                items[i] = items[i + 1];
            }

            items[size - 1] = null;
            size--;

            if (size > 1 && size == items.length / 4) {
                resize(items.length / 2);
            }
        }

        public Key min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return items[0].key;
        }

        public Key max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return items[size - 1].key;
        }

        public Key select(int k) {
            if (isEmpty() || k >= size) {
                throw new IllegalArgumentException("Invalid argument: " + k);
            }

            return items[k].key;
        }

        public Key ceiling(Key key) {
            int rank = rank(key);

            if (rank == size) {
                return null;
            }

            return items[rank].key;
        }

        public Key floor(Key key) {
            if (contains(key)) {
                return key;
            }

            int rank = rank(key);

            if (rank == 0) {
                return null;
            }

            return items[rank - 1].key;
        }

        public void deleteMin() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
            }

            delete(min());
        }

        public void deleteMax() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
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
                queue.enqueue(items[i].key);
            }

            if (contains(high)) {
                queue.enqueue(items[rank(high)].key);
            }

            return queue;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        private void resize(int newSize) {
            Item[] tempItems = new BinarySearchSymbolTable.Item[newSize];

            System.arraycopy(items, 0, tempItems, 0, size);

            items = tempItems;
        }
    }

    public static void main(String[] args) {
        Exercise12 exercise12 = new Exercise12();

        BinarySearchSymbolTable.Item[] items = new BinarySearchSymbolTable.Item[5];
        items[0] = exercise12.new BinarySearchSymbolTable().new Item(5, "Value 5");
        items[1] = exercise12.new BinarySearchSymbolTable().new Item(1, "Value 1");
        items[2] = exercise12.new BinarySearchSymbolTable().new Item(9, "Value 9");
        items[3] = exercise12.new BinarySearchSymbolTable().new Item(2, "Value 2");
        items[4] = exercise12.new BinarySearchSymbolTable().new Item(0, "Value 0");

        BinarySearchSymbolTable<Integer, String> binarySearchSymbolTable = exercise12.new BinarySearchSymbolTable<>(items);

        //Test put() and get() and keys()
        binarySearchSymbolTable.put(99, "Value 99");

        StdOut.println();

        for(Integer key : binarySearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchSymbolTable.get(key));
        }

        //Test delete()
        StdOut.println("\nDelete key 2");
        binarySearchSymbolTable.delete(2);
        for(Integer key : binarySearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchSymbolTable.get(key));
        }

        StdOut.println();

        //Test contains()
        StdOut.println("Contains key 98: " + binarySearchSymbolTable.contains(98) + " Expected: false");
        StdOut.println("Contains key 99: " + binarySearchSymbolTable.contains(99) + " Expected: true");

        //Test isEmpty()
        StdOut.println("Is empty: " + binarySearchSymbolTable.isEmpty() + " Expected: false");

        //Test size()
        StdOut.println("Size: " + binarySearchSymbolTable.size() + " Expected: 5");

        //Test min()
        StdOut.println("Min key: " + binarySearchSymbolTable.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchSymbolTable.max() + " Expected: 99");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchSymbolTable.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchSymbolTable.floor(15) + " Expected: 9");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchSymbolTable.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchSymbolTable.ceiling(15) + " Expected: 99");

        //Test rank()
        StdOut.println("Rank of key 9: " + binarySearchSymbolTable.rank(9) + " Expected: 3");
        StdOut.println("Rank of key 10: " + binarySearchSymbolTable.rank(10) + " Expected: 4");

        //Test select()
        StdOut.println("Select key of rank 4: " + binarySearchSymbolTable.select(4) + " Expected: 99");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");

        binarySearchSymbolTable.deleteMin();
        for(Integer key : binarySearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchSymbolTable.get(key));
        }

        //Test deleteMax()
        StdOut.println("\nDelete max (key 99)");

        binarySearchSymbolTable.deleteMax();
        for(Integer key : binarySearchSymbolTable.keys()) {
            StdOut.println("Key " + key + ": " + binarySearchSymbolTable.get(key));
        }

        StdOut.println();

        //Test size()
        StdOut.println("Size of keys between 2 and 10: " + binarySearchSymbolTable.size(2, 10) + " Expected: 2");

        //Test keys() with range
        StdOut.println();
        for(Integer key : binarySearchSymbolTable.keys(2, 10)) {
            StdOut.println("Key " + key + ": " + binarySearchSymbolTable.get(key));
        }

    }

}
