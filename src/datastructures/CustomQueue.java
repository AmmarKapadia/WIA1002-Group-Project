package datastructures;

// Generic queue, suitable for any object (e.g., Vehicle)
public class CustomQueue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head; // Head of the queue
    private Node<T> tail; // Tail of the queue

    // Enqueue operation (Add to queue)
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) {
            head = tail;
        }
    }

    // Dequeue operation (Remove from queue)
    public T dequeue() {
        if (head == null) {
            return null; // Or throw a custom exception
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return data;
    }

    public boolean isEmpty() {
        return head == null;
    }
}