package chapter5.section1;

import chapter1.section3.Queue;
import chapter2.section1.InsertionSort;

/**
 * Created by Rene Argento on 10/01/18.
 */
// O(N^2), but runs in O(N) if items are uniformly distributed in the buckets
@SuppressWarnings("unchecked")
public class BucketSort {

    public void bucketSort(String[] strings) {
        int alphabetSize = 256;
        Queue<String>[] buckets = new Queue[alphabetSize + 1];

        for(int bucket = 0; bucket < buckets.length; bucket++) {
            buckets[bucket] = new Queue();
        }

        // 1- Put strings in buckets
        for(int i = 0; i < strings.length; i++) {
            int leadingDigitIndex = strings[i].charAt(0);
            buckets[leadingDigitIndex].enqueue(strings[i]);
        }

        // 2- Sort the sublists
        for(int bucket = 0; bucket < buckets.length; bucket++) {
            if (!buckets[bucket].isEmpty()) {
                sortBucket(buckets[bucket]);
            }
        }

        // 3- Stitch together all the buckets
        int arrayIndex = 0;

        for(int r = 0; r <= alphabetSize; r++) {
            while (!buckets[r].isEmpty()) {
                String currentString = buckets[r].dequeue();
                strings[arrayIndex++] = currentString;
            }
        }
    }

    private void sortBucket(Queue<String> bucket) {
        String[] strings = new String[bucket.size()];
        int arrayIndex = 0;

        while (!bucket.isEmpty()) {
            strings[arrayIndex++] = bucket.dequeue();
        }

        InsertionSort.insertionSort(strings);

        for(String string : strings) {
            bucket.enqueue(string);
        }
    }
}
