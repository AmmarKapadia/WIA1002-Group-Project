/**
 * Author: Ammar Kapadia
 * Description: Core model representing a vehicle in the system.
 */
package models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Vehicle {
    private String licensePlate;
    private String ownerName;
    private long entryTime;
    private ParkingSlot assignedSlot;   // NEW: which slot this vehicle currently occupies

    //Constructor
    public Vehicle(String licensePlate, String ownerName, long entryTime) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.entryTime = entryTime;
        this.assignedSlot = null;
    }

    // Basic Getters and Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    // NEW getter / setter for the assigned slot (used by GateManager on exit/undo)
    public ParkingSlot getAssignedSlot() {
        return assignedSlot;
    }

    public void setAssignedSlot(ParkingSlot assignedSlot) {
        this.assignedSlot = assignedSlot;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String entryStr = sdf.format(new Date(entryTime));
        String slotStr  = (assignedSlot != null) ? assignedSlot.getSlotID() : "—";
        return String.format("[%s] Owner: %-15s | Slot: %-4s | Entered: %s",
                licensePlate, ownerName, slotStr, entryStr);
    }
}
