package ru.mail.polis;

import java.util.*;

import static ru.mail.polis.RedBlackTree.Color.BLACK;


public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    enum Color {RED, BLACK}

    private int size;
    private Node root;
    private Node NIL;
    private Comparator<E> comparator;

    public RedBlackTree() {
        this.comparator = null;
        NIL = new Node(BLACK, null, null, null, (E) new Integer(0));
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
        NIL = new Node(BLACK, null, null, null, (E) new Integer(0));
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
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                y = z.parent.parent.left;
                if (y.color == Color.RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
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
        while (curr.left != null) {
            curr = curr.left;
        }
        return minimum(root).key;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return maximum(root).key;
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
            root = new Node(BLACK, NIL, NIL, null, value);
            System.out.println("Root node created with value = " + value + ".");
        } else {
            insert(new Node(BLACK, null, null, null, value));
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
        if (value == null) {
            throw new NullPointerException();
        }
        Node currentNode = root;
        if (!contains(value)) {
            System.out.println("Node with this value = " + value + " does not contain.");
            return false;
        }
        while (currentNode != NIL && currentNode.key.compareTo(value) != 0) {
            if (currentNode.key.compareTo(value) < 0) {
                currentNode = currentNode.right;
            } else {
                currentNode = currentNode.left;
            }
        }
        Node Z = currentNode;
        Node Y;
        if (Z.left == NIL || Z.right == NIL){
            Y = Z;
        } else{
            Y = nextNode(Z);
        }
        Node X;
        if (Y.left != NIL){
            X = Y.left;
        } else{
            X = Y.right;
        }
        X.parent = Y.parent;
        if (Y.parent == null) {
            root = X;
        }  else if (Y == Y.parent.left) { //NPE
            Y.parent.left = X;
        } else {
            Y.parent.right = X;
        }
        if (Y != Z) {
            Z.key = Y.key;
        }
        if (Y.color == BLACK) {
            fixDelete(X);
        }
        System.out.println("Removing node with value = " + value + ". Successful.");
        size--;
        return true;
    }

    private Node nextNode(Node currentNode) {
        if (currentNode.right != NIL) {
            return minimum(currentNode.right);
        }
        Node tempNode = currentNode.parent;
        while (tempNode != NIL && currentNode != tempNode.right) {
            currentNode = tempNode;
            tempNode = tempNode.parent;
        }
        return tempNode;
    }

    private Node minimum(Node x) {
        while (!x.left.equals(NIL)) {
            x = x.left;
        }
        return x;
    }

    private Node maximum(Node x) {
        while(!x.right.equals(NIL)){
            x = x.right;
        }
        return x;
    }

    private void fixDelete(Node x) {
        Node w;
        while (!x.equals(root) && x.color == BLACK) {
            if (x.equals(x.parent.left)) {
                w = x.parent.right;
                if (w.color == Color.RED) {
                    w.color = BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                w = x.parent.left;
                if (w.color == Color.RED) {
                    w.color = BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
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

        for (int value = 0; value <= LEN; value++) {
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
