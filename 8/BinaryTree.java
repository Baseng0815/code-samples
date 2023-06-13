package zettel8;

/**
 * This class implements a binary tree
 *
 * @param <K>
 */
public class BinaryTree<K> {
    protected BinaryTree<K> left, right;
    protected K key;

    /**
     * creates a new instance
     *
     * @param left  the left subtree
     * @param key   the key of the tree node
     * @param right the right subtree
     */
    public BinaryTree(BinaryTree<K> left, K key, BinaryTree<K> right) {
        this.left = left;
        this.key = key;
        this.right = right;
    }

    /**
     * returns the left subtree
     */
    public BinaryTree<K> getLeft() {
        return left;
    }

    /**
     * returns the right subtree
     */
    public BinaryTree<K> getRight() {
        return right;
    }

    /**
     * returns the key
     */
    public K getKey() {
        return key;
    }

}