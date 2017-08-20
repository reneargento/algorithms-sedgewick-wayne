package chapter3.section5;

import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by rene on 17/08/17.
 */
public class Exercise27_List {

    public class List<Item> implements Iterable<Item> {

        private SeparateChainingHashTable<Item, Integer> positionsMap;
        private SeparateChainingHashTable<Integer, Item> itemsMap;

        List() {
            positionsMap = new SeparateChainingHashTable<>();
            itemsMap = new SeparateChainingHashTable<>();
        }

        //O(n)
        public void addFront(Item item) {
            if(item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            if(contains(item)) {
                delete(item);
            }

            //Increment the index of all items
            int maxIndex = size() - 1;
            for(int i = maxIndex; i >= 0; i--) {
                Item currentItem = itemsMap.get(i);
                int newIndex = i + 1;

                itemsMap.put(newIndex, currentItem);
                positionsMap.put(currentItem, newIndex);
            }

            itemsMap.put(0, item);
            positionsMap.put(item, 0);
        }

        //O(1) if item is not in the list, O(n) if it is
        public void addBack(Item item) {
            if(item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            if(contains(item)) {
                delete(item);
            }

            int newIndex = size();
            itemsMap.put(newIndex, item);
            positionsMap.put(item, newIndex);
        }

        //O(n)
        public Item deleteFront() {
            if(isEmpty()) {
                return null;
            }

            Item firstItem = itemsMap.get(0);

            for(int i = 1; i < size(); i++) {
                Item currentItem = itemsMap.get(i);
                int newIndex = i - 1;

                itemsMap.put(newIndex, currentItem);
                positionsMap.put(currentItem, newIndex);
            }

            int maxIndex = size() - 1;
            itemsMap.delete(maxIndex);
            positionsMap.delete(firstItem);

            return firstItem;
        }

        //O(1)
        public Item deleteBack() {
            if(isEmpty()) {
                return null;
            }

            int maxIndex = size() - 1;
            Item lastItem = itemsMap.get(maxIndex);

            itemsMap.delete(maxIndex);
            positionsMap.delete(lastItem);

            return lastItem;
        }

        //O(n)
        public void delete(Item item) {
            if(item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            if(!contains(item)) {
                return;
            }

            int size = size();
            int index = positionsMap.get(item);

            //Decrement the index of all items after the ith position
            for(int i = index + 1; i < size; i++) {
                Item currentItem = itemsMap.get(i);
                int newIndex = i - 1;

                itemsMap.put(newIndex, currentItem);
                positionsMap.put(currentItem, newIndex);
            }

            int maxIndex = size() - 1;
            itemsMap.delete(maxIndex);
            positionsMap.delete(item);
        }

        //O(n)
        public void add(int index, Item item) {
            if(item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            if(index < 0 || index > size()
                    || (index == size() && contains(item))) {
                throw new IllegalArgumentException("Invalid index");
            }

            if(contains(item)) {
                delete(item);
            }

            int maxIndex = size() - 1;

            //Increment the index of all items after the ith position
            for(int i = maxIndex; i >= index; i--) {
                int newIndex = i + 1;
                Item currentItem = itemsMap.get(i);

                positionsMap.put(currentItem, newIndex);
                itemsMap.put(newIndex, currentItem);
            }

            positionsMap.put(item, index);
            itemsMap.put(index, item);
        }

        //O(n)
        public Item delete(int index) {
            if(index < 0 || index >= size()) {
                throw new IllegalArgumentException("Invalid index");
            }

            Item deletedItem = itemsMap.get(index);
            delete(deletedItem);
            return deletedItem;
        }

        //O(1)
        public boolean contains(Item item) {
            return positionsMap.contains(item);
        }

        //O(1)
        public boolean isEmpty() {
            return size() == 0;
        }

        //O(1)
        public int size() {
            return itemsMap.size();
        }

        @Override
        public Iterator<Item> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Item> {

            int currentKey;

            ListIterator() {
                currentKey = 0;
            }

            @Override
            public boolean hasNext() {
                return currentKey < size();
            }

            @Override
            public Item next() {
                Item nextItem = itemsMap.get(currentKey);
                currentKey++;
                return nextItem;
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
