package chapter3.section5;

import chapter1.section3.Exercise35_RandomQueue;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 19/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise29_SymbolTableWithRandomAccess {

    private class SymbolTableWithRandomAccess<Key, Value> {

        private SeparateChainingHashTable<Key, Value> separateChainingHashTable;
        private Exercise35_RandomQueue.RandomQueue<Key> randomQueue;

        SymbolTableWithRandomAccess() {
            separateChainingHashTable = new SeparateChainingHashTable<>();
            randomQueue = new Exercise35_RandomQueue().new RandomQueue<>();
        }

        public void insert(Key key, Value value) {
            separateChainingHashTable.put(key, value);
            randomQueue.enqueue(key);
        }

        public Value get(Key key) {
            return separateChainingHashTable.get(key);
        }

        public Key deleteRandomKey() {
            if (separateChainingHashTable.isEmpty()) {
                throw new RuntimeException("Empty symbol table");
            }

            Key randomKey = randomQueue.dequeue();
            separateChainingHashTable.delete(randomKey);

            return randomKey;
        }

    }

    public static void main(String[] args) {
        Exercise29_SymbolTableWithRandomAccess exercise29_symbolTableWithRandomAccess =
                new Exercise29_SymbolTableWithRandomAccess();
        SymbolTableWithRandomAccess<Integer, Integer> symbolTableWithRandomAccess =
                exercise29_symbolTableWithRandomAccess.new SymbolTableWithRandomAccess();

        symbolTableWithRandomAccess.insert(2, 2);
        symbolTableWithRandomAccess.insert(3, 3);
        symbolTableWithRandomAccess.insert(5, 5);
        symbolTableWithRandomAccess.insert(7, 7);
        symbolTableWithRandomAccess.insert(11, 11);
        symbolTableWithRandomAccess.insert(13, 13);
        symbolTableWithRandomAccess.insert(17, 17);

        StdOut.println("Get 7: " + symbolTableWithRandomAccess.get(7) + " Expected: 7");
        StdOut.println("Get 2: " + symbolTableWithRandomAccess.get(2) + " Expected: 2");
        StdOut.println("Get 17: " + symbolTableWithRandomAccess.get(17) + " Expected: 17");
        StdOut.println("Get 20: " + symbolTableWithRandomAccess.get(20) + " Expected: null");

        StdOut.println("\nRandom key deletes");
        for(int i = 0; i < 7; i++) {
            StdOut.println("Random key deleted: " + symbolTableWithRandomAccess.deleteRandomKey());
        }

        StdOut.println("Expected deleted values: 2 3 5 7 11 13 17 - Not necessarily in this order");
    }

}
