package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by Rene Argento on 8/16/16.
 */
@SuppressWarnings("unchecked")
public class Exercise35_RandomQueue {

    public class RandomQueue<Item> {
        private Item[] items;
        private int size;

        public RandomQueue() {
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
    }

    public static void main(String[] args) {
        Exercise35_RandomQueue exercise35_randomQueue = new Exercise35_RandomQueue();
        RandomQueue<Card> randomQueue = exercise35_randomQueue.new RandomQueue<>();

        fillQueueWithBridgeHandsCards(randomQueue);

        for (int i = 0; i < 2; i++) {
            int count = 0;
            StdOut.println("Hand " + (i + 1));

            while(count < 13) {
                StdOut.println(randomQueue.dequeue());
                count++;
            }
            StdOut.println();
        }

        Card sample = randomQueue.sample();
        StdOut.println("Size before sample: " + randomQueue.size + " Expected: 26");
        StdOut.println("Random item: " + sample);
        StdOut.println("Size after sample: " + randomQueue.size + " Expected: 26");
    }

    @SuppressWarnings("unchecked")
    private static void fillQueueWithBridgeHandsCards(RandomQueue randomQueue) {
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
