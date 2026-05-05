package datastructures;

import models.Vehicle;

public class CustomLinkedList {
    private class Node {
        Vehicle data;
        Node next;

        public Node(Vehicle data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public void add(Vehicle vehicle) {
        Node newNode = new Node(vehicle);
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

    public boolean delete(String licensePlate) {
        if (head == null) return false;
        if (head.data.getLicensePlate().equalsIgnoreCase(licensePlate)) {
            head = head.next;
            return true;
        }
        Node current = head;
        while (current.next != null) {
            if (current.next.data.getLicensePlate().equalsIgnoreCase(licensePlate)) {
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }
}