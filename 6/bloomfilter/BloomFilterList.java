package zettel6.bloomfilter;

import zettel6.list.List;

import java.util.Iterator;

public class BloomFilterList<E> implements List<E> {
    private final List<E> list;
    private final BloomFilter<E> bloomFilter;

    public BloomFilterList(List<E> list, BloomFilter<E> bloomFilter) {
        this.list = list;
        this.bloomFilter = bloomFilter;
    }

    @Override
    public void add(E element) throws IndexOutOfBoundsException {
        this.list.add(element);
        this.bloomFilter.add(element);
    }

    /*
    * This is *very* expensive and definitely should not be done on every removal
    */
    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        E oldElement = this.list.remove(index);
        this.bloomFilter.empty();
        for (E ele : this.list)
            this.bloomFilter.add(ele);

        return oldElement;
    }

    @Override
    public boolean contains(E element) {
        return this.bloomFilter.contains(element) && this.list.contains(element);
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        return this.list.get(index);
    }

    @Override
    public int getSize() {
        return this.list.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return this.list.iterator();
    }
}