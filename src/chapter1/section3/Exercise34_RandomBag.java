package chapter1.section3;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Created by Rene Argento on 8/16/16.
 */
public class Exercise34_RandomBag<Item> implements Iterable<Item>{

    private Item[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public Exercise34_RandomBag() {
        array = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void add(Item item) {
        if (size() == array.length) {
            resize(array.length * 2);
        }

        array[size] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int capacity) {

        Item[] temp = (Item[]) new Object[capacity];

        for(int i = 0; i < size(); i++) {
            temp[i] = array[i];
        }

        array = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomBagIterator();
    }

    @SuppressWarnings("unchecked")
    private class RandomBagIterator implements Iterator<Item> {

        int index;
        Item[] arrayCopy;

        public RandomBagIterator() {
            index = 0;
            arrayCopy = (Item[]) new Object[size];

            for(int i = 0; i < size; i++) {
                arrayCopy[i] = array[i];
            }

            sortArrayCopy();
        }

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public Item next() {
            Item item = arrayCopy[index];
            index++;
            return item;
        }

        private void sortArrayCopy() {
            for(int i = 0; i < size; i++) {

                int randomIndex = StdRandom.uniform(0, size - 1);

                //Swap
                Item temp = arrayCopy[i];
                arrayCopy[i] = arrayCopy[randomIndex];
                arrayCopy[randomIndex] = temp;
            }
        }
    }

    public static void main(String[] args) {
        Exercise34_RandomBag<Integer> randomBag = new Exercise34_RandomBag<>();
        randomBag.add(1);
        randomBag.add(2);
        randomBag.add(3);
        randomBag.add(4);
        randomBag.add(5);
        randomBag.add(6);
        randomBag.add(7);
        randomBag.add(8);

        StdOut.print("Random bag items: ");

        StringJoiner randomBagItems = new StringJoiner(" ");
        for (int item : randomBag) {
            randomBagItems.add(String.valueOf(item));
        }

        StdOut.println(randomBagItems.toString());
    }

}
