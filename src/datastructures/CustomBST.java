package datastructures;

import models.Vehicle;


/**
 * Binary Search Tree implementation for alphabetized vehicle storage and retrieval.
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

    // Searches for a vehicle by license plate in O(log n) time
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
}