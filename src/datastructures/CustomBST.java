package datastructures;

import models.Vehicle;


/**
 * Binary Search Tree implementation for alphabetized vehicle storage and retrieval.
 * @author XU ZIMAO
 */
public class CustomBST {
    private class Node {
        Vehicle vehicle;
        Node left, right;

        Node(Vehicle vehicle) {
            this.vehicle = vehicle;
        }
    }

    private Node root;
    private boolean lastDeleteSucceeded; // tracks the result of the most recent delete()

    // Inserts a vehicle using String.compareTo() for alphabetical ordering.
    public void insert(Vehicle vehicle) {
        root = insertRecursive(root, vehicle);
    }

    private Node insertRecursive(Node root, Vehicle vehicle) {
        if (root == null) {
            return new Node(vehicle);
        }
        
        // Compare license plates
        int comparison = vehicle.getLicensePlate().compareTo(root.vehicle.getLicensePlate());

        if (comparison < 0) {
            root.left = insertRecursive(root.left, vehicle);
        } else if (comparison > 0) {
            root.right = insertRecursive(root.right, vehicle);
        }
        return root;
    }

    // Searches for a vehicle by license plate in O(log n) average time
    public Vehicle search(String licensePlate) {
        return searchRecursive(root, licensePlate);
    }

    private Vehicle searchRecursive(Node root, String licensePlate) {
        if (root == null || root.vehicle.getLicensePlate().equals(licensePlate)) {
            return (root != null) ? root.vehicle : null;
        }
        int comparison = licensePlate.compareTo(root.vehicle.getLicensePlate());
        if (comparison < 0) {
            return searchRecursive(root.left, licensePlate);
        } else {
            return searchRecursive(root.right, licensePlate);
        }
    }

    public boolean delete(String licensePlate) {
        lastDeleteSucceeded = false;
        root = deleteRecursive(root, licensePlate);
        return lastDeleteSucceeded;
    }

    private Node deleteRecursive(Node node, String licensePlate) {
        if (node == null) {
            return null;
        }
        int cmp = licensePlate.compareTo(node.vehicle.getLicensePlate());
        if (cmp < 0) {
            node.left = deleteRecursive(node.left, licensePlate);
        } else if (cmp > 0) {
            node.right = deleteRecursive(node.right, licensePlate);
        } else {
            // Found the node we want to delete
            lastDeleteSucceeded = true;
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Two children: find in-order successor (smallest in right subtree)
            Node successor = findMin(node.right);
            node.vehicle = successor.vehicle;
            // Delete the successor from the right subtree
            node.right = deleteSuccessor(node.right);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node deleteSuccessor(Node node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteSuccessor(node.left);
        return node;
    }
}
