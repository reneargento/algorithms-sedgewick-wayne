package chapter3.section1;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 22/04/17.
 */
@SuppressWarnings("unchecked")
public class Exercise2 {

    public class ArrayST<Key, Value> {

        public Key[] keys;
        public Value[] values;
        private int size;

        public ArrayST(int capacity) {
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
                    return values[i];
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
        Exercise2 exercise2 = new Exercise2();
        ArrayST<Integer, String> arrayST = exercise2.new ArrayST<>(2);

        for(int i = 0; i < 10; i++) {
            arrayST.put(i, "Value " + i);
        }

        arrayST.delete(4);

        //Testing iterator
        for(Integer key : arrayST.keys()) {
            StdOut.println("Key " + key + ": " + arrayST.get(key));
        }

        StdOut.println("\nExpected:\n" +
                "Key 0: Value 0\n" +
                "Key 1: Value 1\n" +
                "Key 2: Value 2\n" +
                "Key 3: Value 3\n" +
                "Key 9: Value 9\n" +
                "Key 5: Value 5\n" +
                "Key 6: Value 6\n" +
                "Key 7: Value 7\n" +
                "Key 8: Value 8");
    }

}
