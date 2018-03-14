package chapter3.section5;

import chapter1.section3.DoublyLinkedList;
import chapter3.section4.SeparateChainingHashTable;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Rene Argento on 17/08/17.
 */
@SuppressWarnings("unchecked")
public class Exercise26_LRUCache {

    public class LRUCache<Item> {

        private DoublyLinkedList<Item> doublyLinkedList;
        private SeparateChainingHashTable<Item, DoublyLinkedList.DoubleNode> hashTable;

        LRUCache() {
            doublyLinkedList = new DoublyLinkedList<>();
            hashTable = new SeparateChainingHashTable<>();
        }

        public int size() {
            return doublyLinkedList.size();
        }

        //O(1)
        public void access(Item item) {

            if (hashTable.contains(item)) {
                DoublyLinkedList.DoubleNode itemNodeInList = hashTable.get(item);
                doublyLinkedList.removeItemWithNode(itemNodeInList);
            }

            DoublyLinkedList.DoubleNode newListNode = doublyLinkedList.insertAtTheBeginningAndReturnNode(item);
            //Overwrite item in hash table if it exists or simply add it if it does not exist
            hashTable.put(item, newListNode);
        }

        //O(1)
        public Item remove() {
            Item leastRecentlyAccessedItem = doublyLinkedList.removeFromTheEnd();

            if (leastRecentlyAccessedItem != null) {
                hashTable.delete(leastRecentlyAccessedItem);
            }

            return leastRecentlyAccessedItem;
        }

    }

    public static void main(String[] args) {
        Exercise26_LRUCache exercise26_lruCache = new Exercise26_LRUCache();
        LRUCache<Integer> lruCache = exercise26_lruCache.new LRUCache<>();

        lruCache.access(1);
        lruCache.access(2);
        lruCache.access(3);
        lruCache.access(4);
        lruCache.access(5);

        StdOut.println("Size: " + lruCache.size() + " Expected: 5");
        StdOut.println("Least recently accessed item: " + lruCache.remove() + " Expected: 1");
        StdOut.println("Least recently accessed item: " + lruCache.remove() + " Expected: 2");

        lruCache.access(3);
        StdOut.println("Least recently accessed item: " + lruCache.remove() + " Expected: 4");
        StdOut.println("Size: " + lruCache.size() + " Expected: 2");
    }

}
