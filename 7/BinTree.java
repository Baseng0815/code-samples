package zettel7;

import java.util.LinkedList;
import java.util.List;

public class BinTree<T> {

    protected BinTree<T> left, right;
    protected T key;

    public BinTree(BinTree<T> l, T k, BinTree<T> r) {
        this.left = l;
        this.key = k;
        this.right = r;
    }

    public BinTree<T> getLeft() {
        return left;
    }

    public BinTree<T> getRight() {
        return right;
    }

    public T getKey() {
        return key;
    }

    public boolean isEmpty() {
        return ((left == null) && (right == null) && (key == null));
    }

    public void inOrder() {
        if (isEmpty())
            return;

        if (left != null)
            left.inOrder();
        System.out.println(key);
        if (right != null)
            right.inOrder();
    }

    public void postOrder() {
        if (isEmpty())
            return;

        if (left != null)
            left.inOrder();
        if (right != null)
            right.inOrder();
        System.out.println(key);
    }

    public void preOrder() {
        if (isEmpty())
            return;

        System.out.println(key);
        if (left != null)
            left.inOrder();
        if (right != null)
            right.inOrder();
    }

    public void levelOrder() {
        if (isEmpty())
            return;

        List<BinTree<T>> list = new LinkedList<>();

        list.add(this);

        while (!list.isEmpty()) {
            BinTree<T> remove = list.remove(0);
            System.out.println(remove.getKey());

            if (remove.left != null) {
                list.add(remove.left);
            }
            if (remove.right != null) {
                list.add(remove.right);
            }
        }
    }

    public static void main(String[] args) {
        BinTree<String> tree = new BinTree<String>(
                new BinTree<String>(
                        new BinTree<String>(
                                new BinTree<String>(null, "e", null),
                                "f",
                                null),
                        "a",
                        new BinTree<String>(null, "c", null)),
                "g",
                new BinTree<String>(
                        new BinTree<String>(
                                null,
                                "h",
                                new BinTree<String>(null, "b", null)),
                        "d",
                        null));
        System.out.println("in-order: ");
        tree.inOrder();
        System.out.println("==");
        tree.levelOrder();
    }

}