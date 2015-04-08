package com.dataStructures;

import java.util.Iterator;
import java.util.Stack;

public class BST<Key extends Comparable<Key>,Value> implements Iterable<Key> {

private class Node{

Key key;
Value value;
Node left, right;

Node(Key key , Value value){
this.key = key;
this.value = value;
}

}

private Node root;

public Value get(Key key){
Node x = root;
while(x != null){
int cmp = key.compareTo(x.key);
if (cmp == 0) return x.value;
else if(cmp < 0) x = x.left;
else if(cmp > 0) x = x.right;	
}
return null;
}


public void put(Key key , Value value){
root = put(root, key , value);	

}

private Node put(Node x, Key key, Value value){	

if(x == null) return new Node(key , value);	
int cmp = key.compareTo(x.key);
if(cmp == 0 ) x.value = value;
else if (cmp < 0) x.left = put(x.left , key , value);
else if (cmp > 0) x.right = put(x.right , key , value);
return x;
}


public Iterator<Key> iterator()
{ 
return new BSTIterator(); 
}


private class BSTIterator implements Iterator<Key> {

private Stack<Node> stack = new Stack<Node>();

private void pushLeft(Node x) {
while (x != null) {
stack.push(x);
x = x.left;
}
}

BSTIterator() {
pushLeft(root);
}

public boolean hasNext() {
return !stack.isEmpty();
}

public Key next() {
Node x = stack.pop();
pushLeft(x.right);
return x.key;
}

public void remove() {

}

}

}

