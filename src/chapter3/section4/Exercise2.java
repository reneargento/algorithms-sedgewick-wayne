package chapter3.section4;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rene on 18/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise2 {

    private class SeparateChainingHashTableLinkedList<Key, Value> {

        private class Node {
            Key key;
            Value value;
            Node next;

            public Node(Key key, Value value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }

        private List<Node> buckets;
        private int keysSize;
        private int numberOfBuckets;

        private static final double INCREASE_THRESHOLD = 0.7;
        private static final double DECREASE_THRESHOLD = 0.25;

        public SeparateChainingHashTableLinkedList() {
            this(10);
        }

        SeparateChainingHashTableLinkedList(int numberOfBuckets) {
            keysSize = 0;
            this.numberOfBuckets = numberOfBuckets;
            buckets = new ArrayList<>(numberOfBuckets);

            for(int i=0; i < numberOfBuckets; i++) {
                buckets.add(null);
            }
        }

        private int hash(Key key) {
            return (key.hashCode() & 0x7fffffff) % numberOfBuckets;
        }

        private double getLoadFactor() {
            return ((double) keysSize) / (double) numberOfBuckets;
        }

        public boolean isEmpty() {
            return keysSize == 0;
        }

        public int size() {
            return keysSize;
        }

        public boolean contains(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to contains() cannot be null");
            }

            return get(key) != null;
        }

        private void resize(int newBucketSize) {

            List<Node> tempBuckets = buckets;
            buckets = new ArrayList<>(newBucketSize);

            numberOfBuckets = newBucketSize;
            keysSize = 0;

            for(int i = 0; i < newBucketSize; i++) {
                buckets.add(null);
            }

            for(Node node : tempBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }

        public Value get(Key key) {
            if (key == null) {
                throw new IllegalArgumentException("Argument to get() cannot be null");
            }

            //Get node at the head of the bucket
            Node node = buckets.get(hash(key));

            while (node != null) {
                if(node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }

            return null;
        }

        public void put(Key key, Value value) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }

            if(value == null) {
                delete(key);
                return;
            }

            //Get node at the head of the bucket
            int bucketIndex = hash(key);
            Node node = buckets.get(bucketIndex);

            //Update key value if it already exists
            while (node != null) {
                if(node.key.equals(key)) {
                    node.value = value;
                    return;
                }

                node = node.next;
            }

            keysSize++;
            node = buckets.get(bucketIndex);
            Node newNode = new Node(key, value, node);
            buckets.set(bucketIndex, newNode);

            if(getLoadFactor() > INCREASE_THRESHOLD) {
                resize(numberOfBuckets * 2);
            }
        }

        public void delete(Key key) {
            if(isEmpty()) {
                return;
            }

            if (key == null) {
                throw new IllegalArgumentException("Argument to delete() cannot be null");
            }

            if(!contains(key)) {
                return;
            }

            int bucketIndex = hash(key);
            Node node = buckets.get(bucketIndex);

            keysSize--;

            if(node.key.equals(key)) {
                buckets.set(bucketIndex, node.next);
            } else {
                while (node != null) {
                    if(node.next.equals(key)) {
                        node.next = node.next.next;
                        break;
                    }

                    node = node.next;
                }
            }

            if(getLoadFactor() < DECREASE_THRESHOLD) {
                resize(numberOfBuckets / 2);
            }
        }

        public Iterable<Key> keys() {
            Queue<Key> keys = new Queue<>();

            for(int i = 0; i < buckets.size(); i++) {
                if(buckets.get(i) != null) {
                    Node node = buckets.get(i);

                    while (node != null) {
                        keys.enqueue(node.key);
                        node = node.next;
                    }
                }
            }

            if(!keys.isEmpty() && keys.peek() instanceof Comparable) {
                Key[] keysToBeSorted = (Key[]) new Comparable[keys.size()];
                for(int i=0; i < keysToBeSorted.length; i++) {
                    keysToBeSorted[i] = keys.dequeue();
                }

                Arrays.sort(keysToBeSorted);

                for(Key key : keysToBeSorted) {
                    keys.enqueue(key);
                }
            }

            return keys;
        }

    }

    public static void main(String[] args) {
        Exercise2 exercise2 = new Exercise2();
        SeparateChainingHashTableLinkedList<Integer, Integer> separateChainingHashTableLinkedList =
                exercise2.new SeparateChainingHashTableLinkedList<>(5);

        separateChainingHashTableLinkedList.put(10, 10);
        separateChainingHashTableLinkedList.put(99, 99);
        separateChainingHashTableLinkedList.put(-5, -5);
        separateChainingHashTableLinkedList.put(33, 33);
        separateChainingHashTableLinkedList.put(2, 2);
        separateChainingHashTableLinkedList.put(1, 1);
        separateChainingHashTableLinkedList.put(9, 9);

        System.out.println("Contains 33, expected: true");
        System.out.println(separateChainingHashTableLinkedList.contains(33));
        System.out.println("Contains 34, expected: false");
        System.out.println(separateChainingHashTableLinkedList.contains(34));

        System.out.println("\nGet -5, 10 and 99");
        System.out.println(separateChainingHashTableLinkedList.get(-5));
        System.out.println(separateChainingHashTableLinkedList.get(10));
        System.out.println(separateChainingHashTableLinkedList.get(99));

        System.out.println("\nKeys");
        for(Integer key : separateChainingHashTableLinkedList.keys()) {
            System.out.print(key + " ");
        }
        System.out.println("\nExpected: -5 1 2 9 10 33 99");

        System.out.println("\nDelete -5 and 2");
        separateChainingHashTableLinkedList.delete(-5);
        separateChainingHashTableLinkedList.delete(2);

        System.out.println("\nKeys");
        for(Integer key : separateChainingHashTableLinkedList.keys()) {
            System.out.print(key + " ");
        }
        System.out.println("\nExpected: 1 9 10 33 99");

        System.out.println("\nSize, expected: 5");
        System.out.println(separateChainingHashTableLinkedList.size());

        System.out.println("\nDelete all");
        separateChainingHashTableLinkedList.delete(1);
        separateChainingHashTableLinkedList.delete(9);
        separateChainingHashTableLinkedList.delete(10);
        separateChainingHashTableLinkedList.delete(33);
        separateChainingHashTableLinkedList.delete(99);

        System.out.println("Size, expected: 0");
        System.out.println(separateChainingHashTableLinkedList.size());
    }

}
