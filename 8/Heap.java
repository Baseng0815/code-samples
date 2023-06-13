package zettel8;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * This class represents a binary heap
 *
 * @param <T> The heap's element type
 */
@SuppressWarnings("unchecked")
public class Heap<T> {
    /**
     * Constant: The initial capacity, if not set
     */
    public static int DEFAULT_CAPACITY = 16;
    /**
     * Holds the elements of this heap
     */
    private T[] elements;

    /**
     * The number of elements stored in this heap
     */
    private int number;

    /**
     * The element comparator
     */
    private final Comparator<T> comparator;

    /**
     * @param comparator Comparator for elements
     */
    public Heap(Comparator<T> comparator) {
        this.number = 0;
        this.elements = (T[]) new Object[DEFAULT_CAPACITY];
        this.comparator = comparator;
    }

    /**
     * Creates a heap from the given array. This method assumes, that the given
     * array is completely filled with elements
     *
     * @param values the values to fill this heap with
     * @param comp   Comparator for elements
     */
    public Heap(T[] values, Comparator<T> comp) {
        this.number = values.length;
        this.comparator = comp;
        // Compute the array length as the next power of 2 greater than the number of
        // elements
        int size = Integer.highestOneBit(values.length) << 1;
        this.elements = (T[]) new Object[size];
        System.arraycopy(values, 0, elements, 1, values.length);

        // ab Position n/2 in elements sind nur Blätter gespeichert, diese erfüllen die Heap-Eigenschaft
        for (int i = number / 2; i > 0; i--) {
            downheap(i);
        }
    }

    /**
     * @return the number of elements in this heap
     */
    public int size() {
        return number;
    }

    /**
     * @return true, if this heap contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return number == 0;
    }

    /**
     * Retrieves the top element of this heap, but does not remove it
     *
     * @return the top element of this heap
     */
    public T peek() {
        if (number == 0)
            throw new NoSuchElementException("Heap is empty");
        return elements[1];

    }

    /**
     * Adds the given elemet to this heap and restores the heap property
     *
     * @param elem the element to add
     */
    public void add(T elem) {
        number++;
        if (number == elements.length)
            resize();
        elements[number] = elem;
        upheap(number);
    }

    /**
     * Removes the top element of this heap (if any)
     */
    public void remove() {
        if (number == 0)
            return;
        elements[1] = elements[number];
        number--;
        downheap(1);

    }

    /**
     * Doubles the capacity of the storage array
     */

    private void resize() {
        T[] tmp = (T[]) new Comparable[elements.length * 2];
        System.arraycopy(elements, 0, tmp, 0, elements.length);
        elements = tmp;
    }

    /**
     * Bubbles the element at the given position up the heap into its correct position
     *
     * @param i the start position
     */
    private void upheap(int i) {
        if (i == 1 || comparator.compare(elements[i], elements[i / 2]) >= 0)
            return;
        else {
            elements[0] = elements[i];
            elements[i] = elements[i / 2];
            elements[i / 2] = elements[0];
            upheap(i / 2);
        }
    }

    /**
     * Bubbles the element at the given position down the heap into its correct position
     *
     * @param i the start position
     */
    private void downheap(int i) {
        // There is no smaller child
        if ((2 * i > number || comparator.compare(elements[i], elements[2 * i]) <= 0) &&
                (2 * i + 1 > number || comparator.compare(elements[i], elements[2 * i + 1]) <= 0))
            return;
            // There is a smaller child
        else {
            int index = 2 * i; // Try left child first
            if (index + 1 <= number && comparator.compare(elements[index], elements[index + 1]) > 0)
                index++; // Right child smaller than left

            // Swap and continue
            elements[0] = elements[i];
            elements[i] = elements[index];
            elements[index] = elements[0];
            downheap(index);
        }
    }

    /**
     * Finds the k-th smallest element using naive heap construction
     *
     * @param elems
     * @param k
     * @return
     */
    private static Integer findKSmallestNaive(Integer[] elems, int k) {
        if (k < 1)
            throw new IllegalArgumentException("k must be >= 1");

        Heap<Integer> h = new Heap<>(Integer::compare);
        for (Integer e : elems)
            h.add(e);

        for (int i = 1; i < k; i++)
            h.remove();

        return h.peek();
    }

    /**
     * Finds the k-th smallest element using efficient heap construction
     *
     * @param elems
     * @param k
     * @return
     */
    private static Integer findKSmallestHeapify(Integer[] elems, int k) {
        if (k < 1)
            throw new IllegalArgumentException("k must be >= 1");

        Heap<Integer> h = new Heap<>(elems, Integer::compare);

        for (int i = 1; i < k; i++)
            h.remove();

        return h.peek();
    }

    /**
     * Finds the k-th smalles element using a max heap
     *
     * @param elems
     * @param k
     * @return
     */
    private static Integer findKSmallestMaxHeap(Integer[] elems, int k) {
        if (k < 1)
            throw new IllegalArgumentException("k must be >= 1");

        Comparator<Integer> cmp = Integer::compare;
        cmp = cmp.reversed();

        Heap<Integer> h = new Heap<>(cmp);

        int i = 0;
        for (; i < k; i++) {
            h.add(elems[i]);
        }

        for (; i < elems.length; i++) {
            h.add(elems[i]);
            h.remove();
        }
        return h.peek();
    }


    public static void main(String[] args) {
        System.out.println("Building array.");
        final int NUM_VALUES = 100_000_000;
        Random rand = new Random(System.nanoTime());
        Integer[] values = new Integer[NUM_VALUES];

        for (int i = 0; i < NUM_VALUES; i++)
            values[i] = rand.nextInt(NUM_VALUES * 100);


        for (int k = 1; k < 10; k++) {
            System.out.println("K=" + k + ":");
            // Naive
            {
                long time = -System.currentTimeMillis();
                Integer v = findKSmallestNaive(values, k);
                time += System.currentTimeMillis();
                System.out.println("Naive (" + v + "): " + time);
            }

            // Heapify
            {
                long time = -System.currentTimeMillis();
                Integer v = findKSmallestHeapify(values, k);
                time += System.currentTimeMillis();
                System.out.println("Heapify (" + v + "): " + time);
            }

            // max heap
            {
                long time = -System.currentTimeMillis();
                Integer v = findKSmallestMaxHeap(values, k);
                time += System.currentTimeMillis();
                System.out.println("MaxHeap (" + v + "): " + time);
            }
        }
    }
}
