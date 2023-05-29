package zettel5;

public class SimpleEntry<Key, Value> {
    Key key;
    Value value;
    boolean deleted;

    public SimpleEntry(Key key, Value value) {
        this.value = value;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleEntry<?, ?> entry = (SimpleEntry<?, ?>) o;
        return this.key.equals(entry.key);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
