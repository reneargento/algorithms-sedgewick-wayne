package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Rene Argento on 8/16/16.
 */
// Thanks to Oreshnik (https://github.com/Oreshnik) for fixing a bug in the shuffleItems() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/40
// Thanks to sienic (https://github.com/sienic) for suggesting a RandomQueueIterator that uses less memory.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/281
@SuppressWarnings("unchecked")
public class Exercise36_RandomQueue2 {

    public static class RandomQueue2<Item> implements Iterable<Item> {
        private Item[] items;
        private int size;

        public RandomQueue2() {
            items = (Item[]) new Object[1];
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void enqueue(Item item) {
            if (size == items.length) {
                resize(items.length * 2);
            }
            items[size] = item;
            size++;
        }

        public Item dequeue() {
            if (isEmpty()) {
                throw new RuntimeException("Queue underflow");
            }
            int randomIndex = StdRandom.uniform(0, size);
            Item randomItem = items[randomIndex];

            items[randomIndex] = items[size - 1];
            items[size - 1] = null;
            size--;
            if (size > 0 && size == items.length / 4) {
                resize(items.length / 2);
            }
            return randomItem;
        }

        public Item sample() {
            if (isEmpty()) {
                throw new RuntimeException("Queue underflow");
            }
            int randomIndex = StdRandom.uniform(0, size);
            return items[randomIndex];
        }

        private void resize(int capacity) {
            Item[] temp = (Item[]) new Object[capacity];
            for (int i = 0; i < size; i++) {
                temp[i] = items[i];
            }
            items = temp;
        }

        @Override
        public Iterator<Item> iterator() {
            return new RandomQueueIterator();
        }

        private class RandomQueueIterator implements Iterator<Item> {
            int index;
            int[] indices;

            public RandomQueueIterator() {
                index = 0;
                indices = new int[items.length];
                Arrays.setAll(indices, i -> i);
                shuffleItems();
            }

            public boolean hasNext() {
                return index < size;
            }

            public Item next() {
                int nextItemIndex = indices[index];
                Item item = items[nextItemIndex];
                index++;
                return item;
            }

            private void shuffleItems() {
                for (int i = 0; i < size - 1; i++) {
                    int randomIndex = StdRandom.uniform(i + 1, size);

                    //Swap
                    int temp = indices[i];
                    indices[i] = indices[randomIndex];
                    indices[randomIndex] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        RandomQueue2<Card> randomQueue = new RandomQueue2<>();
        fillQueueWithBridgeHandsCards(randomQueue);

        StdOut.println("Cards:\n");
        for (Card card : randomQueue) {
            StdOut.println(card);
        }
    }

    private static void fillQueueWithBridgeHandsCards(RandomQueue2<Card> randomQueue) {
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};

        for (String suit : suits) {
            randomQueue.enqueue(new Card("A", suit));
            randomQueue.enqueue(new Card("2", suit));
            randomQueue.enqueue(new Card("3", suit));
            randomQueue.enqueue(new Card("4", suit));
            randomQueue.enqueue(new Card("5", suit));
            randomQueue.enqueue(new Card("6", suit));
            randomQueue.enqueue(new Card("7", suit));
            randomQueue.enqueue(new Card("8", suit));
            randomQueue.enqueue(new Card("9", suit));
            randomQueue.enqueue(new Card("10", suit));
            randomQueue.enqueue(new Card("J", suit));
            randomQueue.enqueue(new Card("Q", suit));
            randomQueue.enqueue(new Card("K", suit));
        }
    }

    private static class Card {
        String value;
        String suit;

        public Card(String value, String suit) {
            this.value = value;
            this.suit = suit;
        }

        @Override
        public String toString() {
            return value + "-" + suit;
        }
    }
}
