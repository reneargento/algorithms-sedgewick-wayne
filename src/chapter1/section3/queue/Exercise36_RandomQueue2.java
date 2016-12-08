package chapter1.section3.queue;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/**
 * Created by rene on 8/16/16.
 */
@SuppressWarnings("unchecked")
public class Exercise36_RandomQueue2<Item> implements Iterable<Item>{

    private Item[] array;
    private int size;

    public Exercise36_RandomQueue2() {
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

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];

        for (int i=0; i < size; i++) {
            temp[i] = array[i];
        }

        array = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {

        int index;
        Item[] arrayCopy;

        public RandomQueueIterator(){
            index = 0;
            arrayCopy = (Item[]) new Object[array.length];

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
            for (int i=0; i < size; i++) {
                arrayCopy[i] = array[i];
            }
        }

        private void shuffleItems() {

            for (int i=0; i<size; i++) {
                int randomIndex = StdRandom.uniform(0, size);

                //Swap
                Item temp = arrayCopy[i];
                arrayCopy[i] = arrayCopy[randomIndex];
                arrayCopy[randomIndex] = temp;
            }
        }
    }


    public static void main(String[] args) {
        Exercise36_RandomQueue2<Card> randomQueue = new Exercise36_RandomQueue2<>();
        fillQueueWithBridgeHandsCards(randomQueue);

        for (Card card : randomQueue) {
            StdOut.println(card);
        }
    }

    private static void fillQueueWithBridgeHandsCards(Exercise36_RandomQueue2 randomQueue) {
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
