/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datastructures;

import java.util.Arrays;
import models.ParkingSlot;

/**
 * This class is used to implement a priority queue that stores parking slots
 * based on their distanceFromGate (the lowest distanceFromGate has the highest
 * priority).
 *
 * @author Yim Zi Hao
 *
 */
public class CustomMinHeap {

    private ParkingSlot[] heap;
    private int size;
    public static final int DEFAULT_CAPACITY = 11;

    public CustomMinHeap() {
        heap = new ParkingSlot[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Determines whether heap is empty.
     *
     * @return whether or not heap is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return size of heap.
     *
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * Adds a parking slot into heap.
     *
     * @param newParkingSlot
     */
    public void offer(ParkingSlot newParkingSlot) {
        if (size >= heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = newParkingSlot;
        bubbleUp(size);
        size++;
    }

    /**
     * Remove and return the parking slot with the smallest distanceFromGate.
     *
     * @return root of tree / parking slot with lowest distance from gate
     */
    public ParkingSlot poll() {
        if (isEmpty()) {
            return null;
        }
        ParkingSlot min = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        sinkDown(0);
        return min;
    }

    /**
     * Peek at the best slot without removing it.
     *
     * @return heap[0] / parking slot with the lowest distanceFromGate
     */
    public ParkingSlot peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    /**
     * Check if new node is smaller than parent node then move it up the tree or
     * not.
     *
     * @param i
     */
    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i].getDistanceFromGate() < heap[parent].getDistanceFromGate()) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Check if child is smaller than current node then swap or not.
     *
     * @param i
     */
    private void sinkDown(int i) {
        while (true) {
            int smallest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < size && heap[left].getDistanceFromGate() < heap[smallest].getDistanceFromGate()) {
                smallest = left;
            }
            if (right < size && heap[right].getDistanceFromGate() < heap[smallest].getDistanceFromGate()) {
                smallest = right;
            }
            if (smallest == i) {
                break;
            } else {
                swap(i, smallest);
                i = smallest;
            }
        }
    }

    /**
     * Swap two elements of heap
     *
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        ParkingSlot temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
