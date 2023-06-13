package zettel8;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BinarySearchTree<K extends Comparable<? super K>> extends BinaryTree<K> {
    /**
     * creates a new instance
     *
     * @param left  the left subtree
     * @param key   the key of the tree node
     * @param right the right subtree
     */
    public BinarySearchTree(BinarySearchTree<K> left, K key, BinarySearchTree<K> right) {
        super(left, key, right);
    }

    public BinarySearchTree(K key) {
        this(null, key, null);
    }

    /**
     * adds a key to the tree
     *
     * @param key the key of the tree node
     */
    public void add(K key) {
        if (key == null)
            return;

        if (key.compareTo(super.key) <= 0) {
            if (super.left != null)
                this.getLeft().add(key);
            else
                super.left = new BinarySearchTree<>(key);
        } else if (super.right != null)
            this.getRight().add(key);
        else
            super.right = new BinarySearchTree<>(key);
    }

    /**
     * searches a key in the tree
     *
     * @param key the key of the tree node
     * @return subtree where key is found or null
     */
    public BinarySearchTree<K> search(K key) {
        if (key == null)
            return null;

        int cmp = key.compareTo(super.key);
        if (cmp == 0)
            return this;

        if (cmp < 0)
            return this.getLeft().search(key);

        return this.getRight().search(key);
    }

    /**
     * deletes a key from tree
     *
     * @param key the key of the tree node
     * @return true if found and deleted, false otherwise
     */
    public boolean remove(K key) {
        BinarySearchTree<K> removeParent = this.getParent(key);
        if (removeParent == null)
            return false;

        assert removeParent.left != null || removeParent.right != null;

        BinarySearchTree<K> toDelete = removeParent.left != null && removeParent.left.key.equals(key) ?
                removeParent.getLeft() :
                removeParent.getRight();

        if (toDelete.left == null && toDelete.right == null) { // isLeaf
            if (removeParent.left == toDelete)
                removeParent.left = null;
            else
                removeParent.right = null;

            return true;
        }

        toDelete.key = this.removeSymmetricPredecessor(toDelete);
        return true;
    }

    public boolean contains(K key) {
        return this.search(key) != null;
    }

    private BinarySearchTree<K> getParent(K key) {
        BinarySearchTree<K> parent = null;
        BinarySearchTree<K> curr = this;

        int cmp;
        while (curr != null) {
            cmp = key.compareTo(curr.key);
            if (cmp == 0)
                return parent;

            parent = curr;
            curr = cmp < 0 ? curr.getLeft() : curr.getRight();
        }

        return null;
    }

    private K removeSymmetricPredecessor(BinarySearchTree<K> toDelete) {
        BinarySearchTree<K> successorOrPredecessor = toDelete.getPredecessor();
        assert successorOrPredecessor != null;
        BinarySearchTree<K> parentSuccessorOrPredecessor = this.getParent(successorOrPredecessor.key);
        assert parentSuccessorOrPredecessor != null;

        K key = successorOrPredecessor.key;
        parentSuccessorOrPredecessor.right = successorOrPredecessor.left;

        return key;
    }


    public BinarySearchTree<K> getSuccessor(BinarySearchTree<K> node) {
        return node != null ? node.getSuccessor() : null;
    }

    public BinarySearchTree<K> getPredecessor(BinarySearchTree<K> node) {
        return node != null ? node.getPredecessor() : null;
    }

    private BinarySearchTree<K> getSuccessor() {
        return super.right != null ? this.getRight().getMin() : null;
    }

    private BinarySearchTree<K> getPredecessor() {
        return super.left != null ? this.getLeft().getMax() : null;
    }

    public K getMaxKey(K x) {
        BinarySearchTree<K> search = this.search(x);
        search = search != null ? search.getPredecessor() : null;
        return search != null ? search.key : null;
    }

    public K getMaxKey() {
        BinarySearchTree<K> maxNode = this.getMax();
        return maxNode != null ? maxNode.key : null;
    }

    public K getMinKey() {
        BinarySearchTree<K> minNode = this.getMin();
        return minNode != null ? minNode.key : null;
    }

    private BinarySearchTree<K> getMax() {
        return super.right != null ? this.getRight().getMax() : this;
    }

    private BinarySearchTree<K> getMin() {
        return super.left != null ? this.getLeft().getMin() : this;
    }

    /**
     * returns the left subtree
     */
    @Override
    public BinarySearchTree<K> getLeft() {
        return (BinarySearchTree<K>) super.getLeft();
    }

    /**
     * returns the right subtree
     */
    @Override
    public BinarySearchTree<K> getRight() {
        return (BinarySearchTree<K>) super.getRight();
    }

    public List<K> inOrder() {
        List<K> list = new LinkedList<>();
        if (this.left != null)
            list.addAll(this.getLeft().inOrder());

        list.add(this.key);
        if (this.right != null)
            list.addAll(this.getRight().inOrder());

        return list;
    }

    public static void main(String[] args) {
        final int NUM_KEYS = 1000;

        BinarySearchTree<Integer> tree = new BinarySearchTree<>(NUM_KEYS / 2);
        List<Integer> list = IntStream.rangeClosed(1, NUM_KEYS).boxed().collect(Collectors.toList());
        list.remove((Object) (NUM_KEYS / 2));
        Collections.shuffle(list);

        list.forEach(tree::add);
        assert tree.inOrder().stream().sorted().collect(Collectors.toList()).equals(tree.inOrder());

        Random rnd = new Random();
        List<Integer> removeList = Stream.generate(() -> rnd.nextInt(NUM_KEYS))
                .distinct()
                .limit(NUM_KEYS / 2)
                .collect(Collectors.toList());

        assert removeList.stream().allMatch(tree::remove);
        assert removeList.stream().noneMatch(tree::contains);
        assert tree.inOrder().stream().sorted().collect(Collectors.toList()).equals(tree.inOrder());

        list.removeAll(removeList);
        assert list.stream().allMatch(tree::contains);

        int breakpoint = 123;
    }
}
