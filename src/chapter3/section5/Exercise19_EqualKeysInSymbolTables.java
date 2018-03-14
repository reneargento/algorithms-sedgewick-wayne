package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 07/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise19_EqualKeysInSymbolTables {

    public class SeparateChainingMultiST<Key, Value> {

        private class SequentialSearchSymbolTable<Key, Value> {

            private class Node {
                Key key;
                Value value;
                Node next;

                public Node(Key key, Value value, Node next) {
                    this.key = key;
                    this.value = value;
                    this.next = next;
                }
            }

            private Node first;
            private int size;

            public int size() {
                return size;
            }

            public boolean isEmpty() {
                return size == 0;
            }

            public boolean contains(Key key) {
                return get(key) != null;
            }

            public Value get(Key key) {
                for(Node node = first; node != null; node = node.next) {
                    if (key.equals(node.key)) {
                        return node.value;
                    }
                }

                return null;
            }

            public Iterable<Value> getAll(Key key) {
                Queue<Value> values = new Queue<>();

                for(Node node = first; node != null; node = node.next) {
                    if (node.key.equals(key)) {
                        values.enqueue(node.value);
                    }
                }

                return values;
            }

            public void put(Key key, Value value) {
                first = new Node(key, value, first);
                size++;
            }

            public void delete(Key key) {
                if (first.key.equals(key)) {
                    first = first.next;
                    size--;
                    return;
                }

                for(Node node = first; node != null; node = node.next) {
                    if (node.next != null && node.next.key.equals(key)) {
                        node.next = node.next.next;
                        size--;
                        return;
                    }
                }
            }

            public Iterable<Key> keys() {
                Queue<Key> keys = new Queue<>();

                for(Node node = first; node != null; node = node.next) {
                    keys.enqueue(node.key);
                }

                return keys;
            }

        }

        private int averageListSize;

        private int size;
        private int keysSize;
        private SequentialSearchSymbolTable<Key, Value>[] symbolTable;

        private static final int DEFAULT_HASH_TABLE_SIZE = 997;
        private static final int DEFAULT_AVERAGE_LIST_SIZE = 5;

        //The largest prime <= 2^i for i = 1 to 31
        //Used to distribute keys uniformly in the hash table after resizes
        //PRIMES[n] = 2^k - Ak where k is the power of 2 and Ak is the value to subtract to reach the previous prime number
        private final int[] PRIMES = {
                1, 1, 3, 7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
                32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
                8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
                536870909, 1073741789, 2147483647
        };

        //The lg of the hash table size
        //Used in combination with PRIMES[] to distribute keys uniformly in the hash function after resizes
        private int lgM;

        public SeparateChainingMultiST() {
            this(DEFAULT_HASH_TABLE_SIZE, DEFAULT_AVERAGE_LIST_SIZE);
        }

        public SeparateChainingMultiST(int initialSize, int averageListSize) {
            this.size = initialSize;
            this.averageListSize = averageListSize;
            symbolTable = new SequentialSearchSymbolTable[size];

            for(int i = 0; i < size; i++) {
                symbolTable[i] = new SequentialSearchSymbolTable<>();
            }

            lgM = (int) (Math.log(size) / Math.log(2));
        }

        public int size() {
            return keysSize;
        }

        public boolean isEmpty() {
            return keysSize == 0;
        }

        private int hash(Key key) {
            int hash = key.hashCode() & 0x7fffffff;

            if (lgM < 26) {
                hash = hash % PRIMES[lgM + 5];
            }

            return hash % size;
        }

        private double getLoadFactor() {
            return ((double) keysSize) / (double) size;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }

            return get(key) != null;
        }

        private void resize(int newSize) {
            SeparateChainingMultiST<Key, Value> separateChainingMultiSTTemp =
                    new SeparateChainingMultiST<>(newSize, averageListSize);

            for(Key key : keys()) {
                separateChainingMultiSTTemp.put(key, get(key));
            }

            symbolTable = separateChainingMultiSTTemp.symbolTable;
            size = separateChainingMultiSTTemp.size;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            return symbolTable[hash(key)].get(key);
        }

        public Iterable<Value> getAll(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to getAll() cannot be null");
            }

            if (!contains(key)) {
                return new Queue<>();
            }

            int hashIndex = hash(key);
            return symbolTable[hashIndex].getAll(key);
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            int hashIndex = hash(key);
            symbolTable[hashIndex].put(key, value);
            keysSize++;

            if (getLoadFactor() > averageListSize) {
                resize(size * 2);
                lgM++;
            }
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            int hashIndex = hash(key);
            while (symbolTable[hashIndex].contains(key)) {
                symbolTable[hashIndex].delete(key);
                keysSize--;

                if (size > 1 && getLoadFactor() <= averageListSize / (double) 4) {
                    resize(size / 2);
                    lgM--;
                }
            }
        }

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(SequentialSearchSymbolTable<Key, Value> sequentialSearchST : symbolTable) {
                for(Key key : sequentialSearchST.keys()) {
                    keys.enqueue(key);
                }
            }

            return keys;
        }
    }

    public class BinarySearchMultiST<Key extends Comparable<Key>, Value> {

        private Key[] keys;
        private Value[] values;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchMultiST() {
            keys = (Key[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
            values = (Value[]) new Object[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchMultiST(int capacity) {
            keys = (Key[]) new Comparable[capacity];
            values = (Value[]) new Object[capacity];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }
            return get(key) != null;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            if (isEmpty()) {
                return null;
            }

            int rank = rankLast(key);
            if (rank < size && keys[rank].compareTo(key) == 0) {
                return values[rank];
            } else {
                return null;
            }
        }

        public Iterable<Value> getAll(Key key) {
            Queue<Value> values = new Queue<>();

            if (!contains(key)) {
                return values;
            }

            int rankFirst = rankFirst(key);
            int rankLast = rankLast(key);

            for(int index = rankFirst; index < rankLast; index++) {
                values.enqueue(this.values[index]);
            }

            if (rankLast < size && keys[rankLast].equals(key)) {
                values.enqueue(this.values[rankLast]);
            }

            return values;
        }

        public int rankFirst(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int low = 0;
            int high = size - 1;

            int rankFound = -1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int comparison = key.compareTo(keys[middle]);
                if (comparison < 0) {
                    high = middle - 1;
                } else if (comparison > 0) {
                    low = middle + 1;
                } else {
                    rankFound = middle;
                    high = middle - 1;
                }
            }

            if (rankFound != -1) {
                return rankFound;
            } else {
                return low;
            }
        }

        public int rankLast(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int low = 0;
            int high = size - 1;

            int rankFound = -1;

            while (low <= high) {
                int middle = low + (high - low) / 2;

                int comparison = key.compareTo(keys[middle]);
                if (comparison < 0) {
                    high = middle - 1;
                } else if (comparison > 0) {
                    low = middle + 1;
                } else {
                    rankFound = middle;
                    low = middle + 1;
                }
            }

            if (rankFound != -1) {
                return rankFound;
            } else {
                return low;
            }
        }

        //In the case of duplicates, return the rank of the rightmost key
        public int rank(Key key) {
            return rankLast(key);
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            if (size == keys.length) {
                resize(keys.length * 2);
            }

            int rank = rankLast(key);

            for(int i = size; i > rank; i--) {
                keys[i] = keys[i - 1];
                values[i] = values[i - 1];
            }
            keys[rank] = key;
            values[rank] = value;
            size++;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty() || !contains(key)) {
                return;
            }

            while (contains(key)) {
                int rank = rankLast(key);
                for(int i = rank; i < size - 1; i++) {
                    keys[i] = keys[i + 1];
                    values[i] = values[i + 1];
                }

                keys[size - 1] = null;
                values[size - 1] = null;
                size--;

                if (size > 1 && size == keys.length / 4) {
                    resize(keys.length / 2);
                }
            }
        }

        public Key min() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[0];
        }

        public Key max() {
            if (isEmpty()) {
                throw new NoSuchElementException("Empty symbol table");
            }

            return keys[size - 1];
        }

        public Key select(int k) {
            if (isEmpty() || k >= size) {
                throw new IllegalArgumentException("Index " + k + " is higher than size");
            }

            return keys[k];
        }

        public Key ceiling(Key key) {
            int rank = rankLast(key);

            if (rank == size) {
                return null;
            }

            return keys[rank];
        }

        public Key floor(Key key) {
            if (contains(key)) {
                return key;
            }

            int rank = rankLast(key);

            if (rank == 0) {
                return null;
            }

            return keys[rank - 1];
        }

        public void deleteMin() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
            }

            Key minKey = min();

            while (contains(minKey)) {
                delete(minKey);
            }
        }

        public void deleteMax() {
            if (isEmpty()) {
                throw new NoSuchElementException("Symbol table underflow error");
            }

            Key maxKey = max();

            while (contains(maxKey)) {
                delete(maxKey);
            }
        }

        public int size(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (high.compareTo(low) < 0) {
                return 0;
            } else if (contains(high)) {
                return rankLast(high) - rankFirst(low) + 1;
            } else {
                return rankLast(high) - rankFirst(low);
            }
        }

        public Iterable<Key> keys(Key low, Key high) {
            if (low == null || high == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            Queue<Key> queue = new Queue<>();

            int rankFirstLow = rankFirst(low);
            int rankLastHigh = rankLast(high);

            for(int i = rankFirstLow; i < rankLastHigh; i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rankLastHigh]);
            }

            return queue;
        }

        public Iterable<Key> keys() {
            return keys(min(), max());
        }

        private void resize(int newSize) {
            Key[] tempKeys = (Key[]) new Comparable[newSize];
            Value[] tempValues = (Value[]) new Object[newSize];

            System.arraycopy(keys, 0, tempKeys, 0, size);
            System.arraycopy(values, 0, tempValues, 0, size);

            keys = tempKeys;
            values = tempValues;
        }
    }

    public static void main(String[] args) {
        Exercise19_EqualKeysInSymbolTables equalKeysInSymbolTables = new Exercise19_EqualKeysInSymbolTables();
        equalKeysInSymbolTables.testSeparateChainingMultiST();
        equalKeysInSymbolTables.testBinarySearchMultiST();
    }

    private void testSeparateChainingMultiST() {
        StdOut.println("*********** SeparateChainingMultiST tests ***********");
        SeparateChainingMultiST<Integer, Integer> separateChainingMultiST = new SeparateChainingMultiST<>();

        //Test isEmpty()
        StdOut.println("\nIsEmpty: " + separateChainingMultiST.isEmpty() + " Expected: true");

        //Test put()
        separateChainingMultiST.put(0, 0);
        separateChainingMultiST.put(0, 1);
        separateChainingMultiST.put(0, 2);
        separateChainingMultiST.put(0, 2);

        separateChainingMultiST.put(5, 5);
        separateChainingMultiST.put(5, 6);

        separateChainingMultiST.put(8, 8);
        separateChainingMultiST.put(8, 9);

        separateChainingMultiST.put(20, 20);
        separateChainingMultiST.put(20, 0);
        separateChainingMultiST.put(20, 1);
        separateChainingMultiST.put(20, 2);

        separateChainingMultiST.put(21, 21);
        separateChainingMultiST.put(22, 22);
        separateChainingMultiST.put(23, 23);
        separateChainingMultiST.put(24, 100);

        //Test keys()
        StdOut.println("\nKeys() test");

        for(Integer key : separateChainingMultiST.keys()) {
            StdOut.println(key + " " + separateChainingMultiST.get(key));
        }
        StdOut.println("\nExpected: (Not necessarily in this order)");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("5 { 5 or 6 }");
        StdOut.println("5 { 5 or 6 }");
        StdOut.println("8 { 8 or 9 }");
        StdOut.println("8 { 8 or 9 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("21 21");
        StdOut.println("22 22");
        StdOut.println("23 23");
        StdOut.println("24 100");

        //Test getAll()
        StdOut.println("\nGet all values of key 0");
        for(Integer value : separateChainingMultiST.getAll(0)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 0 1 2 2 - Not necessarily in this order");

        StdOut.println("\nGet all values of key 100");
        for(Integer value : separateChainingMultiST.getAll(100)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: ");

        StdOut.println("\nGet all values of key 20");
        for(Integer value : separateChainingMultiST.getAll(20)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 0 1 2 20 - Not necessarily in this order");

        StdOut.println("\nGet all values of key 22");
        for(Integer value : separateChainingMultiST.getAll(22)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 22");

        //Test size()
        StdOut.println("\nKeys size: " + separateChainingMultiST.size() + " Expected: 16");

        //Test contains()
        StdOut.println("\nContains 0: " + separateChainingMultiST.contains(0) + " Expected: true");
        StdOut.println("Contains 100: " + separateChainingMultiST.contains(100) + " Expected: false");

        //Test delete()
        StdOut.println("\nDelete key 5");
        separateChainingMultiST.delete(5);
        for(Integer key : separateChainingMultiST.keys()) {
            StdOut.println(key + " " + separateChainingMultiST.get(key));
        }

        StdOut.println("\nDelete key 24");
        separateChainingMultiST.delete(24);
        for(Integer key : separateChainingMultiST.keys()) {
            StdOut.println(key + " " + separateChainingMultiST.get(key));
        }

        StdOut.println("\nDelete key 0");
        separateChainingMultiST.delete(0);
        for(Integer key : separateChainingMultiST.keys()) {
            StdOut.println(key + " " + separateChainingMultiST.get(key));
        }

        StdOut.println("\nKeys size: " + separateChainingMultiST.size() + " Expected: 9");
        StdOut.println("\nIsEmpty: " + separateChainingMultiST.isEmpty() + " Expected: false");
    }

    private void testBinarySearchMultiST() {
        StdOut.println("\n*********** BinarySearchMultiST tests ***********");
        BinarySearchMultiST<Integer, Integer> binarySearchMultiST = new BinarySearchMultiST<>();

        //Test isEmpty()
        StdOut.println("\nIsEmpty: " + binarySearchMultiST.isEmpty() + " Expected: true");

        //Test put()
        binarySearchMultiST.put(0, 0);
        binarySearchMultiST.put(0, 1);
        binarySearchMultiST.put(0, 2);
        binarySearchMultiST.put(0, 2);

        binarySearchMultiST.put(5, 5);
        binarySearchMultiST.put(5, 6);

        binarySearchMultiST.put(8, 8);
        binarySearchMultiST.put(8, 9);

        binarySearchMultiST.put(20, 20);
        binarySearchMultiST.put(20, 0);
        binarySearchMultiST.put(20, 1);
        binarySearchMultiST.put(20, 2);

        binarySearchMultiST.put(21, 21);
        binarySearchMultiST.put(22, 22);
        binarySearchMultiST.put(23, 23);
        binarySearchMultiST.put(24, 100);

        //Test keys()
        StdOut.println("\nKeys() test");
        for(Integer key : binarySearchMultiST.keys()) {
            StdOut.println(key + " " + binarySearchMultiST.get(key));
        }
        StdOut.println("\nExpected:");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("0 { 0, 1 or 2 }");
        StdOut.println("5 { 5 or 6 }");
        StdOut.println("5 { 5 or 6 }");
        StdOut.println("8 { 8 or 9 }");
        StdOut.println("8 { 8 or 9 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("20 { 0, 1, 2 or 20 }");
        StdOut.println("21 21");
        StdOut.println("22 22");
        StdOut.println("23 23");
        StdOut.println("24 100");

        //Test getAll()
        StdOut.println("\nGet all values of key 0");
        for(Integer value : binarySearchMultiST.getAll(0)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 0 1 2 2 - Not necessarily in this order");

        StdOut.println("\nGet all values of key 100");
        for(Integer value : binarySearchMultiST.getAll(100)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: ");

        StdOut.println("\nGet all values of key 20");
        for(Integer value : binarySearchMultiST.getAll(20)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 0 1 2 20 - Not necessarily in this order");

        StdOut.println("\nGet all values of key 22");
        for(Integer value : binarySearchMultiST.getAll(22)) {
            StdOut.print(value + " ");
        }
        StdOut.println("\nExpected: 22");

        //Test size()
        StdOut.println("\nKeys size: " + binarySearchMultiST.size() + " Expected: 16");

        //Test size() with range
        StdOut.println("Keys size [0, 20]: " + binarySearchMultiST.size(0, 20) + " Expected: 12");

        //Test contains()
        StdOut.println("\nContains 8: " + binarySearchMultiST.contains(8) + " Expected: true");
        StdOut.println("Contains 9: " + binarySearchMultiST.contains(9) + " Expected: false");

        //Test min()
        StdOut.println("\nMin key: " + binarySearchMultiST.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchMultiST.max() + " Expected: 24");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchMultiST.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchMultiST.floor(15) + " Expected: 8");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchMultiST.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchMultiST.ceiling(15) + " Expected: 20");

        //Test select()
        StdOut.println("Select key of rank 3: " + binarySearchMultiST.select(3) + " Expected: 0");
        StdOut.println("Select key of rank 4: " + binarySearchMultiST.select(4) + " Expected: 5");

        //Test rank()
        StdOut.println("RankFirst of key 8: " + binarySearchMultiST.rankFirst(8) + " Expected: 6");
        StdOut.println("RankFirst of key 9: " + binarySearchMultiST.rankFirst(9) + " Expected: 8");
        StdOut.println("RankLast of key 9: " + binarySearchMultiST.rankLast(9) + " Expected: 8");
        StdOut.println("RankFirst of key 20: " + binarySearchMultiST.rankFirst(20) + " Expected: 8");
        StdOut.println("RankLast of key 20: " + binarySearchMultiST.rankLast(20) + " Expected: 11");

        //Test delete()
        StdOut.println("\nDelete key 20");
        binarySearchMultiST.delete(20);

        for(Integer key : binarySearchMultiST.keys()) {
            StdOut.println(key + " " + binarySearchMultiST.get(key));
        }
        StdOut.println("\nKeys size: " + binarySearchMultiST.size() + " Expected: 12");

        StdOut.println("\nDelete key 5");
        binarySearchMultiST.delete(5);

        for(Integer key : binarySearchMultiST.keys()) {
            StdOut.println(key + " " + binarySearchMultiST.get(key));
        }
        StdOut.println("\nKeys size: " + binarySearchMultiST.size() + " Expected: 10");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");
        binarySearchMultiST.deleteMin();

        for(Integer key : binarySearchMultiST.keys()) {
            StdOut.println(key + " " + binarySearchMultiST.get(key));
        }
        StdOut.println("\nKeys size: " + binarySearchMultiST.size() + " Expected: 6");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 24)");
        binarySearchMultiST.deleteMax();

        for(Integer key : binarySearchMultiST.keys()) {
            StdOut.println(key + " " + binarySearchMultiST.get(key));
        }
        StdOut.println("\nKeys size: " + binarySearchMultiST.size() + " Expected: 5");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : binarySearchMultiST.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [20, 22]");
        for(Integer key : binarySearchMultiST.keys(20, 22)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nIsEmpty: " + binarySearchMultiST.isEmpty() + " Expected: false");

        //Delete all
        StdOut.println("\nDelete all");
        while (binarySearchMultiST.size() > 0) {
            for(Integer key : binarySearchMultiST.keys()) {
                StdOut.println(key + " " + binarySearchMultiST.get(key));
            }
            //binarySearchMultiST.delete(binarySearchMultiST.select(0));
            binarySearchMultiST.delete(binarySearchMultiST.select(binarySearchMultiST.size() - 1));
            StdOut.println();
        }
    }

}
