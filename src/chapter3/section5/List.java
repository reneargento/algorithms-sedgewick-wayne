package chapter3.section5;

import chapter3.section3.RedBlackBST;
import chapter3.section4.SeparateChainingHashTable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Rene Argento on 22/08/17.
 */

/**
 * List data structure that supports addFront(), addBack(), deleteFront(), deleteBack(), delete(Item), add(index, Item)
 * and delete(index) operations in O(lg n)
 */
public class List<Item> implements Iterable<Item> {

    private RedBlackBST<Double, Item> itemsBST;
    private SeparateChainingHashTable<Item, Double> itemsPositions;

    private static final double INITIAL_VALUE = 50000;
    private static final double OFFSET = 0.0001;;

    public List() {
        itemsBST = new RedBlackBST<>();
        itemsPositions = new SeparateChainingHashTable<>();
    }

    //O(lg n)
    public void addFront(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (contains(item)) {
            delete(item);
        }

        double minKey;

        if (isEmpty()) {
            minKey = INITIAL_VALUE;
        } else {
            minKey = itemsBST.min();
        }

        double newMinKey = minKey - OFFSET;

        itemsBST.put(newMinKey, item);
        itemsPositions.put(item, newMinKey);
    }

    //O(lg n)
    public void addBack(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (contains(item)) {
            delete(item);
        }

        double maxKey;

        if (isEmpty()) {
            maxKey = INITIAL_VALUE;
        } else {
            maxKey = itemsBST.max();
        }

        double newMaxKey = maxKey + OFFSET;

        itemsBST.put(newMaxKey, item);
        itemsPositions.put(item, newMaxKey);
    }

    //O(lg n)
    public Item deleteFront() {
        if (isEmpty()) {
            return null;
        }

        Item firstItem = itemsBST.get(itemsBST.min());

        itemsBST.deleteMin();
        itemsPositions.delete(firstItem);

        return firstItem;
    }

    //O(lg n)
    public Item deleteBack() {
        if (isEmpty()) {
            return null;
        }

        Item lastItem = itemsBST.get(itemsBST.max());

        itemsBST.deleteMax();
        itemsPositions.delete(lastItem);

        return lastItem;
    }

    //O(lg n)
    public void delete(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (!contains(item)) {
            return;
        }

        double itemPosition = itemsPositions.get(item);
        itemsBST.delete(itemPosition);
        itemsPositions.delete(item);
    }

    //O(lg n)
    public void add(int index, Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (index < 0 || index > size()
                || (index == size() && contains(item))) {
            throw new IllegalArgumentException("Invalid index");
        }

        if (contains(item)) {
            delete(item);
        }

        double previousItemIndex = 0;
        double nextItemIndex = size() - 1;

        if (index > 0) {
            previousItemIndex = itemsBST.select(index - 1);
        } else if (index == 0) {
            previousItemIndex = itemsBST.min() - OFFSET;
        }

        if (index < size()) {
            nextItemIndex = itemsBST.select(index);
        } else if (index == size()) {
            nextItemIndex = itemsBST.max() + OFFSET;
        }

        double medianKey = (previousItemIndex + nextItemIndex) / 2;

        itemsBST.put(medianKey, item);
        itemsPositions.put(item, medianKey);
    }

    //O(lg n)
    public Item delete(int index) {
        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Invalid index");
        }

        Item deletedItem = itemsBST.get(itemsBST.select(index));
        delete(deletedItem);
        return deletedItem;
    }

    //O(1)
    public boolean contains(Item item) {
        return itemsPositions.contains(item);
    }

    //O(1)
    public boolean isEmpty() {
        return size() == 0;
    }

    //O(1)
    public int size() {
        return itemsPositions.size();
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    //O(n)
    private class ListIterator implements Iterator<Item> {

        Queue<Double> keys;

        ListIterator() {
            keys = new LinkedList<>();

            for(Double key : itemsBST.keys()) {
                keys.add(key);
            }
        }

        @Override
        public boolean hasNext() {
            return keys.size() > 0;
        }

        @Override
        public Item next() {
            return itemsBST.get(keys.poll());
        }
    }

}
