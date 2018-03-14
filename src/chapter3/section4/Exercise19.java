package chapter3.section4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Rene Argento on 17/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise19 {

    private class SeparateChainingHashTableWithKeys<Key, Value> extends SeparateChainingHashTable<Key, Value> {

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(SeparateChainingHashTable.SequentialSearchSymbolTable sequentialSearchST : symbolTable) {
                for(Object key : sequentialSearchST.keys()) {
                    keys.enqueue((Key) key);
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

    private class LinearProbingHashTableWithKeys<Key, Value> extends LinearProbingHashTable<Key, Value> {

        LinearProbingHashTableWithKeys(int size) {
            super(size);
        }

        public Iterable<Key> keys() {
            Queue<Key> keySet = new Queue<>();

            for(Object key : keys) {
                if (key != null) {
                    keySet.enqueue((Key) key);
                }
            }

            if (!keySet.isEmpty() && keySet.peek() instanceof Comparable) {
                Key[] keysToBeSorted = (Key[]) new Comparable[keySet.size()];
                for(int i = 0; i < keysToBeSorted.length; i++) {
                    keysToBeSorted[i] = keySet.dequeue();
                }

                Arrays.sort(keysToBeSorted);

                for(Key key : keysToBeSorted) {
                    keySet.enqueue(key);
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

        for(Integer key : separateChainingHashTableWithKeys.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 1 2 9 10 33 99");

        SeparateChainingHashTableWithKeys<Exercise19, Exercise19> separateChainingHashTableWithKeys2 =
                exercise19.new SeparateChainingHashTableWithKeys<>();

        Exercise19 nonComparableKey1 = new Exercise19();
        Exercise19 nonComparableKey2 = new Exercise19();
        separateChainingHashTableWithKeys2.put(nonComparableKey1, nonComparableKey1);
        separateChainingHashTableWithKeys2.put(nonComparableKey2, nonComparableKey2);

        for(Exercise19 key : separateChainingHashTableWithKeys2.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: chapter3.section4.Exercise19@XXX chapter3.section4.Exercise19@XXX");

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

        for(Integer key : linearProbingHashTableWithKeys.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: -5 1 2 9 10 33 99");

        LinearProbingHashTableWithKeys<Exercise19, Exercise19> linearProbingHashTableWithKeys2 =
                exercise19.new LinearProbingHashTableWithKeys<>(20);

        Exercise19 nonComparableKey3 = new Exercise19();
        Exercise19 nonComparableKey4 = new Exercise19();
        linearProbingHashTableWithKeys2.put(nonComparableKey3, nonComparableKey3);
        linearProbingHashTableWithKeys2.put(nonComparableKey4, nonComparableKey4);

        for(Exercise19 key : linearProbingHashTableWithKeys2.keys()) {
            StdOut.print(key + " ");
        }
        StdOut.println("\nExpected: chapter3.section4.Exercise19@XXX chapter3.section4.Exercise19@XXX");
    }

}
