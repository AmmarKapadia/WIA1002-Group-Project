package datastructures;

import java.util.EmptyStackException; // Import the exception class suggested in the document

// Generic stack, used to track operation history
public class CustomStack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top; // Top of the stack

    // Push action to history
    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
    }

    // Pop the most recent action
    public T pop() {
        if (top == null) {
            throw new EmptyStackException(); // Using the suggested exception handling
        }
        T data = top.data;
        top = top.next;
        return data;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
