package chapter3.section5;

import chapter1.section3.DoublyLinkedList;
import chapter3.section4.SeparateChainingHashTable;

/**
 * Created by Rene Argento on 01/10/17.
 */
@SuppressWarnings("unchecked")
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
