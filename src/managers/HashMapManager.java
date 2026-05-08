/**
 * Author: Ammar Kapadia
 * Description: Manager wrapping CustomHashMap to give the rest of the system a
 *              clean API for O(1) vehicle lookups by license plate. Wired into
 *              GateManager so that every entry/exit/undo updates this table
 *              automatically.
 */
package managers;

import datastructures.CustomHashMap;
import models.Vehicle;

public class HashMapManager {

    private final CustomHashMap hashMap;

    /**
     * Default size 53 — a small prime works well for the parking-lot scale we
     * expect in this project (a few dozen to a few hundred concurrent vehicles).
     */
    public HashMapManager() {
        this(53);
    }

    public HashMapManager(int tableSize) {
        this.hashMap = new CustomHashMap(tableSize);
    }

    /**
     * Add a vehicle to the table. Called from GateManager.processNextArrival()
     * and from the EXIT-undo branch.
     */
    public void register(Vehicle vehicle) {
        if (vehicle == null) return;
        hashMap.put(vehicle.getLicensePlate(), vehicle);
    }

    /**
     * Remove a vehicle from the table. Called on exit and on ENTER-undo.
     */
    public void unregister(String licensePlate) {
        if (licensePlate == null) return;
        hashMap.remove(licensePlate);
    }

    /**
     * O(1) average lookup. Used by both Process Exit and Quick Status.
     */
    public Vehicle lookup(String licensePlate) {
        return hashMap.get(licensePlate);
    }

    /**
     * O(1) average yes/no check, used by the Quick Status menu option.
     */
    public boolean isParked(String licensePlate) {
        return hashMap.containsKey(licensePlate);
    }
}
