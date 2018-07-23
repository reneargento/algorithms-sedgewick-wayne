package chapter6;

/**
 * Created by Rene Argento on 23/07/18.
 */
public class Page<Key> implements PageInterface<Key> {

    Page(boolean bottom) {

    }

    @Override
    public void close() {

    }

    @Override
    public void add(Key key) {

    }

    @Override
    public void add(PageInterface page) {

    }

    @Override
    public boolean isExternal() {
        return false;
    }

    @Override
    public boolean contains(Key key) {
        return false;
    }

    @Override
    public PageInterface next(Key key) {
        return null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public PageInterface split() {
        return null;
    }

    @Override
    public Iterable<Key> keys() {
        return null;
    }
}
