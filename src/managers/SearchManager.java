package managers;

import datastructures.CustomAVL;
import models.Vehicle;

/**
 * SearchManager: High-level API for searching vehicle records.
 * Swapped from CustomBST to CustomAVL to guarantee O(log n) search.
 */
public class SearchManager {
    
    private CustomAVL vehicleTree; // 

    public SearchManager() {
        this.vehicleTree = new CustomAVL();
    }

    /**
     * Adds a vehicle to the search index.
     */
    public void addVehicleRecord(Vehicle vehicle) {
        vehicleTree.insert(vehicle);
    }

    /**
     * Finds a vehicle by license plate in O(log n).
     */
    public Vehicle findVehicle(String licensePlate) {
        return vehicleTree.search(licensePlate);
    }

    /**
     * Removes a vehicle record from the search index.
     * @return true if the vehicle existed and was removed, false otherwise.
     * Required for Process Exit and Undo operations.
     */
    public boolean removeVehicle(String licensePlate) {
        return vehicleTree.delete(licensePlate);
    }
}
