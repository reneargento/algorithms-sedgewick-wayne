package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/**
 * Created by Rene Argento on 8/16/16.
 */
// Thanks to Oreshnik (https://github.com/Oreshnik) for fixing a bug in the shuffleItems() method.
// https://github.com/reneargento/algorithms-sedgewick-wayne/issues/40
@SuppressWarnings("unchecked")
public class Exercise36_RandomQueue2 {

    public class RandomQueue2<Item> implements Iterable<Item> {
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
            Item[] arrayCopy;

            public RandomQueueIterator() {
                index = 0;
                arrayCopy = (Item[]) new Object[items.length];

                copyArray();
                shuffleItems();
            }

            public boolean hasNext() {
                return index < size;
            }

            public Item next() {
                Item item = arrayCopy[index];
                index++;
                return item;
            }

            private void copyArray() {
                for (int i = 0; i < size; i++) {
                    arrayCopy[i] = items[i];
                }
            }

            private void shuffleItems() {
                for (int i = 0; i < size; i++) {
                    int randomIndex = StdRandom.uniform(0, i + 1);

                    //Swap
                    Item temp = arrayCopy[i];
                    arrayCopy[i] = arrayCopy[randomIndex];
                    arrayCopy[randomIndex] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        Exercise36_RandomQueue2 exercise36_randomQueue2 = new Exercise36_RandomQueue2();
        RandomQueue2<Card> randomQueue = exercise36_randomQueue2.new RandomQueue2<>();
        fillQueueWithBridgeHandsCards(randomQueue);

        StdOut.println("Cards:\n");

        for (Card card : randomQueue) {
            StdOut.println(card);
        }
    }

    private static void fillQueueWithBridgeHandsCards(RandomQueue2 randomQueue) {
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};

        for (int i = 0; i < suits.length; i++) {
            randomQueue.enqueue(new Card("A", suits[i]));
            randomQueue.enqueue(new Card("2", suits[i]));
            randomQueue.enqueue(new Card("3", suits[i]));
            randomQueue.enqueue(new Card("4", suits[i]));
            randomQueue.enqueue(new Card("5", suits[i]));
            randomQueue.enqueue(new Card("6", suits[i]));
            randomQueue.enqueue(new Card("7", suits[i]));
            randomQueue.enqueue(new Card("8", suits[i]));
            randomQueue.enqueue(new Card("9", suits[i]));
            randomQueue.enqueue(new Card("10", suits[i]));
            randomQueue.enqueue(new Card("J", suits[i]));
            randomQueue.enqueue(new Card("Q", suits[i]));
            randomQueue.enqueue(new Card("K", suits[i]));
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
