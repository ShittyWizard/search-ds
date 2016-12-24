package ru.mail.polis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class AVLTree<E extends Comparable<E>> implements ISortedSet<E> {

    private Node root;
    private int size;
    private final Comparator<E> comparator;

    public AVLTree() {
        this.comparator = null;
    }

    public AVLTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("No first element, Set is empty");
        }
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("No last element, Set is empty");
        }
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.key;
    }

    @Override
    public List<E> inorderTraverse() {
        List<E> result = new ArrayList<>();
        inorderTraverse(root, result);
        return result;
    }

    private void inorderTraverse(Node node, List<E> list) {
        if (node == null) {
            return;
        }
        inorderTraverse(node.left, list);
        list.add(node.key);
        inorderTraverse(node.right, list);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return FindP(value, root);
    }

    private boolean FindP(E key, Node node) {
        if (node == null) {
            return false;
        }
        if (compare(key, node.key) == 0) {
            return true;
        }
        if (compare(key, node.key) == -1) {
            return FindP(key, node.left);
        } else {
            return FindP(key, node.right);
        }
    }

    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (this.contains(value)) {
            return false;
        }
        if (root == null) {
            root = new Node(value);
        } else {
            addP(value, root);
        }
        size++;
        return true;
    }

    private Node addP(E key, Node r) {
        if (r == null) {
            return new Node(key);
        } else {
            if (compare(key, r.key) == -1) { //key<r.key
                r.left = addP(key, r.left);
            } else {
                r.right = addP(key, r.right);
            }
        }
        Node f = balance(r);
        if (r.equals(root)) {
            root = f;
        }
        return f;
    }

    private Node rotateRight(Node node) {
        Node q = node.left;
        node.left = q.right;
        q.right = node;
        node.fixHeight();
        q.fixHeight();
        return q;
    }

    private Node rotateLeft(Node node) {
        Node p = node.right;
        node.right = p.left;
        p.left = node;
        node.fixHeight();
        p.fixHeight();
        return p;
    }

    private Node balance(Node node) {
        node.fixHeight();
        if (node.bFactor() == 2) {
            if (node.right.bFactor() < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        if (node.bFactor() == -2) {
            if (node.left.bFactor() > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        return node;
    }

    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!this.contains(value)) {
            System.out.println("Removing node with value = "
                    + value
                    + ". Tree doesn't contains this node.");
            return false;
        }
        if (size == 1) {
            root = null;
        } else {
            deleteP(root, value);
            System.out.println("Removing node with value = "
                    + value
                    + ". Successful.");
        }
        size--;
        return true;
    }

    private Node deleteP(Node node, E key) {
        if (node == null) {
            return null;
        }


        if (compare(key, node.key) == -1) {             // key < node.key
            node.left = deleteP(node.left, key);
        } else {
            if (compare(key, node.key) == 1) {          // key > node.key
                node.right = deleteP(node.right, key);
            } else {                                    // key = node.key
                Node q = node.left;
                Node r = node.right;
                if (r == null) {
                    return q;
                }
                Node min = findMin(r);
                min.right = removeMin(r);
                min.left = q;
                Node f = balance(min);
                if (node.equals(root)) {
                    root = f;
                }
                return f;
            }
        }
        Node f = balance(node);
        if (node.equals(root)) {
            root = f;
        }
        return f;
    }

    private Node findMin(Node node) {
        return node.left != null ? findMin(node.left) : node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = removeMin(node.left);
        return balance(node);
    }


    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private class Node {
        private E key;
        private Node left;
        private Node right;
        private int height;

        Node(E key) {
            this.key = key;
            left = null;
            right = null;
            height = 1;
        }

        private int height() {
            return height;
        }

        private int bFactor() {
            int r;
            int l;
            if (right == null) {
                r = 0;
            } else {
                r = right.height();
            }
            if (left == null) {
                l = 0;
            } else {
                l = left.height();
            }
            return r - l;
        }

        void fixHeight() {
            int r;
            int l;
            if (right == null) {
                r = 0;
            } else {
                r = right.height();
            }
            if (left == null) {
                l = 0;
            } else {
                l = left.height();
            }
            height = (l > r ? l : r) + 1;
        }
    }


    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(1);
        tree.add(10);
        tree.add(4);
        tree.add(2);
        tree.remove(3); // check removing with value, which doesn't contains in tree
        tree.remove(4); // check removing with correct value
        System.out.println(tree.inorderTraverse().toString());
    }
}
