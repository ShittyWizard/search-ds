package ru.mail.polis;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class RedBlackTree{

    private enum Color {RED, BLACK}

    private int size;
    private Node root;
    private Node NIL;

    public RedBlackTree() {
        NIL = new Node(Color.BLACK, null,null,null, 0);
    }

    private void rightRotate(Node y){
        Node x = y.left;
        y.left = x.right;
        if(!x.right.equals(NIL)){
            x.right.parent = y;
        }
        x.parent = y.parent;
        if(y.parent==null){
            root = x;
        }
        else{
            if(y.equals(y.parent.left)){
                y.parent.left = x;
            }
            else{
                y.parent.right = x;
            }
        }
        x.right = y;
        y.parent = x;
    }

    private void insertFix(Node z){
        Node y;
        while(z.parent!=null && z.parent.color==Color.RED){ //!=null(?)
            if(z.parent==z.parent.parent.left){
                y = z.parent.parent.right;
                if(y.color==Color.RED){
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                }
                else{
                    if(z==z.parent.right){
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }
            }
            else{
                y = z.parent.parent.left;
                if(y.color==Color.RED){
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                }
                else{
                    if(z==z.parent.left){
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

    private void leftRotate(Node x){
        Node y = x.right;
        x.right = y.left;
        if(!y.left.equals(NIL)){
            y.left.parent = x;
        }
        y.parent = x.parent;
        if(x.parent==null){
            root = y;
        }
        else{
            if(x.equals(x.parent.left)){
                x.parent.left = y;
            }
            else{
                x.parent.right = y;
            }
        }
        y.left = x;
        x.parent = y;
    }

    public int first() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no first element");
        }
        Node curr = root;
        while (curr.left != NIL) {
            curr = curr.left;
        }
        return curr.key;
    }


    public int last() {
        if (isEmpty()) {
            throw new NoSuchElementException("set is empty, no last element");
        }
        Node curr = root;
        while (curr.right != NIL) {
            curr = curr.right;
        }
        return curr.key;
    }


    public List inorderTraverse() {
        List result = new ArrayList<>();
        inorderTraverse(root,result);
        return result;
    }

    private void inorderTraverse(Node node, List list) {
        if (node==null || node.equals(NIL)) {
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
        return size==0;
    }


    public boolean contains(int value) {
        return FindP(value,root);
    }

    private boolean FindP(int key, Node node){
        if(node==null || node.equals(NIL)){
            return false;
        }
        if(key == node.key){
            return true;
        }
        if(key < node.key){
            return FindP(key,node.left);
        } else {
            return FindP(key, node.right);
        }
    }


    public boolean add(int value) {
        if(this.contains(value)){
            System.out.println("Tree contains node with value = " + value + ".");
            return false;
        }
        if(root==null){
            root = new Node(Color.BLACK,NIL,NIL,null,value);
            System.out.println("Root node created with value = " + value + ".");
        }
        else {
            insert(new Node(Color.BLACK, null, null, null, value));
            System.out.println("Node with value = " + value + " added.");
        }
        size++;
        return true;
    }

    private void insert(Node z){
        Node y = NIL;
        Node x = root;
        while(!x.equals(NIL)){
            y = x;
            if(z.key<x.key){
                x = x.left;
            }
            else{
                x = x.right;
            }
        }
        z.parent = y;
        if(y.equals(NIL)){
            root = z;
        }
        else{
            if(z.key<y.key){
                y.left = z;
            }
            else{
                y.right = z;
            }
        }
        z.left = NIL;
        z.right = NIL;
        z.color = Color.RED;
        insertFix(z);
    }


    public boolean remove(int value) {
        Node r = findNode(root,value);
        if(r==null){
            System.out.println("Removing node with value = "
                    + value
                    + ". Tree doesn't contains this node.");
            return false;
        }
        else{
            if(size==1){
                root = null;
            }
            else {
                deleteP(r);
            }
        }
        System.out.println("Removing node with value = "
                + value
                + ". Successful.");
        size--;
        return true;
    }

    private Node findNode(Node node, int key){
        if(node==null || node.equals(NIL)){
            return null;
        }
        if(node.key==key){
            return node;
        }
        if(node.key>key){
            return findNode(node.left,key);
        }
        else{
            return findNode(node.right, key);
        }
    }

    private Node minimum(Node x){
        while(!x.left.equals(NIL)){
            x = x.left;
        }
        return x;
    }

    private Node successor(Node x){
        Node y;
        if(!x.right.equals(NIL)){
            return minimum(x.right);
        }
        y = x.parent;
        while(!y.equals(NIL) && x.equals(y.right)){
            x = y;
            y = y.parent;
        }
        return y;
    }

    private void deleteP(Node z){
        Node x,y;
        if(z.left.equals(NIL) || z.right.equals(NIL)){
            y = z;
        }
        else{
            y = successor(z);
        }
        if(!y.left.equals(NIL)){
            x = y.left;
        }
        else{
            x = y.right;
        }
        x.parent = y.parent;
        if(y.parent==null){
            root = x;
        }
        else{
            if(y.equals(y.parent.left)){
                y.parent.left = x;
            }
            else{
                y.parent.right = x;
            }
        }
        if(!y.equals(z)){
            z.key = y.key;
        }
        if(y.color==Color.BLACK){
            fixDelete(x);
        }
    }

    private void fixDelete(Node x){
        Node w;
        while(!x.equals(root) && x.color==Color.BLACK){
            if(x.equals(x.parent.left)){
                w = x.parent.right;
                if(w.color==Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if(w.left.color==Color.BLACK && w.right.color==Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                }
                else{
                    if(w.right.color==Color.BLACK){
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
            }
            else{
                w = x.parent.left;
                if(w.color==Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if(w.right.color==Color.BLACK && w.left.color==Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                }
                else{
                    if(w.left.color==Color.BLACK){
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

    private class Node {
        private Color color;
        private Node left;
        private Node right;
        private Node parent;
        private int key;

        private Node(Color color, Node left, Node right, Node parent, int key) {
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
                    && this.key == node.key;  // key - integer
        }
    }

    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        //Checking adding, removing ( correct, incorrect).
        tree.add(10);
        tree.add(215);
        tree.add(22);
        tree.add(-4);
        tree.add(5);
        System.out.println(tree.inorderTraverse().toString());
        tree.remove(10);
        tree.remove(5);
        tree.add(0);
        System.out.println(tree.inorderTraverse().toString());
        tree.remove(0);
        tree.remove(-1);
        System.out.println(tree.inorderTraverse().toString());
    }
}
