package chapter3.section4;

import edu.princeton.cs.algs4.StdOut;

/**
 * Created by rene on 22/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise27_DoubleProbing {

    static class SeparateChainingHashTableDoubleProbing<Key, Value> extends SeparateChainingHashTable<Key, Value> {

        SeparateChainingHashTableDoubleProbing() {
            super();
        }

        SeparateChainingHashTableDoubleProbing(int size, int averageListSize) {
            super(size, averageListSize);
        }

        int hash1(Key key) {
            int hash = key.hashCode() & 0x7fffffff;
            return (11 * hash) % size;
        }

        int hash2(Key key) {
            int hash = key.hashCode() & 0x7fffffff;
            return (17 * hash) % size;
        }

        public void resize(int newSize) {
            SeparateChainingHashTableDoubleProbing<Key, Value> separateChainingHashTableDoubleProbing =
                    new SeparateChainingHashTableDoubleProbing<>(newSize, averageListSize);

            for(Key key : keys()) {
                separateChainingHashTableDoubleProbing.put(key, get(key));
            }

            symbolTable = separateChainingHashTableDoubleProbing.symbolTable;
            size = separateChainingHashTableDoubleProbing.size;
            keysSize = separateChainingHashTableDoubleProbing.keysSize;
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            int hash1 = hash1(key);
            int hash2 = hash2(key);

            Value value;
            if(symbolTable[hash1].size <= symbolTable[hash2].size) {
                StdOut.println("Searching in list 1");
                value = (Value) symbolTable[hash1].get(key);

                if(value == null && hash1 != hash2) {
                    StdOut.println("Searching in list 2");
                    value = (Value) symbolTable[hash2].get(key);
                }
            } else {
                StdOut.println("Searching in list 2");
                value = (Value) symbolTable[hash2].get(key);

                if(value == null) {
                    StdOut.println("Searching in list 1");
                    value = (Value) symbolTable[hash1].get(key);
                }
            }

            return value;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if(value == null) {
                delete(key);
                return;
            }

            boolean containsKey = contains(key);

            int hash1 = hash1(key);
            int hash2 = hash2(key);

            if(!containsKey) {
                keysSize++;

                if(symbolTable[hash1].size <= symbolTable[hash2].size) {
                    StdOut.println("Inserting in list 1");
                    symbolTable[hash1].put(key, value);
                } else {
                    StdOut.println("Inserting in list 2");
                    symbolTable[hash2].put(key, value);
                }
            } else {
                boolean isInList1 = false;

                for(Object keyInList1 : symbolTable[hash1].keys()) {
                    if(keyInList1.equals(key)) {
                        isInList1 = true;
                        break;
                    }
                }

                if(isInList1) {
                    StdOut.println("Updating list 1");
                    symbolTable[hash1].put(key, value);
                } else {
                    StdOut.println("Updating list 2");
                    symbolTable[hash2].put(key, value);
                }
            }

            if(getLoadFactor() > averageListSize) {
                resize(size * 2);
                lgM++;
            }
        }

        public void delete(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if(isEmpty() || !contains(key)) {
                return;
            }

            keysSize--;

            int hash1 = hash1(key);
            int hash2 = hash2(key);

            if(!symbolTable[hash1].isEmpty() &&
                    (symbolTable[hash1].size <= symbolTable[hash2].size || symbolTable[hash2].isEmpty())) {
                StdOut.println("Deleting in list 1");
                int symbolTableSize = symbolTable[hash1].size;

                symbolTable[hash1].delete(key);
                //Key is not on the shorter list
                if(symbolTableSize == symbolTable[hash1].size) {
                    StdOut.println("Deleting in list 2");
                    symbolTable[hash2].delete(key);
                }
            } else {
                StdOut.println("Deleting in list 2");
                int symbolTableSize = symbolTable[hash2].size;

                symbolTable[hash2].delete(key);
                //Key is not on the shorter list
                if(symbolTableSize == symbolTable[hash2].size) {
                    StdOut.println("Deleting in list 1");
                    symbolTable[hash1].delete(key);
                }
            }

            if(size > 1 && getLoadFactor() <= averageListSize / (double) 4) {
                resize(size / 2);
                lgM--;
            }
        }
    }

    public static void main(String[] args) {
        SeparateChainingHashTableDoubleProbing<Integer, Integer> separateChainingHashTableDoubleProbing =
                new Exercise27_DoubleProbing.SeparateChainingHashTableDoubleProbing<>(9, 2);

        for(int key = 1; key <= 100; key++) {
            separateChainingHashTableDoubleProbing.put(key, key);
        }

        separateChainingHashTableDoubleProbing.get(3);
        separateChainingHashTableDoubleProbing.get(7);

        separateChainingHashTableDoubleProbing.delete(3);
        separateChainingHashTableDoubleProbing.delete(5);

        separateChainingHashTableDoubleProbing.put(10, 10);
    }

}
