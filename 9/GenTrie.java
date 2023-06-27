public class GenTrie {
    private final GenTrie[] children;
    private boolean end;

    public GenTrie() {
        this.children = new GenTrie[4];
        this.end = false;
    }

    /**
     * Translates 'A' -> 0, 'B' -> 1, 'C' -> 2, 'T' -> 3
     */
    private static int charToIndex(char c) {
        switch (c) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            default:
                throw new IllegalStateException("Unexpected value: " + c);
        }
    }

    /**
     * Translates 0 -> 'A', 1 -> 'C', 2 -> 'G', 3 -> 'T'
     */
    private static char indexToChar(int index) {
        char[] chars = {'A', 'C', 'G', 'T'};
        return chars[index];
    }

    private void insertImpl(String seq) {
        if (seq.length() == 0) {
            end = true;
        } else {
            int index = charToIndex(seq.charAt(0));
            if (children[index] == null) {
                children[index] = new GenTrie();
            }
            children[index].insertImpl(seq.substring(1));
        }
    }

    /**
     * Insert a gene sequence into this trie.
     *
     * @param seq The gene sequence.
     */
    public void insert(String seq) {
        if (seq.length() == 0) {
            throw new IllegalArgumentException("cannot insert an empty sequence");
        }
        insertImpl(seq);
    }

    /**
     * Check if this trie contains the given gene sequence.
     *
     * @param seq The gene sequence
     * @return true, if this trie contains seq
     */
    public boolean contains(String seq) {
        if (seq.length() == 0) {
            return end;
        } else {
            GenTrie child = children[charToIndex(seq.charAt(0))];
            return child != null && child.contains(seq.substring(1));
        }
    }

    /**
     * @return true, if after deletion the node has no more children and should be pruned by the parent
     */
    public boolean removeImpl(String seq) {
        if (seq.length() == 0) {
            if (end) {
                end = false;
                return isEmpty();
            }
        } else {
            int index = charToIndex(seq.charAt(0));
            GenTrie child = children[index];
            if (child != null && child.removeImpl(seq.substring(1))) {
                children[index] = null;
                return isEmpty();
            }
        }
        return false;
    }

    /**
     * Remove a gene sequence from the trie.
     *
     * @param seq The sequence.
     */
    public void remove(String seq) {
        removeImpl(seq);
    }

    /**
     * Check if a node is empty, i.e. it has no children and no end-marker, and should be deleted in the parent.
     *
     * @return true, if the node is empty.
     */
    private boolean isEmpty() {
        if (end) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (children[i] != null) {
                return false;
            }
        }
        return true;
    }

    private void printImpl(String prefix) {
        if (end) {
            System.out.println(prefix);
        }
        for (int i = 0; i < 4; i++) {
            GenTrie child = children[i];
            if (child != null) {
                child.printImpl(prefix + indexToChar(i));
            }
        }
    }

    /**
     * Print all contained gene sequences in lexicographical order.
     */
    public void print() {
        printImpl("");
    }

    public static void main(String[] args) {
        // Note: assertions are only enabled if `-ea` is passed to java

        String[] sequences = {"AA", "AACG", "ACG", "ACT", "AG", "ATA", "ATCC", "ATCG", "ATG", "ATT", "CG", "CT", "G", "GTA", "TAG", "TAT", "TC"};

        GenTrie t = new GenTrie();

        for (var s : sequences) {
            t.insert(s);
        }

        for (var s : sequences) {
            assert t.contains(s);
        }

        t.print();

        t.remove(sequences[0]);
        t.remove(sequences[1]);
        assert !t.contains(sequences[0]);
        assert !t.contains(sequences[1]);

        System.out.println();
        t.print();

        for (var s : sequences) {
            t.remove(s);
        }

        assert t.isEmpty();
    }
}