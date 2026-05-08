/**
 * Author: Ammar Kapadia
 * Description: Custom HashMap mapping a String (license plate) to a Vehicle object.
 * Uses an array of linked list nodes for basic collision handling.
 */
package datastructures;
import models.Vehicle;

public class CustomHashMap {
    // Basic inner class to represent a node in the linked list
    private class HashNode {
        private String key;
        private Vehicle value;
        private HashNode next;

        public HashNode(String key, Vehicle value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] table;
    private int tableSize;

    // Constructor to initialize the array with a specific size
    public CustomHashMap(int tableSize) {
        this.tableSize = tableSize;
        this.table = new HashNode[tableSize];
    }

    /**
     * Standard hash function mapping the string to a valid array index.
     * Uses Math.abs to prevent negative indices.
     */
    private int hashFunction(String key) {
        return Math.abs(key.hashCode() % tableSize);
    }

    /**
     * Inserts or updates a Vehicle record.
     */
    public void put(String key, Vehicle value) {
        // Basic safety check
        if (key == null) {
            return;
        }
        int index = hashFunction(key);
        HashNode head = table[index];
        HashNode current = head;

        // Update path: key already in chain
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Insert path: prepend new node to the chain
        HashNode newNode = new HashNode(key, value);
        newNode.next = head;
        table[index] = newNode;
    }

    /**
     * Retrieves a Vehicle in O(1) average time complexity based on its license plate.
     */
    public Vehicle get(String key) {
        if (key == null) {
            return null;
        }
        int index = hashFunction(key);
        HashNode current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public Vehicle remove(String key) {
        if (key == null) {
            return null;
        }
        int index = hashFunction(key);
        HashNode current = table[index];
        HashNode prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    // Removing the head of the chain
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }
}
