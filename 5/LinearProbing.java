package zettel5;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinearProbing<Key, Value> extends AbstractHashTable<Key, Value> implements Remove<Key, Value> {
    public LinearProbing(int capacity, Function<Key, Integer> hashFunction) {
        super(capacity, hashFunction);
    }

    final int probe(Key key, int probeNumber) {
        return (super.hash(key) + probeNumber) % super.capacity();
    }

    private SimpleEntry<Key, Value> getEntry(Key key) {
        int probeNumber = 0;

        for (SimpleEntry<Key, Value> find = this.getEntryAt(this.probe(key, probeNumber));
             probeNumber < super.capacity() && super.getEntryStateOf(find) != EntryState.NotPresent;
             find = super.getEntryAt(this.probe(key, ++probeNumber))) {
            if (!find.deleted && find.key.equals(key))
                return find;
        }

        return null;
    }

    @Override
    public final Value get(Key key) {
        SimpleEntry<?, Value> entry = this.getEntry(key);
        return entry != null ? entry.value : null;
    }

    @Override
    public Value remove(Key key) {
        SimpleEntry<?, Value> entry = this.getEntry(key);
        if (entry != null) {
            entry.deleted = true;
            return entry.value;
        }

        return null;
    }

    public static void main(String[] args) {
        Hashing<Integer, String> map = new LinearProbing<>(100, i -> i * 3 + 13);
        List<Integer> keys = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());
        Collections.shuffle(keys);

        keys.forEach(key -> map.put(key, key.toString()));

        boolean checked = keys.stream().allMatch(key -> map.get(key).equals(key.toString()));

        String s = "";

    }
}
