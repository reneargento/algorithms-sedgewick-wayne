package chapter5.section2;

/**
 * Created by Rene Argento on 18/02/18.
 */
public interface TrieInterface<Value> {

    int size();
    boolean isEmpty();
    boolean contains(String key);
    Value get(String key);
    void put(String key, Value value);
    void delete(String key);

    Iterable<String> keys();
    Iterable<String> keysWithPrefix(String prefix);
    Iterable<String> keysThatMatch(String pattern);
    String longestPrefixOf(String query);

}
