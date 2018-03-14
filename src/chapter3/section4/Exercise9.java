package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 20/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise9 {

    private class SeparateChainingHashTableWithDelete<Key, Value> extends SeparateChainingHashTable<Key, Value> {

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if (isEmpty()) {
                return;
            }

            if (!contains(key)) {
                return;
            }

            symbolTable[hash(key)].delete(key);
            keysSize--;

            if (getLoadFactor() <= averageListSize / (double) 4) {
                resize(size / 2);
            }
        }

    }

    public static void main(String[] args) {
        Exercise9 exercise9 = new Exercise9();
        SeparateChainingHashTableWithDelete<Integer, Integer> separateChainingHashTableWithDelete =
                exercise9.new SeparateChainingHashTableWithDelete<>();

        separateChainingHashTableWithDelete.put(1, 1);
        separateChainingHashTableWithDelete.put(2, 2);
        separateChainingHashTableWithDelete.put(3, 3);
        separateChainingHashTableWithDelete.put(4, 4);
        separateChainingHashTableWithDelete.put(5, 5);
        separateChainingHashTableWithDelete.put(6, 6);
        separateChainingHashTableWithDelete.put(7, 7);

        StdOut.println("Keys");
        for(Integer key : separateChainingHashTableWithDelete.keys()) {
            StdOut.print(key + " ");
        }

        int[] keysToDelete = {-1, 1, 2, 7, 6, 4, 5, 3};
        for(int k : keysToDelete) {
            StdOut.println("\nDelete key " + k);
            separateChainingHashTableWithDelete.delete(k);

            for(Integer key : separateChainingHashTableWithDelete.keys()) {
                StdOut.print(key + " ");
            }
            StdOut.println("\nSize: " + separateChainingHashTableWithDelete.size());
        }
    }

}
