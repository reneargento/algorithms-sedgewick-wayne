package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 24/04/17.
 */
@SuppressWarnings("unchecked")
public class Exercise22_SelfOrganizingSearch {

    public class ArraySTSelfOrganizing<Key, Value> {

        public Key[] keys;
        public Value[] values;
        private int size;

        public ArraySTSelfOrganizing(int capacity) {
            keys = (Key[]) new Object[capacity];
            values = (Value[]) new Object[capacity];
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if (value == null) {
                delete(key);
                return;
            }

            for(int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    values[i] = value;
                    return;
                }
            }

            if (size == keys.length) {
                resize(keys.length * 2);
            }

            keys[size] = key;
            values[size] = value;
            size++;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            for(int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    if (i == 0) {
                        return values[i];
                    }

                    //Move-to-front heuristic
                    Key tempKey = keys[i];
                    Value tempValue = values[i];

                    for(int j = i; j > 0; j--) {
                        keys[j] = keys[j - 1];
                        values[j] = values[j - 1];
                    }

                    keys[0] = tempKey;
                    values[0] = tempValue;

                    return values[0];
                }
            }

            return null;
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            for(int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    keys[i] = keys[size - 1];
                    values[i] = values[size - 1];

                    keys[size - 1] = null;
                    values[size - 1] = null;

                    size--;

                    break;
                }
            }

            if (size > 1 && size == keys.length / 4) {
                resize(keys.length / 2);
            }
        }

        public boolean contains(Key key) {
            for(int i = 0; i < size; i++) {
                if (keys[i].equals(key)) {
                    return true;
                }
            }

            return false;
        }

        public Iterable<Key> keys() {
            Queue<Key> queue = new Queue<>();

            for(int i = 0; i < size; i++) {
                queue.enqueue(keys[i]);
            }

            return queue;
        }

        private void resize(int newSize) {
            Key[] tempKeys = (Key[]) new Object[newSize];
            Value[] tempValues = (Value[]) new Object[newSize];

            System.arraycopy(keys, 0, tempKeys, 0, size);
            System.arraycopy(values, 0, tempValues, 0, size);

            keys = tempKeys;
            values = tempValues;
        }

    }

    public static void main(String[] args) {
        Exercise22_SelfOrganizingSearch selfOrganizingSearch = new Exercise22_SelfOrganizingSearch();
        ArraySTSelfOrganizing<Integer, String> arraySTSelfOrganizing = selfOrganizingSearch.new ArraySTSelfOrganizing<>(2);

        for(int i = 0; i < 10; i++) {
            arraySTSelfOrganizing.put(i, "Value " + i);
        }

        StdOut.println("Initial key order:");

        for(Integer key : arraySTSelfOrganizing.keys()) {
            StdOut.println("Key " + key);
        }

        StdOut.println("\nAfter searching for key 4:");
        arraySTSelfOrganizing.get(4);

        for(Integer key : arraySTSelfOrganizing.keys()) {
            StdOut.println("Key " + key);
        }

        StdOut.println("\nAfter searching for key 7:");
        arraySTSelfOrganizing.get(7);

        for(Integer key : arraySTSelfOrganizing.keys()) {
            StdOut.println("Key " + key);
        }

        StdOut.println("\nAfter searching again for key 7:");
        arraySTSelfOrganizing.get(7);

        for(Integer key : arraySTSelfOrganizing.keys()) {
            StdOut.println("Key " + key);
        }

        StdOut.println("\nAfter searching for key 9:");
        arraySTSelfOrganizing.get(9);

        for(Integer key : arraySTSelfOrganizing.keys()) {
            StdOut.println("Key " + key);
        }

    }

}
