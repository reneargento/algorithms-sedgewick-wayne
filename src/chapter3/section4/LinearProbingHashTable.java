package chapter3.section4;

/**
 * Created by rene on 17/07/17.
 */
@SuppressWarnings("unchecked")
public class LinearProbingHashTable<Key, Value> {

    private int keysSize;
    private int size;
    private Key[] keys;
    private Value[] values;

    private LinearProbingHashTable(int size) {
        this.size = size;
        keys = (Key[]) new Object[size];
        values = (Value[]) new Object[size];
    }

    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % size;
    }

    private void resize(int newSize) {
        LinearProbingHashTable<Key, Value> tempHashTable = new LinearProbingHashTable<>(newSize);

        for(int i=0; i < size; i++) {
            if(keys[i] != null) {
                tempHashTable.put(keys[i], values[i]);
            }
        }

        keys = tempHashTable.keys;
        values = tempHashTable.values;
        size = tempHashTable.size;
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

        for(int tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
            if(keys[tableIndex].equals(key)) {
                return values[tableIndex];
            }
        }

        return null;
    }

    public void put(Key key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        if(value == null) {
            delete(key);
            return;
        }

        if(keysSize >= size / 2) {
            resize(size * 2);
        }

        int tableIndex;
        for(tableIndex = hash(key); keys[tableIndex] != null; tableIndex = (tableIndex + 1) % size) {
            if(keys[tableIndex].equals(key)) {
                values[tableIndex] = value;
                return;
            }
        }

        keys[tableIndex] = key;
        values[tableIndex] = value;
        keysSize++;
    }

    public void delete(Key key) {
        if(!contains(key)) {
            return;
        }

        int tableIndex = hash(key);
        while (!keys[tableIndex].equals(key)) {
            tableIndex = (tableIndex + 1) % size;
        }

        keys[tableIndex] = null;
        values[tableIndex] = null;

        tableIndex = (tableIndex + 1) % size;

        while (keys[tableIndex] != null) {
            Key keyToRedo = keys[tableIndex];
            Value valueToRedo = values[tableIndex];

            keys[tableIndex] = null;
            values[tableIndex] = null;
            keysSize--;

            put(keyToRedo, valueToRedo);
            tableIndex = (tableIndex + 1) % size;
        }

        keysSize--;
        if(keysSize > 0 && keysSize <= size / 8) {
            resize(size / 2);
        }
    }

}
