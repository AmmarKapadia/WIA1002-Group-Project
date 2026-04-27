package managers;

import datastructures.CustomLinkedList;
import models.Vehicle;

public class RecordManager {

    private CustomLinkedList recordList;

    public RecordManager() {
        recordList = new CustomLinkedList();
    }

    // Tambah vehicle baru ke list
    public void addRecord(Vehicle vehicle) {
        recordList.add(vehicle);
        System.out.println("Record added: " + vehicle.getLicensePlate());
    }

    // Hapus vehicle berdasarkan license plate
    public void removeRecord(String licensePlate) {
        boolean removed = recordList.delete(licensePlate);
        if (removed) {
            System.out.println("Record removed: " + licensePlate);
        } else {
            System.out.println("Vehicle not found: " + licensePlate);
        }
    }

    // Tampilkan semua record
    public void displayAllRecords() {
        System.out.println("=== All Parking Records ===");
        recordList.display();
    }
}