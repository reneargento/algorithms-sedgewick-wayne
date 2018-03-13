package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/21/16.
 */
@SuppressWarnings("unchecked")
public class Exercise38_1_DeleteKthElement<Item> implements Iterable<Item>{

    private Item[] queue;
    private int size;

    public Exercise38_1_DeleteKthElement() {
        queue = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(Item item) {

        if (size == queue.length) {
            resize(queue.length * 2);
        }

        queue[size] = item;
        size++;
    }

    public Item delete(int k) {

        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }
        if (k <= 0 || size < k) {
            throw new RuntimeException("Invalid index");
        }

        Item item = queue[k - 1];
        moveItemsLeft(k);

        size--;

        if (size == queue.length / 4) {
            resize(queue.length / 2);
        }

        return item;
    }

    private void moveItemsLeft(int firstIndex) {
        for(int i = firstIndex; i < size; i++) {
            queue[i - 1] = queue[i];
        }
        queue[size - 1] = null; //to avoid loitering
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            temp[i] = queue[i];
        }

        queue = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new GeneralizedQueueIterator();
    }

    private class GeneralizedQueueIterator implements Iterator<Item> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Item next() {
            Item item = queue[index];
            index++;
            return item;
        }
    }

    public static void main(String[] args) {
        Exercise38_1_DeleteKthElement<Integer> generalizedQueue = new Exercise38_1_DeleteKthElement<>();
        generalizedQueue.insert(0);
        generalizedQueue.insert(1);
        generalizedQueue.insert(2);
        generalizedQueue.insert(3);
        generalizedQueue.insert(4);

        generalizedQueue.delete(1);
        generalizedQueue.delete(3);

        StringJoiner generalizedQueueItems = new StringJoiner(" ");
        for (int item : generalizedQueue) {
            generalizedQueueItems.add(String.valueOf(item));
        }

        StdOut.println("Generalized queue items: " + generalizedQueueItems.toString());
        StdOut.println("Expected: 1 2 4");
    }
}
