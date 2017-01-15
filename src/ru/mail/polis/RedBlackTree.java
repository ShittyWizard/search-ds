package ru.mail.polis;

import java.util.*;

import static ru.mail.polis.RedBlackTree.Color.BLACK;
import static ru.mail.polis.RedBlackTree.Color.RED;

public class RedBlackTree<E extends Comparable<E>> implements ISortedSet<E> {

    enum Color {RED, BLACK}

    private final Node NIL = new Node(null);
    private int size;
    private Node root = NIL;
    private Comparator<E> comparator;

    public RedBlackTree() {
        this.comparator = null;
    }

    public RedBlackTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private int compare(E v1, E v2) {
        return comparator == null ? v1.compareTo(v2) : comparator.compare(v1, v2);
    }

    private void rightRotate(Node n) {
        if (n.parent != NIL) {
            if (n == n.parent.left) {
                n.parent.left = n.left;
            } else {
                n.parent.right = n.left;
            }
            n.left.parent = n.parent;
            n.parent = n.left;
            if (n.left.right != NIL) {
                n.left.right.parent = n;
            }
            n.left = n.left.right;
            n.parent.right = n;
            return;
        }
        Node rootLeft = root.left;
        root.left = root.left.right;
        rootLeft.right.parent = root;
        root.parent = rootLeft;
        rootLeft.right = root;
        rootLeft.parent = NIL;
        root = rootLeft;
    }

    private void leftRotate(Node n) {
        if (n.parent != NIL) {
            if (n == n.parent.left) {
                n.parent.left = n.right;
            } else {
                n.parent.right = n.right;
            }
            n.right.parent = n.parent;
            n.parent = n.right;
            if (n.right.left != NIL) {
                n.right.left.parent = n;
            }
            n.right = n.right.left;
            n.parent.left = n;
            return;
        }
        Node rootRight = root.right;
        root.right = rootRight.left;
        rootRight.left.parent = root;
        root.parent = rootRight;
        rootRight.left = root;
        rootRight.parent = NIL;
        root = rootRight;
    }

    @Override
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        return minimum(root).key;
    }

    private Node minimum(Node x) {
        while (!x.left.equals(NIL)) {
            x = x.left;
        }
        return x;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        return maximum(root).key;
    }

    private Node maximum(Node x) {
        while(!x.right.equals(NIL)){
            x = x.right;
        }
        return x;
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
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (root != NIL) {
            Node curr = root;
            while (curr != NIL) {
                int cmp = compare(curr.key, value);
                if (cmp == 0) {
                    return true;
                } else if (cmp < 0) {
                    curr = curr.right;
                } else {
                    curr = curr.left;
                }
            }
        }
        return false;
    }

    @Override
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException("Null value");
        }
        boolean res;
        Node nodeToAdd = new Node(value);
        Node compNode = root;
        if (root != NIL) {
            while (true) {
                if (compare(nodeToAdd.key, compNode.key) < 0) {
                    if (compNode.left == NIL) {
                        compNode.left = nodeToAdd;
                        nodeToAdd.parent = compNode;
                        nodeToAdd.color = RED;
                        res = true;
                        break;
                    } else {
                        compNode = compNode.left;
                    }
                } else if (compare(nodeToAdd.key, compNode.key) > 0) {
                    if (compNode.right == NIL) {
                        compNode.right = nodeToAdd;
                        nodeToAdd.parent = compNode;
                        nodeToAdd.color = RED;
                        res = true;
                        break;
                    } else {
                        compNode = compNode.right;
                    }
                } else {
                    res = false;
                    break;
                }
            }
            if(res)
                fixAdd(nodeToAdd);
        } else {
            root = nodeToAdd;
            nodeToAdd.color = BLACK;
            nodeToAdd.parent = NIL;
            res = true;
        }
        if(res)
            size++;
        return res;
    }

    private void fixAdd(Node node) {
        while (node.parent.color != BLACK) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == RED && uncle != NIL) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right){
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rightRotate(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == RED && uncle != NIL) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left){
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    leftRotate(node.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }


    @Override
    public boolean remove(E value) {
        if (value == null) {
            throw new NullPointerException();
        }
        Node nodeForRemove, n, k;
        nodeForRemove = nodeForRemove(value, root);
        if(nodeForRemove == NIL){
            return false;
        }
        boolean isFixNeeded = nodeForRemove.color == BLACK;

        if(nodeForRemove.left == NIL) {
            n = nodeForRemove.right;
            performTransplant(nodeForRemove, nodeForRemove.right);
        } else if(nodeForRemove.right == NIL){
            n = nodeForRemove.left;
            performTransplant(nodeForRemove, nodeForRemove.left);
        } else {
            Node tempRoot = nodeForRemove.right;
            while(tempRoot.left != NIL) {
                tempRoot = tempRoot.left;
            }
            k = tempRoot;
            isFixNeeded = k.color == BLACK;
            n = k.right;
            if(k.parent == nodeForRemove) {
                n.parent = k;
            } else {
                performTransplant(k, k.right);
                k.right = nodeForRemove.right;
                k.right.parent = k;
            }
            performTransplant(nodeForRemove, k);
            k.left = nodeForRemove.left;
            k.left.parent = k;
            k.color = nodeForRemove.color;
        }

        if(isFixNeeded){
            fixRemove(n);
        }

        size--;
        return true;
    }

    private void performTransplant(Node n, Node k) {
        if(n.parent == NIL) {
            root = k;
        } else if(n == n.parent.left) {
            n.parent.left = k;
        } else {
            n.parent.right = k;
        }
        k.parent = n.parent;
    }

    private void fixRemove(Node node) {
        while(node != root && node.color == BLACK) {
            if(node == node.parent.left) {
                Node w = node.parent.right;
                if(w.color == RED) {
                    w.color = BLACK;
                    node.parent.color = RED;
                    leftRotate(node.parent);
                    w = node.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    node = node.parent;
                    continue;
                } else if(w.right.color == BLACK) {
                    w.left.color = BLACK;
                    w.color = RED;
                    rightRotate(w);
                    w = node.parent.right;
                }
                if(w.right.color == RED) {
                    w.color = node.parent.color;
                    node.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(node.parent);
                    node = root;
                }
            } else {
                Node w = node.parent.left;
                if(w.color == RED) {
                    w.color = BLACK;
                    node.parent.color = RED;
                    rightRotate(node.parent);
                    w = node.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    node = node.parent;
                    continue;
                } else if(w.left.color == BLACK) {
                    w.right.color = BLACK;
                    w.color = RED;
                    leftRotate(w);
                    w = node.parent.left;
                }
                if(w.left.color == RED) {
                    w.color = node.parent.color;
                    node.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.color = BLACK;
    }

    private Node nodeForRemove(E value, Node n) {
        if (isEmpty()) {
            return NIL;
        }
        if (compare(value, n.key) < 0) {
            if (n.left != NIL) {
                return nodeForRemove(value, n.left);
            }
        } else if (compare(value, n.key) > 0) {
            if (n.right != NIL) {
                return nodeForRemove(value, n.right);
            }
        } else {
            return n;
        }
        return NIL;
    }

    class Node {

        Node(E value) {
            this.key = value;
        }

        E key;
        Node left = NIL;
        Node right = NIL;
        Node parent = NIL;
        Color color = BLACK;
    }
}
