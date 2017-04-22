package chapter3.section1;

/**
 * Created by rene on 22/04/17.
 */
public class SequentialSearchSymbolTable<Key, Value> {

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

    private Node first;

    public Value get(Key key) {
        for(Node node = first; node != null; node = node.next) {
            if(key.equals(node.key)) {
                return node.value;
            }
        }

        return null;
    }

    public void put(Key key, Value value) {
        for(Node node = first; node != null; node = node.next) {
            if(key.equals(node.key)) {
                node.value = value;
                return;
            }
        }

        first = new Node(key, value, first);
    }

}
