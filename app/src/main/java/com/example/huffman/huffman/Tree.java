package com.example.huffman.huffman;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree implements Serializable {
    public Node root;
    public Node nyt;

    public Map<Character,Node> map = new HashMap<Character,Node>();
    public List<Node> list = new ArrayList<Node>();

    public Tree(){
        this.root = new Node(null);
        list.add(root);
        this.nyt = root;
    }

    public List<Boolean> getCode(Character c){
        List<Boolean> charCode;

        if(map.containsKey(c)){
            charCode = generateCode(map.get(c));
        }else{
            charCode = generateCode(nyt);
            charCode.addAll(toBinaryList(c));
        }

        return charCode;
    }

    private List<Boolean> generateCode(Node node){
        List<Boolean> charCode = new ArrayList<Boolean>();
        Node temp = node;
        Node parent;
        while(temp.parent != null){
            parent = temp.parent;
            try {
                charCode.add(parent.right == temp);
            }catch(OutOfMemoryError e){
                Log.e(e.getClass().toString(),e.getMessage(),e);
                Log.w("tree",node.toString());
                printTree();
            }

            temp = parent;
        }

        // swap code bits
        Boolean t;
        for(int i=0;i<(charCode.size()/2.0);i++){
            t = charCode.get(i);
            charCode.set(i,charCode.get(charCode.size()-i-1));
            charCode.set(charCode.size()-i-1,t);
        }

        return charCode;
    }

    public void add(char c){
        Log.w("Tree","add "+c);
        // char exist in tree
        if(map.containsKey(c)){
            Node node = map.get(c);
            node.increment();
            updateTree(node);

        // char doesn't exist in tree
        }else{
            Node node = new Node(nyt,c);
            map.put(c,node);
            nyt.left = new Node(nyt);
            nyt.right = node;
            nyt = nyt.left;
            list.add(0,nyt);
            list.add(1,node);
            updateNodeIndexes();
            updateTree(node);
        }
        printTree();
        printList();
    }

    private void updateNodeIndexes(){
        for (Node node : list) {
            node.setIndex(list.indexOf(node));
        }
    }

    private void updateTree(Node node){
        Node nodeToSwap;
        node.setWeight(node.getWeight()-1);

        while(node != root){
            node.increment();
            if((nodeToSwap = findNodeToSwap(node))
                    != null){
                swapNodes(node,nodeToSwap);
            }
            node = node.parent;
        }
        // node is root
        node.increment();
    }

    private Node findNodeToSwap(Node node){
        Log.w("findNodeToSwap","START = "+node.toString());
        Node temp = list.get(list.indexOf(node)+1), next;
        for(int i = list.indexOf(temp);i<list.size()-1;i++){
            next = list.get(i+1);
            Log.w("findNodeToSwap","temp = "+temp.toString()+"\nnext = "+next.toString());
            if(     temp.getWeight() < next.getWeight()  &&
                    temp.getWeight() < node.getWeight() &&
                    temp != node.parent){
                return temp;
            }
            temp = next;
        }
        Log.w("findNodeToSwap","root");
        return null;
    }

    private void swapNodes(Node node1, Node node2){
        Log.w("SwapNode",node1.toString()+"\n"+node2.toString());
        Log.w("SwapNode","TreeBeforeSnap - start");
        printTree();
        printList();
        Log.w("SwapNode","TreeBeforeSnap - end");

        if(list.indexOf(node1) > list.indexOf(node2)){
            Node temp = node1;
            node1 = node2;
            node2 = temp;
        }

        if(node1.parent.left == node1){
            node1.parent.left = node2;
        }else{
            node1.parent.right = node2;
        }
        if(node2.parent.left == node2 && node2.parent.right != node2){
            node2.parent.left = node1;
        }else{
            node2.parent.right = node1;
        }

        Node parent1 = node1.parent;
        node1.parent = node2.parent;
        node2.parent = parent1;

        int ind1 = list.indexOf(node1);
        int ind2 = list.indexOf(node2);
        list.remove(node1);
        list.remove(node2);
//        if(ind1 < ind2){
        list.add(ind1,node2);
        list.add(ind2,node1);
//        }else{
//            list.add(ind2,node1);
//            list.add(ind1,node2);
//        }
        updateNodeIndexes();
    }

    public String toBinaryString(char c){
        String out = Integer.toBinaryString(c);
        while(out.length() < 16){
            out = "0" + out;
        }
        return out;
    }

    public List<Boolean> toBinaryList(char c){
        ArrayList<Boolean> out = new ArrayList<Boolean>();
        String str = toBinaryString(c);
        for(int i=0;i<str.length();i++){
            out.add(str.charAt(i) == '1');
        }
        return out;
    }

    public void printTree(){
        //printNodeChild3();
        Log.w("tree","TreeSize = "+Integer.toString(list.size()));
        String str = printNodeChild(root,0,false);
        Log.w("Tree",str);
    }

    public String printNodeChild(Node node, int tabulate, boolean max_r){
        //Log.w("treetest",node.printNode());
        String str = new String("");
        if(node != null) {
            String tab = new String("");
            for (int i = 0; i < tabulate; i++) {
                tab = tab + "\t";
            } //if(node == root) tab = "";


            str = str + printNodeChild(node.right,tabulate+1,max_r);
            if(!max_r){ tab=""; max_r=true; }
            str = str + tab + node.printNode() + "\n";
            str = str + printNodeChild(node.left,tabulate+1,max_r);
        }
        return str;
    }

    public void printList(){
        for(int i=0;i<list.size();i++){
            Log.w("tree",list.get(i).toString());

        }
    }

    public int checkLevel(Node node){
        int level = 0;
        while(node != root){
            node = node.parent;
            level++;
        }
        return level;
    }

    public int countOnLevel(int level){
        Node node = nyt;
        int currLevel = checkLevel(nyt);
        if(currLevel < level) return 0;
        while(currLevel > level){
            node = node.parent;
            currLevel--;
        }
        int count = 1;
        Node temp;
        if(node.getIndex()-1 >= 0) {
            temp = list.get(node.getIndex() - 1);
            while (checkLevel(temp) == level) {
                count++;
                if (temp.getIndex() - 1 < 0) break;
                temp = list.get(temp.getIndex() - 1);
            }
        }
        if(node.getIndex()+1 < list.size()) {
            temp = list.get(node.getIndex() + 1);
            while (checkLevel(temp) == level) {
                count++;
                if (temp.getIndex() + 1 >= list.size()) break;
                temp = list.get(temp.getIndex() + 1);
            }
        }
        return count;
    }
}
