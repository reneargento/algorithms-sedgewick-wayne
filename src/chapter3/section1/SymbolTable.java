package chapter3.section1;

/**
 * Created by Rene Argento on 12/06/17.
 */
public interface SymbolTable<Key, Value> {

    Value get(Key key);
    void put(Key key, Value value);
    boolean contains(Key key);
    void delete(Key key);
    Iterable<Key> keys();

}
