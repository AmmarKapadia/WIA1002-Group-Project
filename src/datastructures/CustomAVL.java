package datastructures;

import models.Vehicle; // Assuming Vehicle is in the models package

/**
 * CustomAVL: A self-balancing Binary Search Tree for Vehicle records.
 * Ensures O(log n) performance by maintaining a balance factor between [-1, 1].
 */
public class CustomAVL {
    
    private class Node {
        Vehicle vehicle;
        int height;
        Node left, right;

        Node(Vehicle vehicle) {
            this.vehicle = vehicle;
            this.height = 1;
        }
    }

    private Node root;
    private boolean lastDeleteSucceeded; // tracks whether the most recent delete() actually found the node

    // --- Core Balance Logic [cite: 132, 133, 134] ---

    private int getHeight(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x; // New root
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y; // New root
    }

    private Node rebalance(Node node) {
        updateHeight(node);
        int balance = getBalance(node);

        // LL Case 
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // RR Case 
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // LR Case 
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // RL Case 
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // --- Public API ---

    public void insert(Vehicle vehicle) {
        root = insertRecursive(root, vehicle);
    }

    private Node insertRecursive(Node node, Vehicle vehicle) {
        if (node == null) return new Node(vehicle);

        // Compare license plates (lexicographical order)
        int cmp = vehicle.getLicensePlate().compareTo(node.vehicle.getLicensePlate());

        if (cmp < 0) {
            node.left = insertRecursive(node.left, vehicle);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, vehicle);
        } else {
            return node; // Duplicate license plates not allowed
        }

        return rebalance(node);
    }

    public Vehicle search(String licensePlate) {
        Node current = root;
        while (current != null) {
            int cmp = licensePlate.compareTo(current.vehicle.getLicensePlate());
            if (cmp == 0) return current.vehicle;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    public boolean delete(String licensePlate) {
        lastDeleteSucceeded = false;
        root = deleteRecursive(root, licensePlate);
        return lastDeleteSucceeded;
    }

    private Node deleteRecursive(Node node, String licensePlate) {
        if (node == null) return null;

        int cmp = licensePlate.compareTo(node.vehicle.getLicensePlate());

        if (cmp < 0) {
            node.left = deleteRecursive(node.left, licensePlate);
        } else if (cmp > 0) {
            node.right = deleteRecursive(node.right, licensePlate);
        } else {
            // Node found — mark success before the actual removal
            lastDeleteSucceeded = true;
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node temp = findMin(node.right);
                node.vehicle = temp.vehicle;
                node.right = deleteRecursive(node.right, temp.vehicle.getLicensePlate());
            }
        }

        if (node == null) return null;
        return rebalance(node);
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // For verification 
    public int getRootHeight() {
        return getHeight(root);
    }
}
