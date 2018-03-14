package chapter3.section5;

import chapter3.section3.RedBlackBST;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Rene Argento on 17/08/17.
 */
public class Exercise27_List {

    public class List<Item> implements Iterable<Item> {

        private RedBlackBST<Double, Item> itemsBST;
        private SeparateChainingHashTable<Item, Double> itemsPositions;

        private static final double INITIAL_VALUE = 50000;
        private static final double OFFSET = 0.0001;;

        List() {
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

    public static void main(String[] args) {
        Exercise27_List exercise27_list = new Exercise27_List();
        List<Integer> list = exercise27_list.new List<>();

        StdOut.println("Add item 1 to the front of the list");
        //Test addFront() and addBack()
        list.addFront(1);
        StdOut.println("Add item 0 to the front of the list");
        list.addFront(0);
        StdOut.println("Add item 10 to the back of the list");
        list.addBack(10);
        StdOut.println("Add item 11 to the back of the list");
        list.addBack(11);

        //Test size()
        StdOut.println("\nSize: " + list.size() + " Expected: 4");

        //Test isEmpty()
        StdOut.println("isEmpty: " + list.isEmpty() + " Expected: false\n");

        //Test iterator
        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 0 1 10 11");

        //Test add()
        StdOut.println("\nAdd item 9 on index 2");
        list.add(2, 9);

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 0 1 9 10 11");

        StdOut.println("\nAdd item -1 on index 0");
        list.add(0, -1);

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: -1 0 1 9 10 11");

        //Test deleteFront()
        StdOut.println("\nDelete front");
        list.deleteFront();

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 0 1 9 10 11");

        //Test deleteBack()
        StdOut.println("\nDelete back");
        list.deleteBack();

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 0 1 9 10");

        //Test delete(int index)
        StdOut.println("\nDelete item on index 2");
        list.delete(2);

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 0 1 10");

        StdOut.println("\nDelete item on index 0");
        list.delete(0);

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 1 10");

        //Test delete(Item item)
        StdOut.println("\nDelete item 5");
        list.delete(new Integer(5));

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 1 10");

        StdOut.println("\nDelete item 10");
        list.delete(new Integer(10));

        for(int item : list) {
            StdOut.print(item + " ");
        }
        StdOut.println("\nExpected: 1");

        StdOut.println("\nDelete front");
        list.deleteFront();
        StdOut.println("isEmpty: " + list.isEmpty() + " Expected: true");
    }

}
