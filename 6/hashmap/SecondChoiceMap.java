package zettel6.hashmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a map that uses second choice hashing
 *
 * @param <K>
 * @param <V>
 */
public class SecondChoiceMap<K, V> implements Map<K, V> {
    private final List<List<MapEntry<K, V>>> table1;
    private final List<List<MapEntry<K, V>>> table2;
    private final HashFunction<K> h1;
    private final HashFunction<K> h2;
    public final int numBuckets;

    public SecondChoiceMap(int numBuckets, HashFunction<K> h1, HashFunction<K> h2) {
        this.numBuckets = numBuckets;
        this.h1 = h1;
        this.h2 = h2;
        this.table1 = new ArrayList<>();
        this.table2 = new ArrayList<>();
        for (int i = 0; i < numBuckets; i++) {
            this.table1.add(new LinkedList<>());
            this.table2.add(new LinkedList<>());
        }
    }

    private int getAddress(K key, HashFunction<K> h) {
        int hash = h.hash(key) % this.numBuckets;
        if (hash < 0)
            hash += this.numBuckets;

        return hash;
    }

    private V get(K key, List<List<MapEntry<K, V>>> table, HashFunction<K> hf) {
        for (MapEntry<?, V> e : table.get(getAddress(key, hf)))
            if (e.getKey().equals(key))
                return e.getValue();

        return null;
    }

    @Override
    public V get(K key) {
        V value = this.get(key, this.table1, this.h1);
        if (value == null)
            value = this.get(key, this.table2, this.h2);

        return value;
    }

    @Override
    public void put(K key, V value) {
        this.remove(key);
        int addr1 = this.getAddress(key, this.h1);
        int addr2 = this.getAddress(key, this.h2);

        if (this.table1.get(addr1).size() < this.table2.get(addr2).size())
            this.table1.get(addr1).add(new MapEntry<>(key, value));
        else
            this.table2.get(addr2).add(new MapEntry<>(key, value));
    }

    private boolean remove(K key, List<MapEntry<K, V>> bucket) {
        for (Iterator<MapEntry<K, V>> i = bucket.iterator(); i.hasNext(); ) {
            MapEntry<K, V> e = i.next();
            if (e.getKey().equals(key)) {
                i.remove();
                return true;
            }
        }

        return false;
    }

    @Override
    public void remove(K key) {
        if (!this.remove(key, this.table1.get(this.getAddress(key, this.h1))))
            this.remove(key, this.table2.get(this.getAddress(key, this.h2)));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Table1: [");

        this.table1.forEach(str::append);
        str.append("]\nTable2: [");
        this.table2.forEach(str::append);

        return str.append(']').toString();
    }
}
