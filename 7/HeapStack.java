package zettel7;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class HeapStack<E> implements SimpleStack<E> {

    class Element<E> {

        private final E element;
        private final int insertion;

        Element(E element, int insertion) {
            this.element = element;
            this.insertion = insertion;
        }
    }

    private final int maximumCapacity;

    private int capacity = 0;
    private PriorityQueue<Element<E>> priorityQueue;

    public HeapStack(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        this.priorityQueue = new PriorityQueue<>((Comparator<Element<E>>)
                (o1, o2) -> Integer.compare(o2.insertion, o1.insertion)
        );
    }

    @Override
    public E pop() throws NoSuchElementException {
        if (priorityQueue.isEmpty())
            throw new NoSuchElementException();

        capacity--;
        return priorityQueue.poll().element;
    }

    @Override
    public void push(E element) {
        if (capacity > maximumCapacity) {
            throw new IllegalStateException("Maximum Capacity reached");
        }
        priorityQueue.add(new Element<>(element, capacity++));
    }

    public E peek() {
        if (priorityQueue.isEmpty())
            throw new NoSuchElementException();

        return priorityQueue.peek().element;
    }

    @Override
    public void clear() {
        priorityQueue.clear();
    }

    @Override
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    @Override
    public int capacity() {
        return maximumCapacity;
    }


}
