package zettel5;

import java.util.function.Function;
import java.util.stream.IntStream;

public class RobinHood<Key, Value> extends LinearProbing<Key, Value> {
    private final int[] probingInfo;

    public RobinHood(int capacity, Function<Key, Integer> hashFunction) {
        super(capacity, hashFunction);
        this.probingInfo = new int[capacity];
    }

    private Value putInternal(Key key, Value value, int probeNumber, int index, int probesCount) {
        if (probesCount > super.capacity())
            throw new IllegalStateException("Hashmap full");

        EntryState oldEntryState;
        while ((oldEntryState = this.getEntryStateAt(index)) == EntryState.Present &&
                this.probingInfo[index] >= probeNumber)
            index = super.probe(key, ++probeNumber);

        SimpleEntry<Key, Value> oldEntry = super.getEntryAt(index);
        int oldEntryProbingInfo = this.probingInfo[index];
        this.probingInfo[index] = probeNumber;

        if (oldEntryState != EntryState.Present) {
            super.getTable()[index] = new SimpleEntry<>(key, value);
            return null;
        }

        if (oldEntry.key.equals(key)) {
            Value oldValue = super.getTable()[index].value;
            super.getTable()[index] = new SimpleEntry<>(key, value);
            return oldValue;
        }

        super.getTable()[index] = new SimpleEntry<>(key, value);
        oldEntryProbingInfo++;

        return this.putInternal(
                oldEntry.key,
                oldEntry.value,
                oldEntryProbingInfo,
                super.probe(key, oldEntryProbingInfo),
                probesCount + 1);
    }

    @Override
    public final Value put(Key key, Value value) {
        return this.putInternal(
                key, value, 0, super.probe(key, 0), 0);
    }

    private SimpleEntry<SimpleEntry<Key, Value>, Integer> getEntryInfo(Key key) {
        int probeNumber = 0;
        int index = this.probe(key, probeNumber);

        for (SimpleEntry<Key, Value> find = super.getEntryAt(index);
             probeNumber < super.capacity() && super.getEntryStateOf(find) != EntryState.NotPresent;
             index = this.probe(key, ++probeNumber),
                     find = super.getEntryAt(index)) {
            if (!find.deleted && find.key.equals(key))
                return new SimpleEntry<>(find, index);
        }

        return null;
    }

    @Override
    public final Value remove(Key key) {
        SimpleEntry<SimpleEntry<Key, Value>, Integer> entryInfo
                = this.getEntryInfo(key);

        if (entryInfo == null)
            return null;

        for (int prevIndex = entryInfo.value, index = (entryInfo.value + 1) % super.capacity();
             super.getEntryStateAt(index) == EntryState.Present && this.probingInfo[index] > 0;
             prevIndex = index, index = (index + 1) % super.capacity()) {
            super.getTable()[prevIndex] = super.getEntryAt(index);
            super.getTable()[index] = null;
            this.probingInfo[prevIndex] = this.probingInfo[index] - 1;
            this.probingInfo[index] = 0;
        }

        return entryInfo.key.value;
    }

    public static void main(String[] args) {
        RobinHood<Integer, String> map = new RobinHood<>(100, k -> k * 2); //3 + 31
        IntStream.rangeClosed(1, 100).forEach(k -> map.put(k, k + ""));
        boolean checked = IntStream.rangeClosed(1, 100).allMatch(k -> map.get(k) != null);
        IntStream.rangeClosed(1, 50).forEach(map::remove);
        boolean checked2 = IntStream.rangeClosed(51, 100).allMatch(k -> map.get(k) != null);
        int breakpoint = 3;
    }
}
