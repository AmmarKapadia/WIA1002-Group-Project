package managers;

import datastructures.CustomBST;
import models.Vehicle;

/**
 * Implement BST Serch
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
}