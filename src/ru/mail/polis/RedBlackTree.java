package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;


public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    private enum Color {RED, BLACK}

    private int size;
    private Node root;
    private Node NIL;
    private Comparator<E> comparator;

    public RedBlackTree() {
        this.comparator = null;
        NIL = new Node(Color.BLACK, null, null, null, (E) new Integer(0));
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
        NIL = new Node(Color.BLACK, null, null, null, (E) new Integer(0));
    }

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (!x.right.equals(NIL)) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else {
            if (y.equals(y.parent.left)) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        }
        x.right = y;
        y.parent = x;
    }

    private void insertFix(Node z) {
        Node y;
        while (z.parent != null && z.parent.color == Color.RED) {
            if (z.parent == z.parent.parent.left) {
                y = z.parent.parent.right;
                if (y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                y = z.parent.parent.left;
                if (y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (!y.left.equals(NIL)) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else {
            if (x.equals(x.parent.left)) {
                x.parent.left = y;
            } else {
                x.parent.right = y;
            }
        }
        y.left = x;
        x.parent = y;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        Node curr = root;
        while (curr.left != NIL) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node curr = root;
        while (curr.right != NIL) {
            curr = curr.right;
        }
        return curr.key;
    }


    public List<E> inorderTraverse() {
        List<E> result = new ArrayList<>();
        inorderTraverse(root, result);
        return result;
    }

    private void inorderTraverse(Node node, List<E> list) {
        if (node == null || node.equals(NIL)) {
            return;
        }
        inorderTraverse(node.left, list);
        list.add(node.key);
        inorderTraverse(node.right, list);
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E value) {
        return findP(value, root);
    }

    private boolean findP(E key, Node node) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (node == null || node.equals(NIL)) {
            return false;
        }
        if (key.equals(node.key)) {
            return true;
        }
        if (key.compareTo(node.key) < 0) {
            return findP(key, node.left);
        } else {
            return findP(key, node.right);
        }
    }

    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (this.contains(value)) {
            System.out.println("Tree contains node with value = " + value + ".");
            return false;
        }
        if (root == null) {
            root = new Node(Color.BLACK, NIL, NIL, null, value);
            System.out.println("Root node created with value = " + value + ".");
        } else {
            insert(new Node(Color.BLACK, null, null, null, value));
            System.out.println("Node with value = " + value + " added.");
        }
        size++;
        return true;
    }

    private void insert(Node z) {
        Node y = NIL;
        Node x = root;
        while (!x.equals(NIL)) {
            y = x;
            if (z.key.compareTo(x.key) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y.equals(NIL)) {
            root = z;
        } else {
            if (z.key.compareTo(y.key) < 0) {
                y.left = z;
            } else {
                y.right = z;
            }
        }
        z.left = NIL;
        z.right = NIL;
        z.color = Color.RED;
        insertFix(z);
    }

    @Override
    public boolean remove(E value) {
        if (root == null) {
            return false;
        }
        Node n = root;
        Node delNode = null;
        Node child = root;

        while (child != null) {
            n = child;
            int cmp = value.compareTo(n.key);
            child = cmp >= 0 ? n.right : n.left;
            if (cmp == 0) {
                delNode = n;
            }
        }
        if (delNode == null) {
            System.out.println("Removing node with value = "
                    + value
                    + ". Tree doesn't contains this node.");
            return false;
        } else {
            delNode.key = n.key;
            deleteP(delNode);
            System.out.println("Removing node with value = "
                    + value
                    + ". Successful.");
            size--;
            return true;
        }
    }

    private Node minimum(Node x) {
        while (!x.left.equals(NIL)) {
            x = x.left;
        }
        return x;
    }

    private Node successor(Node x) {
        Node y;
        if (!x.right.equals(NIL)) {
            return minimum(x.right);
        }
        y = x.parent;
        while (!y.equals(NIL) && x.equals(y.right)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    private void deleteP(Node z) {
        if (z == null) {
            throw new NullPointerException();
        }
        if (size==1){
            z=null;
            root=null;
            return;
        }
        Node x, y;
        if (z.left.equals(NIL) || z.right.equals(NIL)) {
            y = z;
        } else {
            y = successor(z);
        }
        if (!y.left.equals(NIL)) {
            x = y.left;
        } else {
            x = y.right;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else {
            if (y.equals(y.parent.left)) {
                y.parent.left = x;
            } else {
                y.parent.right = x;
            }
        }
        if (!y.equals(z)) {
            z.key = y.key;
        }
        if (y.color == Color.BLACK) {
            fixDelete(x);
        }
    }

    private void fixDelete(Node x) {
        Node w;
        while (!x.equals(root) && x.color == Color.BLACK) {
            if (x.equals(x.parent.left)) {
                w = x.parent.right;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Color.BLACK) {
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Color.BLACK) {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }

    public class Node {
        private Color color;
        private Node left;
        private Node right;
        private Node parent;
        private E key;

        private Node(Color color, Node left, Node right, Node parent, E key) {
            this.color = color;
            this.left = left;
            this.right = right;
            this.key = key;
            this.parent = parent;
        }

        private boolean equals(Node node) {
            return this.color == node.color
                    && this.left == node.left
                    && this.right == node.right
                    && this.parent == node.parent
                    && this.key.equals(node.key);
        }
    }

    public static void main(String[] args) {
        int LEN = 10;
        ISortedSet<Integer> set = new RedBlackTree<>();
        for (int value = 0; value < LEN; value++) {
            set.add(value);
        }
        System.out.println(set.size());
        for (int value = LEN; value >= 0; value--) {
            set.remove(value);
            System.out.println(set.inorderTraverse());
            System.out.println(set.size());
        }
    }
}
