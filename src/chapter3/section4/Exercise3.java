package chapter3.section4;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rene on 18/07/17.
 */
@SuppressWarnings("unchecked")
public class Exercise3 {

    private class SeparateChainingHashTableLinkedListWithDeleteK<Key, Value> {

        private class Node {
            Key key;
            Value value;
            int numberOfKeysAtTimeOfInsert;
            Node next;

            public Node(Key key, Value value, Node next, int numberOfKeysAtTimeOfInsert) {
                this.key = key;
                this.value = value;
                this.next = next;
                this.numberOfKeysAtTimeOfInsert = numberOfKeysAtTimeOfInsert;
            }
        }

        private List<Node> buckets;
        private int keysSize;
        private int numberOfBuckets;

        private static final double INCREASE_THRESHOLD = 0.7;
        private static final double DECREASE_THRESHOLD = 0.25;

        SeparateChainingHashTableLinkedListWithDeleteK(int numberOfBuckets) {
            keysSize = 0;
            this.numberOfBuckets = numberOfBuckets;
            buckets = new ArrayList<>(numberOfBuckets);

            for(int i = 0; i < numberOfBuckets; i++) {
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
            Node newNode = new Node(key, value, node, keysSize);
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

        public void deleteNewestNodes(int k) {
            if(k < 0) {
                throw new IllegalArgumentException("K cannot be negative");
            }

            if(isEmpty()) {
                return;
            }

            for(int i = 0; i < buckets.size(); i++) {
                if(buckets.get(i) != null) {
                    Node node = buckets.get(i);
                    Node previous = null;

                    while (node != null && node.numberOfKeysAtTimeOfInsert <= k) {
                        previous = node;
                        node = node.next;
                    }

                    int numberOfDeletedNodes = 0;
                    while (node != null) {
                        numberOfDeletedNodes++;
                        node = node.next;
                    }

                    buckets.set(i, previous);
                    keysSize -= numberOfDeletedNodes;
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
                for(int i = 0; i < keysToBeSorted.length; i++) {
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
        Exercise3 exercise3 = new Exercise3();
        SeparateChainingHashTableLinkedListWithDeleteK<Integer, Integer> separateChainingHashTableLinkedListWithDeleteK =
                exercise3.new SeparateChainingHashTableLinkedListWithDeleteK<>(5);

        separateChainingHashTableLinkedListWithDeleteK.put(1, 1);
        separateChainingHashTableLinkedListWithDeleteK.put(2, 2);
        separateChainingHashTableLinkedListWithDeleteK.put(3, 3);
        separateChainingHashTableLinkedListWithDeleteK.put(4, 4);
        separateChainingHashTableLinkedListWithDeleteK.put(5, 5);
        separateChainingHashTableLinkedListWithDeleteK.put(6, 6);
        separateChainingHashTableLinkedListWithDeleteK.put(7, 7);

        System.out.println("\nKeys");
        for(Integer key : separateChainingHashTableLinkedListWithDeleteK.keys()) {
            System.out.print(key + " ");
        }

        int[] kValuesToTest = {8, 7, 6, 4, 3, 2, 0};
        for(int k : kValuesToTest) {
            System.out.println("\nDelete K = " + k);
            separateChainingHashTableLinkedListWithDeleteK.deleteNewestNodes(k);

            for(Integer key : separateChainingHashTableLinkedListWithDeleteK.keys()) {
                System.out.print(key + " ");
            }
            System.out.println("\nSize: " + separateChainingHashTableLinkedListWithDeleteK.size());
        }
    }

}
