import java.util.*;

public class TwoThreeFourTree<K extends Comparable<K>> {

    private static class SplitResult<K extends Comparable<K>> {
        Node<K> sibling;
        K key;

        SplitResult(Node<K> sibling, K key) {
            this.sibling = sibling;
            this.key = key;
        }
    }

    private static class Node<K extends Comparable<K>> {
        List<Node<K>> children;
        List<K> keys;

        Node() {
            children = new ArrayList<>();
            keys = new ArrayList<>();
        }

        boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * Check if a node is overflowing, i.e. it has 4 keys (and 5 children) - one too many. If so, it needs to be split.
         */
        boolean isOverFlow() {
            return keys.size() == 4;
        }

        /**
         * Splits a node and returns a tuple containing the new sibling and the key that is to be inserted in the parent
         *
         * @return the SplitResult
         */
        SplitResult<K> split() {
            Node<K> sibling = new Node<>();

            sibling.keys.add(this.keys.get(3));
            this.keys.remove(3);

            if (!isLeaf()) {
                sibling.children.add(this.children.get(3));
                sibling.children.add(this.children.get(4));
                this.children.remove(4);
                this.children.remove(3);
            }

            K key = this.keys.remove(2);

            return new SplitResult<>(sibling, key);
        }

        List<K> inOrder() {
            LinkedList<K> list = new LinkedList<>();
            if (!isLeaf()) {
                for (int k = 0; k < keys.size(); k++) {
                    list.addAll(children.get(k).inOrder());
                    list.add(keys.get(k));
                }
                list.addAll(children.get(children.size() - 1).inOrder());
            } else {
                list.addAll(keys);
            }

            return list;
        }

        void insert(K key) {
            int i = 0;
            for (; i < keys.size(); i++) {
                int cmp = key.compareTo(keys.get(i));
                if (cmp == 0) {
                    // already contained
                    return;
                }
                if (cmp < 0) {
                    break;
                }
            }

            if (isLeaf()) {
                keys.add(i, key);
            } else {
                children.get(i).insert(key);

                // Check for overflow in the child and fix it if necessary
                if (children.get(i).isOverFlow()) {
                    SplitResult<K> res = children.get(i).split();
                    children.add(i + 1, res.sibling);
                    keys.add(i, res.key);
                }

            }
        }

        boolean contains(K key) {
            int i = 0;
            for (; i < keys.size(); i++) {
                int cmp = key.compareTo(keys.get(i));
                if (cmp == 0) {
                    return true;
                } else if (cmp < 0) {
                    break;
                }
            }
            if (isLeaf()) {
                return false;
            }
            return children.get(i).contains(key);
        }

        @Override
        public String toString() {
            return keys.toString();
        }
    }

    private Node<K> root;

    public TwoThreeFourTree() {
        this.root = new Node<>();
    }

    /**
     * Insert a key into the tree.
     *
     * @param key The key to be inserted into the tree.
     */
    public void insert(K key) {
        root.insert(key);

        if (root.isOverFlow()) {
            // Split the root and create a new one
            SplitResult<K> res = root.split();
            Node<K> newRoot = new Node<>();
            newRoot.keys.add(res.key);
            newRoot.children.add(root);
            newRoot.children.add(res.sibling);
            root = newRoot;
        }
    }

    /**
     * Check if a key is contained in the tree.
     *
     * @param key The key.
     * @return true, if key is contained in the tree.
     */
    public boolean contains(K key) {
        return root.contains(key);
    }

    /**
     * Get the minimal element in the tree.
     *
     * @return The minimal element in the tree.
     */
    public K getMin() {
        if (root.keys.isEmpty()) {
            return null;
        }

        Node<K> node = root;
        while (!node.isLeaf()) {
            node = node.children.get(0);
        }

        return node.keys.get(0);
    }

    /**
     * Get the maximal element in the tree.
     *
     * @return The maximal element in the tree.
     */
    public K getMax() {
        if (root.keys.isEmpty()) {
            return null;
        }

        Node<K> node = root;
        while (!node.isLeaf()) {
            node = node.children.get(node.children.size() - 1);
        }

        return node.keys.get(node.keys.size() - 1);
    }

    /**
     * Get an ordered list of all elements in the tree.
     *
     * @return An ordered list of all elements in the tree
     */
    public List<K> inOrder() {
        return root.inOrder();
    }

    public static void main(String[] args) {
        // Note: assertions are only enabled if `-ea` is passed to java

        int num_inserts = 20;
        int num_generated = num_inserts * 2;

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num_generated; i++) {
            list.add(i);
        }
        Collections.shuffle(list);

        TwoThreeFourTree<Integer> tree = new TwoThreeFourTree<>();

        for (int i = 0; i < num_inserts; i++) {
            tree.insert(list.get(i));
        }

        for (int i = 0; i < num_inserts; i++) {
            assert tree.contains(list.get(i));
        }

        System.out.println(tree.inOrder());

        System.out.println("Min: " + tree.getMin());
        System.out.println("Max: " + tree.getMax());
    }
}