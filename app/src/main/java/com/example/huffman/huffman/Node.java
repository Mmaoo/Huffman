package com.example.huffman.huffman;

public class Node {

    private Character value = null;
    private int weight;
    private int index;
    public Node parent = null;
    public Node left = null;
    public Node right = null;

    public Node(Node parent){ // NYT
        this.parent = parent;
        this.weight = 0;
        this.index = 0;
    }

    public Node(Node parent, char value) { // leaf
        this.parent = parent;
        this.value = value;
        this.weight = 1;
        this.index = 1;
    }

    public Node(int val) {
        weight = val;
    }

    public boolean isLeaf() {
        return value != null;
    }

    public void increment(){
        this.weight++;
    }

    public Character getCharacter() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + (value != null ? String.valueOf(value) : "null") +
                ", weight=" + weight +
                ", index=" + index +
                ", parent=" + (parent != null ? Integer.toString(parent.index) : "null") +
                ", left=" + (left != null ? Integer.toString(left.index) : "null") +
                ", right=" + (right != null ? Integer.toString(right.index) : "null") +
                '}';
    }

    public String printNode(){
        return "Node{" +
                "index = " + index +
                ", weight = " + weight +
                ", value = " + (value != null ? String.valueOf(value) : "null") +
                "}";
    }
}
