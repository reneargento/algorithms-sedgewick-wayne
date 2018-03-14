package chapter3.section5;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * Created by Rene Argento on 07/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise18_Multisets {

    /**
     * API for MultiHashSET
     *
     * public class MultiHashSET<Key>
     *     MultiHashSET()                         create an empty set
     *     void add(Key key)                      add key into the set
     *     void delete(Key key)                   remove all keys equal to key from the set
     *     boolean contains(Key key)              is key in the set?
     *     boolean isEmpty()                      is the set empty?
     *     int size()                             number of keys in the set
     *     Iterable<Key> keys()                   all the keys in the set
     *     String toString()                      string representation of the set
     */

    private interface MultiHashSET<Key> {
        void add(Key key);
        void delete(Key key);
        boolean contains(Key key);
        boolean isEmpty();
        int size();
        Iterable<Key> keys();
        String toString();
    }

    /**
     * API for MultiSET
     *
     * public class MultiSET<Key>
     *     MultiSET()                             create an empty set
     *     void add(Key key)                      add key into the set
     *     void delete(Key key)                   remove all keys equal to key from the set
     *     boolean contains(Key key)              is key in the set?
     *     boolean isEmpty()                      is the set empty?
     *     int size()                             number of keys in the set
     *     Key min()                              smallest key
     *     Key max()                              largest key
     *     Key floor(Key key)                     largest key less than or equal to key
     *     Key ceiling(Key key)                   smallest key greater than or equal to key
     *     int rankFirst(Key key)                 number of keys less than key (or less than the first key, in case of duplicates)
     *     int rankLast(Key key)                  number of keys less than key (or less than the last key, in case of duplicates)
     *     Key select(int k)                      key of rank k
     *     void deleteMin()                       delete all keys equal to the smallest key
     *     void deleteMax()                       delete all keys equal to the largest key
     *     int size(Key low, Key high)            number of keys in [low..high] (includes all duplicates)
     *     Iterable<Key> keys(Key low, Key high)  keys in [low..high], in sorted order
     *     Iterable<Key> keys()                   all the keys in the set, in sorted order
     *     String toString()                      string representation of the set
     */

    private interface MultiSET<Key> {
        void add(Key key);
        void delete(Key key);
        boolean contains(Key key);
        boolean isEmpty();
        int size();
        Key min();
        Key max();
        Key floor(Key key);
        Key ceiling(Key key);
        int rankFirst(Key key);
        int rankLast(Key key);
        Key select(int k);
        void deleteMin();
        void deleteMax();
        int size(Key low, Key high);
        Iterable<Key> keys(Key low, Key high);
        Iterable<Key> keys();
        String toString();
    }

    public class SeparateChainingMultiSET<Key> implements MultiHashSET<Key> {

        private class SequentialSearchSymbolTable<Key> {

            private class Node {
                Key key;
                Node next;

                public Node(Key key, Node next) {
                    this.key = key;
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
                for(Node node = first; node != null; node = node.next) {
                    if (key.equals(node.key)) {
                        return true;
                    }
                }

                return false;
            }

            public void add(Key key) {
                first = new Node(key, first);
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
        private SequentialSearchSymbolTable<Key>[] symbolTable;

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

        public SeparateChainingMultiSET() {
            this(DEFAULT_HASH_TABLE_SIZE, DEFAULT_AVERAGE_LIST_SIZE);
        }

        public SeparateChainingMultiSET(int initialSize, int averageListSize) {
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

            return symbolTable[hash(key)].contains(key);
        }

        private void resize(int newSize) {
            SeparateChainingMultiSET<Key> separateChainingMultiSET = new SeparateChainingMultiSET<>(newSize, averageListSize);

            for(Key key : keys()) {
                separateChainingMultiSET.add(key);
            }

            symbolTable = separateChainingMultiSET.symbolTable;
            size = separateChainingMultiSET.size;
        }

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            int hashIndex = hash(key);
            symbolTable[hashIndex].add(key);
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

            while (contains(key)) {
                symbolTable[hash(key)].delete(key);
                keysSize--;

                if (size > 1 && getLoadFactor() <= averageListSize / (double) 4) {
                    resize(size / 2);
                    lgM--;
                }
            }
        }

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(SequentialSearchSymbolTable<Key> sequentialSearchST : symbolTable) {
                for(Key key : sequentialSearchST.keys()) {
                    keys.enqueue(key);
                }
            }

            return keys;
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

    public class BinarySearchMultiSET<Key extends Comparable<Key>> implements MultiSET<Key> {

        private Key[] keys;
        private int size;

        private static final int DEFAULT_INITIAL_CAPACITY = 2;

        public BinarySearchMultiSET() {
            keys = (Key[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
        }

        public BinarySearchMultiSET(int capacity) {
            keys = (Key[]) new Comparable[capacity];
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

            if (isEmpty()) {
                return false;
            }

            int rank = rankLast(key);
            if (rank < size && keys[rank].compareTo(key) == 0) {
                return true;
            } else {
                return false;
            }
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

        public void add(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (size == keys.length) {
                resize(keys.length * 2);
            }

            int rank = rankLast(key);

            for(int i = size; i > rank; i--) {
                keys[i] = keys[i - 1];
            }
            keys[rank] = key;
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
                }

                keys[size - 1] = null;
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
                throw new NoSuchElementException("Multiset is empty");
            }

            Key minKey = min();

            while (contains(minKey)) {
                delete(minKey);
            }
        }

        public void deleteMax() {
            if (isEmpty()) {
                throw new NoSuchElementException("Multiset is empty");
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

            for(int i = rankFirst(low); i < rankLast(high); i++) {
                queue.enqueue(keys[i]);
            }

            if (contains(high)) {
                queue.enqueue(keys[rankLast(high)]);
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
        Exercise18_Multisets multisets = new Exercise18_Multisets();
        multisets.testSeparateChainingMultiSET();
        multisets.testBinarySearchMultiSET();
    }

    private void testSeparateChainingMultiSET() {
        StdOut.println("*********** SeparateChainingMultiSET tests ***********");
        MultiHashSET<Integer> separateChainingMultiSET = new SeparateChainingMultiSET<>();

        //Test isEmpty()
        StdOut.println("\nIsEmpty: " + separateChainingMultiSET.isEmpty() + " Expected: true");

        //Test add()
        separateChainingMultiSET.add(0);
        separateChainingMultiSET.add(0);
        separateChainingMultiSET.add(0);
        separateChainingMultiSET.add(0);

        separateChainingMultiSET.add(5);
        separateChainingMultiSET.add(5);

        separateChainingMultiSET.add(8);
        separateChainingMultiSET.add(8);

        separateChainingMultiSET.add(20);
        separateChainingMultiSET.add(20);
        separateChainingMultiSET.add(20);
        separateChainingMultiSET.add(20);

        separateChainingMultiSET.add(21);
        separateChainingMultiSET.add(22);
        separateChainingMultiSET.add(23);
        separateChainingMultiSET.add(24);

        //Test keys()
        StdOut.println("\nKeys() test");

        for(Integer key : separateChainingMultiSET.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: 0 0 0 0 5 5 8 8 20 20 20 20 21 22 23 24 - Not necessarily in this order");

        //Test size()
        StdOut.println("\nKeys size: " + separateChainingMultiSET.size() + " Expected: 16");

        StdOut.println("\ntoString() test: " + separateChainingMultiSET);

        //Test contains()
        StdOut.println("\nContains 0: " + separateChainingMultiSET.contains(0) + " Expected: true");
        StdOut.println("Contains 100: " + separateChainingMultiSET.contains(100) + " Expected: false");

        //Test delete()
        StdOut.println("\nDelete key 5");
        separateChainingMultiSET.delete(5);
        StdOut.println(separateChainingMultiSET);

        StdOut.println("\nDelete key 24");
        separateChainingMultiSET.delete(24);
        StdOut.println(separateChainingMultiSET);

        StdOut.println("\nDelete key 0");
        separateChainingMultiSET.delete(0);
        StdOut.println(separateChainingMultiSET);

        StdOut.println("\nKeys size: " + separateChainingMultiSET.size() + " Expected: 9");
        StdOut.println("\nIsEmpty: " + separateChainingMultiSET.isEmpty() + " Expected: false");
    }

    private void testBinarySearchMultiSET() {
        StdOut.println("\n\n*********** BinarySearchMultiSET tests ***********");
        MultiSET<Integer> binarySearchMultiSET = new BinarySearchMultiSET<>();

        //Test isEmpty()
        StdOut.println("\nIsEmpty: " + binarySearchMultiSET.isEmpty() + " Expected: true");

        //Test add()
        binarySearchMultiSET.add(0);
        binarySearchMultiSET.add(0);
        binarySearchMultiSET.add(0);
        binarySearchMultiSET.add(0);

        binarySearchMultiSET.add(5);
        binarySearchMultiSET.add(5);

        binarySearchMultiSET.add(8);
        binarySearchMultiSET.add(8);

        binarySearchMultiSET.add(20);
        binarySearchMultiSET.add(20);
        binarySearchMultiSET.add(20);
        binarySearchMultiSET.add(20);

        binarySearchMultiSET.add(21);
        binarySearchMultiSET.add(22);
        binarySearchMultiSET.add(23);
        binarySearchMultiSET.add(24);

        //Test keys()
        StdOut.println("\nKeys() test");
        for(Integer key : binarySearchMultiSET.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: 0 0 0 0 5 5 8 8 20 20 20 20 21 22 23 24");

        //Test size()
        StdOut.println("\nKeys size: " + binarySearchMultiSET.size() + " Expected: 16");

        //Test size() with range
        StdOut.println("Keys size [0, 20]: " + binarySearchMultiSET.size(0, 20) + " Expected: 12");

        //Test contains()
        StdOut.println("\nContains 8: " + binarySearchMultiSET.contains(8) + " Expected: true");
        StdOut.println("Contains 9: " + binarySearchMultiSET.contains(9) + " Expected: false");

        //Test min()
        StdOut.println("\nMin key: " + binarySearchMultiSET.min() + " Expected: 0");

        //Test max()
        StdOut.println("Max key: " + binarySearchMultiSET.max() + " Expected: 24");

        //Test floor()
        StdOut.println("Floor of 5: " + binarySearchMultiSET.floor(5) + " Expected: 5");
        StdOut.println("Floor of 15: " + binarySearchMultiSET.floor(15) + " Expected: 8");

        //Test ceiling()
        StdOut.println("Ceiling of 5: " + binarySearchMultiSET.ceiling(5) + " Expected: 5");
        StdOut.println("Ceiling of 15: " + binarySearchMultiSET.ceiling(15) + " Expected: 20");

        //Test select()
        StdOut.println("Select key of rank 3: " + binarySearchMultiSET.select(3) + " Expected: 0");
        StdOut.println("Select key of rank 4: " + binarySearchMultiSET.select(4) + " Expected: 5");

        //Test rank()
        StdOut.println("RankFirst of key 8: " + binarySearchMultiSET.rankFirst(8) + " Expected: 6");
        StdOut.println("RankFirst of key 9: " + binarySearchMultiSET.rankFirst(9) + " Expected: 8");
        StdOut.println("RankLast of key 9: " + binarySearchMultiSET.rankLast(9) + " Expected: 8");
        StdOut.println("RankFirst of key 20: " + binarySearchMultiSET.rankFirst(20) + " Expected: 8");
        StdOut.println("RankLast of key 20: " + binarySearchMultiSET.rankLast(20) + " Expected: 11");

        //Test delete()
        StdOut.println("\nDelete key 20");
        binarySearchMultiSET.delete(20);

        //Test toString()
        StdOut.println(binarySearchMultiSET);
        StdOut.println("\nKeys size: " + binarySearchMultiSET.size() + " Expected: 12");

        StdOut.println("\nDelete key 5");
        binarySearchMultiSET.delete(5);

        StdOut.println(binarySearchMultiSET);
        StdOut.println("\nKeys size: " + binarySearchMultiSET.size() + " Expected: 10");

        //Test deleteMin()
        StdOut.println("\nDelete min (key 0)");
        binarySearchMultiSET.deleteMin();

        StdOut.println(binarySearchMultiSET);
        StdOut.println("\nKeys size: " + binarySearchMultiSET.size() + " Expected: 6");

        //Test deleteMax()
        StdOut.println("\nDelete max (key 24)");
        binarySearchMultiSET.deleteMax();

        StdOut.println(binarySearchMultiSET);
        StdOut.println("\nKeys size: " + binarySearchMultiSET.size() + " Expected: 5");

        //Test keys() with range
        StdOut.println("\nKeys in range [2, 10]");
        for(Integer key : binarySearchMultiSET.keys(2, 10)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nKeys in range [20, 22]");
        for(Integer key : binarySearchMultiSET.keys(20, 22)) {
            StdOut.print(key + " ");
        }

        StdOut.println("\n\nIsEmpty: " + binarySearchMultiSET.isEmpty() + " Expected: false");

        //Delete all
        StdOut.println("\nDelete all");
        while (binarySearchMultiSET.size() > 0) {
            StdOut.println(binarySearchMultiSET);
            //binarySearchMultiSET.delete(binarySearchMultiSET.select(0));
            binarySearchMultiSET.delete(binarySearchMultiSET.select(binarySearchMultiSET.size() - 1));
        }
        StdOut.println(binarySearchMultiSET);
    }

}
