package zettel7;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class HeapQueue<E> implements SimpleFIFOQueue<E> {

    class Element<E> {

        private final E element;
        private final int insertion;

        Element(E element, int insertion) {
            this.element = element;
            this.insertion = insertion;
        }
    }

    private final int maximumCapacity;

    private int allInserts = 0;
    private int capacity = 0;
    private PriorityQueue<Element<E>> priorityQueue;

    public HeapQueue(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        this.priorityQueue = new PriorityQueue<>((Comparator<Element<E>>)
                (o1, o2) -> Integer.compare(o1.insertion, o2.insertion)
        );
    }


    @Override
    public E poll() throws NoSuchElementException {
        if (priorityQueue.isEmpty())
            throw new NoSuchElementException();

        capacity--;
        return priorityQueue.poll().element;
    }

    @Override
    public void add(E element) {
        if (capacity > maximumCapacity) {
            throw new IllegalStateException("Maximum Capacity reached");
        }
        capacity++;

        priorityQueue.add(new Element<>(element, allInserts++));


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
