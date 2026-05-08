package datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked list of Edge objects, used as the per-vertex neighbour list
 * inside CustomGraph. Implements Iterable<Edge> so callers (e.g. Dijkstra,
 * printGraph) can walk it with a for-each loop from any package.
 */
public class EdgeLinkedList implements Iterable<Edge> {
    private static class Node {
        Edge data;
        Node next;

        public Node(Edge data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public void add(Edge edge) {
        Node newNode = new Node(edge);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public Iterator<Edge> iterator() {
        return new Iterator<Edge>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Edge next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                Edge data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
