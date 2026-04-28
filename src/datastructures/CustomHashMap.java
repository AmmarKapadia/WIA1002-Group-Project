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

        //Inner Class Constructor
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
     * Uses Java's built in Math.abs to prevent negative indices.
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

        // Step 1: Linear search to see if the key already exists.
        // If it does, just update the value and exit.
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        
        // Step 2: If we get here, the key was not found. 
        // We create a new node and add it to the front of the linked list.
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

        // Basic linear search through the linked list to handle collisions
        while (current != null) {
            // Compare strings using the standard .equals() method
            if (current.key.equals(key)) {
                return current.value; // Found the vehicle
            }
            // Move to the next node
            current = current.next;
        }
        // If the loop finishes and nothing is found, return null
        return null; 
    }
}