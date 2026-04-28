/**
 * Author: Ammar Kapadia
 * Description: Core model representing a parking slot.
 */
package models;

public class ParkingSlot {
    private String slotID;
    private int distanceFromGate;
    private boolean isOccupied;

    //Constructor
    public ParkingSlot(String slotID, int distanceFromGate) {
        this.slotID = slotID;
        this.distanceFromGate = distanceFromGate;
        this.isOccupied = false; // the default state
    }

    // Basic Getters and Setters
    public String getSlotID() { 
        return slotID; 
    }
    
    public void setSlotID(String slotID) { 
        this.slotID = slotID; 
    }

    public int getDistanceFromGate() { 
        return distanceFromGate; 
    }
    
    public void setDistanceFromGate(int distanceFromGate) { 
        this.distanceFromGate = distanceFromGate; 
    }

    public boolean isOccupied() { 
        return isOccupied; 
    }
    
    public void setOccupied(boolean occupied) { 
        this.isOccupied = occupied; 
    }
}