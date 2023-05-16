package cola;

import java.util.NoSuchElementException;

public interface Query<E extends Comparable<E>> {

    /**
     * Searches the element with a top down algorithm, i.e.,
     * first looking in the smallest array, then the second smallest, ...,
     * and lastly in the largest array.
     *
     * @param element The element of the element
     * @return the instance of this element in the data structure;
     * @throws NoSuchElementException if the element if element is not present
     */
    E searchElement(E element) throws NoSuchElementException;
}
