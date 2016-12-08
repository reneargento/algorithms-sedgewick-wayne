package chapter1.section3.queue;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by rene on 8/16/16.
 */
public class Exercise35_RandomQueue<Item> {

    private Item[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public Exercise35_RandomQueue() {
        array = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Item item) {
        if(size == array.length) {
            resize(array.length * 2);
        }

        array[size] = item;
        size++;
    }

    public Item dequeue() {
        if(isEmpty()){
            throw new RuntimeException("Queue underflow");
        }

        int randomIndex = StdRandom.uniform(0, size);

        Item randomItem = array[randomIndex];

        array[randomIndex] = array[size - 1];
        array[size - 1] = null;
        size--;

        return randomItem;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        int randomIndex = StdRandom.uniform(0, size);

        Item randomItem = array[randomIndex];
        return randomItem;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i=0; i < size; i++) {
            temp[i] = array[i];
        }

        array = temp;
    }

    public static void main(String[] args) {
        Exercise35_RandomQueue<Card> randomQueue = new Exercise35_RandomQueue<>();
        fillQueueWithBridgeHandsCards(randomQueue);

        for (int i =0; i < 2; i++) {
            int count = 0;
            StdOut.println("Hand " + (i+1));

            while(count < 13) {
                StdOut.println(randomQueue.dequeue());
                count++;
            }
            StdOut.println();
        }

        Card sample = randomQueue.sample();
        StdOut.println("Size before sample: " + randomQueue.size);
        StdOut.println("Random item: " + sample);
        StdOut.println("Size after sample: " + randomQueue.size);
    }

    @SuppressWarnings("unchecked")
    private static void fillQueueWithBridgeHandsCards(Exercise35_RandomQueue randomQueue) {
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};

        for (int i=0; i < suits.length; i++){
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
