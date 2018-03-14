package chapter3.section5;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * Created by Rene Argento on 02/08/17.
 */
@SuppressWarnings("unchecked")
public class HashSet<Key> {

    private class SeparateChainingHashTable<Key> {

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

            public void put(Key key) {
                for(Node node = first; node != null; node = node.next) {
                    if (key.equals(node.key)) {
                        node.key = key;
                        return;
                    }
                }

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

        private SeparateChainingHashTable() {
            this(DEFAULT_HASH_TABLE_SIZE, DEFAULT_AVERAGE_LIST_SIZE);
        }

        private SeparateChainingHashTable(int initialSize, int averageListSize) {
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
            return symbolTable[hash(key)].contains(key);
        }

        private void resize(int newSize) {
            SeparateChainingHashTable<Key> separateChainingHashTableTemp =
                    new SeparateChainingHashTable<>(newSize, averageListSize);

            for(Key key : keys()) {
                separateChainingHashTableTemp.put(key);
            }

            symbolTable = separateChainingHashTableTemp.symbolTable;
            size = separateChainingHashTableTemp.size;
            keysSize = separateChainingHashTableTemp.keysSize;
        }

        public void put(Key key) {
            int hashIndex = hash(key);
            int currentSize = symbolTable[hashIndex].size;
            symbolTable[hashIndex].put(key);

            if (currentSize < symbolTable[hashIndex].size) {
                keysSize++;
            }

            if (getLoadFactor() > averageListSize) {
                resize(size * 2);
                lgM++;
            }
        }

        public void delete(Key key) {
            if (isEmpty() || !contains(key)) {
                return;
            }

            symbolTable[hash(key)].delete(key);
            keysSize--;

            if (size > 1 && getLoadFactor() <= averageListSize / (double) 4) {
                resize(size / 2);
                lgM--;
            }
        }

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(SequentialSearchSymbolTable<Key> sequentialSearchST : symbolTable) {
                for(Key key : sequentialSearchST.keys()) {
                    keys.enqueue(key);
                }
            }

            if (!keys.isEmpty() && keys.peek() instanceof Comparable) {
                Key[] keysToBeSorted = (Key[]) new Comparable[keys.size()];
                for(int i = 0; i < keysToBeSorted.length; i++) {
                    keysToBeSorted[i] = keys.dequeue();
                }

                Arrays.sort(keysToBeSorted);

                for(Key key : keysToBeSorted) {
                    keys.enqueue(key);
                }
            }

            return keys;
        }

    }

    private SeparateChainingHashTable<Key> hashTable;

    public HashSet() {
        hashTable = new SeparateChainingHashTable<>();
    }

    public boolean isEmpty() {
        return hashTable.isEmpty();
    }

    public int size() {
        return hashTable.size();
    }

    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }

        return hashTable.contains(key);
    }

    public void add(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        hashTable.put(key);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Argument to delete() cannot be null");
        }

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
