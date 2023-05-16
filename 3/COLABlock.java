package cola;

import java.util.Arrays;

public class COLABlock<E extends Comparable<E>> {

    private E[] elements;

    public COLABlock(int size) {
        elements = (E[]) new Comparable[size];
    }

    /**
     * Searches for an element equal (i.e. compareTo == 0) to the given element in this block.
     *
     * @param element The element to search for.
     * @return The element in the data structure; null if non-existent.
     */
    public E search(E element) {
        int pos = Arrays.binarySearch(elements, element);
        if (pos < 0)
            return null;
        return elements[pos];
    }

    /**
     * Sets the element at the given index.
     *
     * @param index   The index the element should be stored at
     * @param element The element
     */
    public void set(int index, E element) {
        elements[index] = element;
    }

    /**
     * Returns the size of the block.
     *
     * @return The size
     */
    public int getSize() {
        return elements.length;
    }

    /**
     * Merges mergeBlock into this COLABlock.
     * @param mergeBlock
     */
    public void merge(COLABlock<E> mergeBlock) {
        E[] result = (E[]) new Comparable[elements.length * 2];
        int resultPointer = 0;

        E[] leftArray = this.elements;
        int leftPointer = 0;
        E[] rightArray = mergeBlock.elements;
        int rightPointer = 0;

        while (leftPointer < leftArray.length && rightPointer < rightArray.length) {
            if (leftArray[leftPointer].compareTo(rightArray[rightPointer]) <= 0)
                result[resultPointer++] = leftArray[leftPointer++];
            else {
                result[resultPointer++] = rightArray[rightPointer++];
            }
        }
        while (leftPointer < leftArray.length)
            result[resultPointer++] = leftArray[leftPointer++];
        while (rightPointer < rightArray.length)
            result[resultPointer++] = rightArray[rightPointer++];

        elements = result;
    }

}
