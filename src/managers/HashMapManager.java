/**
 * Author: Ammar Kapadia
 * Description: Manager wrapping CustomHashMap to give the rest of the system a
 *              clean API for O(1) vehicle lookups by license plate. Updated in
 *              Sprint 2 to use the now-generic CustomHashMap<Vehicle> and to
 *              expose size() so StatsManager can compute occupancy in O(1).
 */
package managers;

import datastructures.CustomHashMap;
import models.Vehicle;

public class HashMapManager {

    private final CustomHashMap<Vehicle> hashMap;

    public HashMapManager() {
        this(53);
    }

    public HashMapManager(int tableSize) {
        this.hashMap = new CustomHashMap<>(tableSize);
    }

    public void register(Vehicle vehicle) {
        if (vehicle == null) return;
        hashMap.put(vehicle.getLicensePlate(), vehicle);
    }

    public void unregister(String licensePlate) {
        if (licensePlate == null) return;
        hashMap.remove(licensePlate);
    }

    /** O(1) average lookup. */
    public Vehicle lookup(String licensePlate) {
        return hashMap.get(licensePlate);
    }

    /** O(1) average yes/no check. */
    public boolean isParked(String licensePlate) {
        return hashMap.containsKey(licensePlate);
    }

    /** Number of currently-registered (parked) vehicles. O(1). Used by StatsManager. */
    public int size() {
        return hashMap.size();
    }

    /** Returns the license plates of all currently parked vehicles. Used by MapPane. */
    public String[] getAllParkedPlates() {
        return hashMap.keys();
    }
}
