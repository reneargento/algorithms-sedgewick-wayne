package chapter6.btrees;

/**
 * Created by Rene Argento on 25/07/18.
 */
public interface PageSTInterface<Key, Value> {

    int pageSize();

    int keysSize(boolean countSentinel);

    void setKeysSize(int size);

    void incrementKeysSize();

    void decrementKeysSize();

    boolean isEmpty();

    boolean containsSentinel();

    void setContainsSentinel(boolean containsSentinel);

    void open();

    void close(boolean verbose);

    boolean isExternal();

    boolean contains(Key key);

    Value get(Key key);

    PageSTInterface<Key, Value> getParentPage();

    void setParentPage(PageSTInterface<Key, Value> parentPage);

    PageSTInterface<Key, Value> getChildPage(Key key);

    void add(Key key, Value value);

    void addPage(PageSTInterface<Key, Value> page);

    void addAllKeysFromPage(PageSTInterface<Key, Value> page);

    Key min();

    Key max();

    Key floor(Key key);

    Key ceiling(Key key);

    Key select(int index);

    int rank(Key key);

    void delete(Key key);

    void deletePageWithKey(Key key);

    void deleteMin();

    void deleteMax();

    PageSTInterface<Key, Value> next(Key key);

    boolean isFull();

    PageSTInterface<Key, Value> split();

    Iterable<Key> keys();

    int maxNumberOfNodes();

}
