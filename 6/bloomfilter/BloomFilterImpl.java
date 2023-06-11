package zettel6.bloomfilter;

import java.util.Arrays;

public class BloomFilterImpl<E> implements BloomFilter<E> {
    private final boolean[] bitsArray;
    private final HashFunction<E>[] hashFunctions;

    private final int m;

    public BloomFilterImpl(HashFunctionFactory<E> factory, int numberOfHashFunctions, int filterSize) {
        this.bitsArray = new boolean[filterSize];
        this.hashFunctions = factory.generate(numberOfHashFunctions);
        this.m = filterSize;
    }

    private static <E> int getAddress(E e, HashFunction<E> h, int m) {
        int hash = h.hash(e) % m;
        if (hash < 0)
            hash += m;

        return hash;
    }

    @Override
    public void add(E element) {
        for (HashFunction<E> hf : this.hashFunctions)
            this.bitsArray[getAddress(element, hf, m)] = true;
    }

    @Override
    public boolean contains(E element) {
        for (HashFunction<E> hf : this.hashFunctions)
            if (!this.bitsArray[getAddress(element, hf, m)])
                return false;

        return true;
    }

    @Override
    public void empty() {
        Arrays.fill(this.bitsArray, false);
    }
}
