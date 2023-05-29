package zettel5;

import java.util.function.Function;

abstract class AbstractHashTable<Key, Value> implements Hashing<Key, Value> {
    private final SimpleEntry<Key, Value>[] table;
    private final Function<Key, Integer> hashFunction;

    AbstractHashTable(int capacity, Function<Key, Integer> hashFunction) {
        this.table = (SimpleEntry<Key, Value>[]) new SimpleEntry[capacity];
        this.hashFunction = hashFunction;
    }

    abstract int probe(Key key, int probeNumber);

    final SimpleEntry<Key, Value> getEntryAt(int index) {
        return this.table[index];
    }

    final int hash(Key key) {
        return this.hashFunction.apply(key);
    }

    final int capacity() {
        return this.table.length;
    }

    final EntryState getEntryStateAt(int index) {
        return getEntryStateOf(this.table[index]);
    }

    final EntryState getEntryStateOf(SimpleEntry<Key, Value> entry) {
        if (entry == null)
            return EntryState.NotPresent;

        if (entry.deleted)
            return EntryState.Deleted;

        return EntryState.Present;
    }

    final SimpleEntry<Key, Value>[] getTable() {
        return this.table;
    }

    @Override
    public Value put(Key key, Value value) {
        int index, probeNumber;
        for (probeNumber = 0, index = this.probe(key, probeNumber);
             probeNumber < this.capacity() && this.getEntryStateAt(index) == EntryState.Present;
             index++, probeNumber++)
            ;

        if (probeNumber >= this.capacity())
            throw new IllegalStateException("HashTable full");

        Value oldValue = null;
        if (this.getEntryStateAt(index) == EntryState.Present)
            oldValue = this.table[index].value;

        this.table[index] = new SimpleEntry<>(key, value);
        return oldValue;
    }
}
