import java.util.Iterator;
import java.util.Optional;

public class ListSet<E extends Comparable<E>> implements Set<E>, Iterable<E> {

    private static class SetNode<T> {
        private final T item;
        private SetNode<T> next;

        /**
         * constructor to build a node with no successor
         *
         * @param element the value to be stored by this node
         */
        private SetNode(T element) {
            item = element;
            next = null;
        }

        /**
         * constructor to build a node with specified (maybe null) successor
         *
         * @param element   the value to be stored by this node
         * @param reference the next field for this node
         */
        private SetNode(T element, SetNode<T> reference) {
            item = element;
            next = reference;
        }
    }

    // This is the start of the linked list representing this set.
    // If the set is empty, it is null
    private SetNode<E> head = null;

    @Override
    public boolean add(E element) {
        SetNode<E> current = head;
        // End of List
        if (current == null) {
            head = new SetNode<>(element, null);
            return true;
        }
        // Not the end, insert in a sorted manner
        int compare = element.compareTo(current.item);
        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            head = new SetNode<>(element, current);
            return true;
        } else {
            return add(current, element);
        }
    }

    /**
     * recursive private method, called by the public wrapper method
     *
     * @param prev    the previous element of this partial list
     * @param element the value to be added
     * @return true, if the value was added; false, otherwise.
     */
    private boolean add(SetNode<E> prev, E element) {
        SetNode<E> current = prev.next;
        // End of List
        if (current == null) {
            prev.next = new SetNode<>(element, null);
            return true;
        }
        // Not the end, insert in a sorted manner
        int compare = element.compareTo(current.item);
        if (compare == 0) {
            return false;
        } else if (compare < 0) {
            prev.next = new SetNode<>(element, current);
            return true;
        } else {
            return add(current, element);
        }
    }

    @Override
    public boolean remove(E element) {
        if (head == null)
            return false;
        if (head.item.compareTo(element) == 0) {
            head = head.next;
            return true;
        }
        return remove(head, element);
    }

    /**
     * recursive private method, called by the public wrapper method
     *
     * @param prev    the previous element of this partial list
     * @param element the value to be added
     * @return true, if the value was added; false, otherwise.
     */
    private boolean remove(SetNode<E> prev, E element) {
        SetNode<E> current = prev.next;
        //E nd of List
        if (current == null) {
            return false;
        }
        // Not the end, remove item or search recursively
        if (element.compareTo(current.item) == 0) {
            prev.next = current.next;
            return true;
        }
        return remove(current, element);
    }

    @Override
    public boolean contains(E element) {
        return contains(head, element);
    }

    /**
     * recursive private method, called by the public wrapper method
     *
     * @param node    the head of the remaining list, maybe null
     * @param element the value to be searched
     * @return search is succesful
     */
    private boolean contains(SetNode<E> node, E element) {
        if (node == null)
            return false;
        return node.item.compareTo(element) == 0 || contains(node.next, element);
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    private Optional<E> getOptional(Iterator<E> iterator) {
        return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty();
    }

    @Override
    public void union(Set<E> set) {
        if (set.isEmpty())
            return;

        Iterator<E> thisIterator = this.iterator();
        Iterator<E> otherIterator = set.iterator();

        SetNode<E> dummyHead = new SetNode<>(null, null);
        SetNode<E> curNode = dummyHead;

        Optional<E> thisElement = getOptional(thisIterator);
        Optional<E> otherElement = getOptional(otherIterator);

        while (thisElement.isPresent() && otherElement.isPresent()) {
            SetNode<E> nextNode;
            int compare = otherElement.get().compareTo(thisElement.get());
            if (compare == 0) {
                otherElement = getOptional(otherIterator);
            } else if (compare > 0) {
                nextNode = new SetNode<>(otherElement.get());
                curNode.next = nextNode;
                curNode = nextNode;
                otherElement = getOptional(otherIterator);
            } else {
                nextNode = new SetNode<>(thisElement.get());
                curNode.next = nextNode;
                curNode = nextNode;
                thisElement = getOptional(thisIterator);
            }
        }

        while (thisElement.isPresent()) {
            curNode.next = new SetNode<>(thisElement.get());
            curNode = curNode.next;
            thisElement = getOptional(thisIterator);
        }

        while (otherElement.isPresent()) {
            curNode.next = new SetNode<>(otherElement.get());
            curNode = curNode.next;
            otherElement = getOptional(otherIterator);
        }

        this.head = dummyHead.next;
    }

    @Override
    public void intersect(Set<E> set) {
        Iterator<E> iter = this.iterator();
        while (iter.hasNext()) {
            E next = iter.next();
            if (!set.contains(next)) {
                remove(next); // Be careful: Only works, because remove does not change the pointer of next.
            }
        }
    }

    @Override
    public void subtract(Set<E> set) {
        Iterator<E> iter = set.iterator();
        while (iter.hasNext()) {
            E next = iter.next();
            remove(next);
        }
    }

    /**
     * recursive private method, called by the public wrapper method
     *
     * @param node the head of the list (may be null if we are at the end)
     * @return the string representing the list
     */
    private String toString(SetNode<E> node) {
        if (node == null) {
            return "";
        }
        if (node.next == null) {
            return node.item.toString();
        }
        return node.item.toString() + " -> " + toString(node.next);
    }

    @Override
    public String toString() {
        return toString(head);
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            SetNode<E> node = head;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public E next() {
                E element = node.item;
                node = node.next;
                return element;
            }
        };
    }
}
