package managers;

import datastructures.CustomBST;
import models.Vehicle;

/**
 * Implements BST-based search for vehicles by license plate.
 *
 * @author XU ZIMAO
 */
public class SearchManager {
    private CustomBST bst;

    public SearchManager() {
        this.bst = new CustomBST();
    }

    public void addVehicleRecord(Vehicle vehicle) {
        bst.insert(vehicle);
    }

    public Vehicle findVehicle(String licensePlate) {
        return bst.search(licensePlate);
    }

    public boolean removeVehicle(String licensePlate) {
        return bst.delete(licensePlate);
    }
}