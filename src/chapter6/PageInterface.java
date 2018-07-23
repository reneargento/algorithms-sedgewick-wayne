package chapter6;

/**
 * Created by Rene Argento on 23/07/18.
 */
public interface PageInterface<Key> {

    void close();

    void add(Key key);

    void add(PageInterface page);

    boolean isExternal();

    boolean contains(Key key);

    PageInterface next(Key key);

    boolean isFull();

    PageInterface split();

    Iterable<Key> keys();

}
