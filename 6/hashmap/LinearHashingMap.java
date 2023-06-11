package zettel6.hashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a map that uses linear hashing to expand the underlying hash table
 *
 * @param <K>
 * @param <V>
 */
public class LinearHashingMap<K, V> implements Map<K, V> {
    private final List<List<MapEntry<K, V>>> hashTable;
    private final int m0;
    private final double alphaMax;

    private HashFunction<K> hashFunction_level;
    private int n;
    private int level;
    private int expansionPointer;

    public LinearHashingMap(int m0, double alphaMax, HashFunction<K> hashFunction) {
        assert m0 > 0;
        assert alphaMax > 0d;

        this.m0 = m0;
        this.alphaMax = alphaMax;
        this.hashFunction_level = hashFunction;

        this.level = 0;
        this.expansionPointer = 0;
        this.hashTable = new ArrayList<>();

        for (int i = 0; i < m0; i++)
            this.hashTable.add(new LinkedList<>());
    }

    private int hashRing(K key) {
        int index = this.hashFunction_level.hash(key) % this.hashTable.size();
        if (index < 0)
            index += this.hashTable.size();

        return index;
    }

    /**
     * get the address for the given key with respect to current level
     *
     * @param key
     * @return the address for the given key with respect to current level
     */
    public int getAddress(K key) {
        int index = this.hashRing(key);
        if (index < this.expansionPointer)
            return this.hashFunction_nextLevel(key);

        return index;
    }

    private int hashFunction_nextLevel(K key) {
        return (this.hashFunction_level.hash(key) + (int) Math.pow(2, this.level) * this.m0 - 1) %
                this.hashTable.size();
    }

    /**
     * get the current alpha value
     *
     * @return the current alpha value
     */
    public double getAlpha() {
        return (double) this.n / this.hashTable.size();
    }

    /**
     * check if number of elements in hash table exceeds threshold
     *
     * @return true if the hash table needs to be extended
     */
    public boolean checkOverflow() {
        return this.getAlpha() > this.alphaMax;
    }

    /**
     * expands the hash table
     */
    protected void split() {
        List<MapEntry<K, V>> newBucket = new LinkedList<>();
        List<MapEntry<K, V>> bucket = this.hashTable.get(this.expansionPointer);
        for (Iterator<MapEntry<K, V>> it = bucket.iterator(); it.hasNext(); ) {
            MapEntry<K, V> next = it.next();
            if (this.hashFunction_nextLevel(next.getKey()) != this.expansionPointer) {
                newBucket.add(next);
                it.remove();
            }
        }

        this.hashTable.add(newBucket);
        if (++this.expansionPointer > (int) Math.pow(2, this.level) * this.m0) {
            this.expansionPointer = 0;
            this.level++;

            HashFunction<K> oldHashFunctionRef = this.hashFunction_level;
            int offsetConstant = (int) Math.pow(2, this.level) * this.m0 - 1;
            this.hashFunction_level = k -> oldHashFunctionRef.hash(k) + offsetConstant;
        }
    }

    @Override
    public V get(K key) {
        List<MapEntry<K, V>> bucket = this.hashTable.get(this.getAddress(key));
        for (MapEntry<K, V> tuple : bucket) {
            if (tuple.getKey().equals(key)) {
                return tuple.getValue();
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        List<MapEntry<K, V>> bucket = this.hashTable.get(this.getAddress(key));
        for (MapEntry<K, V> tuple : bucket) {
            if (tuple.getKey().equals(key)) {
                tuple.setValue(value);
                return;
            }
        }

        bucket.add(new MapEntry<>(key, value));
        this.n++;

        if (this.checkOverflow())
            this.split();
    }

    @Override
    public void remove(K key) {
        List<MapEntry<K, V>> bucket = this.hashTable.get(this.getAddress(key));
        for (Iterator<MapEntry<K, V>> it = bucket.iterator(); it.hasNext(); ) {
            if (it.next().getKey().equals(key)) {
                it.remove();
                this.n--;
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Iterable<?> bucket : this.hashTable) {
            str.append('[');
            for (Object entry : bucket)
                str.append(entry);

            str.append(']');
        }

        return str.toString();
    }
}