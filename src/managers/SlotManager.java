/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager;

import datastructures.CustomMinHeap;
import models.ParkingSlot;

/**
 * This class is used to implement CustomMinHeap and give users the best parking
 * slot (lowest distanceFromGate)
 *
 * @author Yim Zi Hao
 */
public class SlotManager {

    private CustomMinHeap heap;

    public SlotManager() {
        heap = new CustomMinHeap();
    }

    /**
     * Load parking slots at startup.
     *
     * @param parkingSlot
     */
    public void loadSlot(ParkingSlot[] parkingSlot) {
        for (ParkingSlot slot : parkingSlot) {
            heap.offer(slot);
        }
    }

    /**
     * Assign the nearest available slot to an incoming vehicle.
     *
     * @return best parking slot (lowest distanceFromGate)
     */
    public ParkingSlot assignBestSlot() {
        ParkingSlot best = heap.poll();
        if (best == null) {
            System.out.println("No available parking slots.");
            return null;
        }
        best.setOccupied(true);
        System.out.println("Assigned slot: " + best.getSlotID()
                + " (distance: " + best.getDistanceFromGate() + "m)");
        return best;
    }

    /**
     * Release a slot when a vehicle exits then put it back in the heap.
     *
     * @param slot
     */
    public void releaseSlot(ParkingSlot slot) {
        slot.setOccupied(false);
        heap.offer(slot);
        System.out.println("Slot " + slot.getSlotID() + " is now available again.");
    }

    public boolean hasAvailableSlots() {
        return !heap.isEmpty();
    }
}
