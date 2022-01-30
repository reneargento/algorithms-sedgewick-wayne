package chapter3.section4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/07/17.
 */
// Thanks to faame (https://github.com/faame) for suggesting an improvement on the keys() method design.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/235
@SuppressWarnings("unchecked")
public class Exercise19 {

    private class SeparateChainingHashTableWithKeys<Key, Value> extends SeparateChainingHashTable<Key, Value> {

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for (SeparateChainingHashTable.SequentialSearchSymbolTable sequentialSearchST : symbolTable) {
                for (Object key : sequentialSearchST.keys()) {
                    keys.enqueue((Key) key);
                }
            }
            return keys;
        }
    }

    private class LinearProbingHashTableWithKeys<Key, Value> extends LinearProbingHashTable<Key, Value> {
        LinearProbingHashTableWithKeys(int size) {
            super(size);
        }

        public Iterable<Key> keys() {
            Queue<Key> keySet = new Queue<>();

            for (Object key : keys) {
                if (key != null) {
                    keySet.enqueue((Key) key);
                }
            }
            return keySet;
        }
    }

    public static void main(String[] args) {
        Exercise19 exercise19 = new Exercise19();

        StdOut.println("Separate Chaining tests");
        SeparateChainingHashTableWithKeys<Integer, Integer> separateChainingHashTableWithKeys =
                exercise19.new SeparateChainingHashTableWithKeys<>();

        separateChainingHashTableWithKeys.put(10, 10);
        separateChainingHashTableWithKeys.put(99, 99);
        separateChainingHashTableWithKeys.put(-5, -5);
        separateChainingHashTableWithKeys.put(33, 33);
        separateChainingHashTableWithKeys.put(2, 2);
        separateChainingHashTableWithKeys.put(1, 1);
        separateChainingHashTableWithKeys.put(9, 9);

        for (Integer key : separateChainingHashTableWithKeys.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected (there is no guarantee on the order): 1 2 9 10 33 -5 99");

        StdOut.println("\nLinear Probing tests");
        LinearProbingHashTableWithKeys<Integer, Integer> linearProbingHashTableWithKeys =
                exercise19.new LinearProbingHashTableWithKeys<>(20);

        linearProbingHashTableWithKeys.put(10, 10);
        linearProbingHashTableWithKeys.put(99, 99);
        linearProbingHashTableWithKeys.put(-5, -5);
        linearProbingHashTableWithKeys.put(33, 33);
        linearProbingHashTableWithKeys.put(2, 2);
        linearProbingHashTableWithKeys.put(1, 1);
        linearProbingHashTableWithKeys.put(9, 9);

        for (Integer key : linearProbingHashTableWithKeys.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected (there is no guarantee on the order): 1 2 -5 9 10 33 99");
    }
}
