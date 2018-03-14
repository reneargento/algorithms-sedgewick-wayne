package chapter1.section3;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/21/16.
 */
@SuppressWarnings("unchecked")
public class Exercise39_RingBuffer<Item> implements Iterable<Item>{

    private Item[] ringBuffer;
    private int size;
    private int first;
    private int last;

    private Queue<Item> producerAuxBuffer;
    private int dataCountToBeConsumed;

    public Exercise39_RingBuffer(int capacity) {
        ringBuffer = (Item[]) new Object[capacity];
        size = 0;
        first = -1;
        last = -1;

        producerAuxBuffer = new Queue<>();
        dataCountToBeConsumed = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void produce(Item item) {

        if (dataCountToBeConsumed > 0) {
            //There is data to be consumed
            consumeData(item);
            dataCountToBeConsumed--;
        } else {
            if (isEmpty()) {
                ringBuffer[size] = item;
                first = 0;
                last = 0;
                size++;
            } else {
                if (size < ringBuffer.length) {
                    if (last == ringBuffer.length - 1) {
                        last = 0; //wrap around
                    } else {
                        last++;
                    }
                    ringBuffer[last] = item;
                    size++;
                } else {
                    //RingBuffer is full - add to auxiliary Producer Buffer
                    producerAuxBuffer.enqueue(item);
                }
            }
        }
    }

    private void consumeData(Item item) {
        //Consumer consumes the item
        StdOut.print("Data consumed: " + item);
    }

    public Item consume() {
        if (isEmpty()) {
            dataCountToBeConsumed++;
            return null;
        }

        Item item = ringBuffer[first];
        ringBuffer[first] = null; //avoid loitering

        if (first == ringBuffer.length - 1) {
            first = 0; //wrap around
        } else {
            first++;
        }

        size--;

        if (!producerAuxBuffer.isEmpty()) {
            produce(producerAuxBuffer.dequeue());
        }

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RingBufferIterator();
    }

    private class RingBufferIterator implements Iterator<Item> {

        private int current = first;
        private int count = 0;

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Item next() {
            Item item = ringBuffer[current];

            if (current == ringBuffer.length - 1) {
                current = 0; //Wrap around
            } else {
                current++;
            }

            count++;
            return item;
        }

    }

    public static void main(String[] args) {
        Exercise39_RingBuffer<Integer> ringBuffer = new Exercise39_RingBuffer<>(4);
        ringBuffer.produce(0);
        ringBuffer.produce(1);
        ringBuffer.produce(2);
        ringBuffer.produce(3);
        ringBuffer.produce(4);
        ringBuffer.produce(5);

        Integer item1 = ringBuffer.consume();
        if (item1 != null) {
            StdOut.println("Consumed " + item1);
        }
        StdOut.println("Expected: 0\n");

        Integer item2 = ringBuffer.consume();
        if (item2 != null) {
            StdOut.println("Consumed " + item2);
        }
        StdOut.println("Expected: 1\n");

        ringBuffer.produce(6);
        ringBuffer.produce(7);

        StringJoiner ringBufferItems = new StringJoiner(" ");
        for (int item : ringBuffer) {
            ringBufferItems.add(String.valueOf(item));
        }

        StdOut.println("Main ring buffer items: " + ringBufferItems.toString());
        StdOut.println("Expected: 2 3 4 5");
    }
}
