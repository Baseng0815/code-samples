package zettel5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A buffer based on the least recently used strategy.
 * The buffer keeps a configurable number of elements.
 * Once full, it evicts the element that has not been accessed longest,
 * i.e. the put or get operation is furthest away from the present.
 */
public class LRUBuffer<Key, Value> {
    /**
     * The fixed capacity of the buffer. Once reached, an element will be replaced.
     */
    private final int capacity;
    private final Map<Key, SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>>> kvStore;
    private final SimpleDoubleLinkedList<SimpleEntry<Key, Value>> entryList;

    public LRUBuffer(int capacity) {
        this.capacity = capacity;
        this.kvStore = new HashMap<>();
        this.entryList = new SimpleDoubleLinkedList<>();
    }

    /**
     * Puts the given key and value into the buffer possibly replacing and returning a value previously
     * associated with the key. If the buffer is full, the least recently used element is evicted.
     * The operation has an expected runtime of O(1).
     *
     * @param key   The search key of the element.
     * @param value The value associated with the key.
     * @return The value previously associated with key; null if the key is not present in the buffer.
     */
    public Value put(Key key, Value value) { // O(1)
        if (this.kvStore.containsKey(key)) { // O(1)
            SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>> entry
                    = this.getNode(key); // O(1)

            Value prevValue = entry.element.value; // O(1)
            entry.element.value = value; // O(1)
            return prevValue; // O(1)
        }

        if (this.kvStore.size() >= this.capacity) // O(1)
            this.kvStore.remove(this.entryList.removeLast().key); // O(1)

        this.entryList.addFirst(new SimpleEntry<>(key, value)); // O(1)
        SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>> oldEntry
                = this.kvStore.put(key, this.entryList.head); // O(1)

        return oldEntry != null ? oldEntry.element.value : null; // O(1)
    }

    /**
     * Gets the value associated with the given key.
     * The operation has an expected runtime of O(1).
     *
     * @param key The search key of the element.
     * @return The value associated with the key; null if the key is not present in the buffer.
     */
    public Value get(Key key) { // O(1)
        SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>> result
                = this.getNode(key); // O(1)

        return result == null ? null : result.element.value; // O(1)
    }

    private SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>> getNode(Key key) {
        SimpleDoubleLinkedList.Node<SimpleEntry<Key, Value>> node
                = this.kvStore.get(key); // O(1)

        if (node == null) // O(1)
            return null; // O(1)

        if (node.next != null) // O(1)
            node.next.prev = node.prev; // O(1)
        else
            this.entryList.tail = node.prev; // O(1)

        if (node.prev != null) // O(1)
            node.prev.next = node.next; // O(1)
        else
            this.entryList.head = node.next; // O(1)

        this.entryList.addFirst(node.element); // O(1)
        this.kvStore.put(key, this.entryList.head); // O(1)
        return node; // O(1)
    }

    @Override
    public String toString() {
        return this.entryList.toString();
    }

    public static void main(String[] args) {
        // 3) a)
        /*
            put(A): (Head) A (Tail)
            put(H): (Head) H <--> A (Tail)
            put(J): (Head) J <--> H <--> A (Tail)
            put(A): (Head) A <--> J <--> H (Tail)
            put(E): (Head) E <--> A <--> J <--> H (Tail)
            put(W): (Head) W <--> E <--> A <--> J <--> H (Tail)
            put(P): (Head) P <--> W <--> E <--> A <--> J (Tail)
            put(A): (Head) A <--> P <--> W <--> E <--> J (Tail)
         */
        LRUBuffer<Character, Character> buff = new LRUBuffer<>(5);
        Arrays.asList('A', 'H', 'J', 'A', 'E', 'W', 'P', 'A')
                .forEach(c -> {
                    buff.put(c, c);
                    System.out.println("put(" + c + "): " + buff);
                });
    }
}
