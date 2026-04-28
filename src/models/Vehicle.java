/**
 * Author: Ammar Kapadia
 * Description: Core model representing a vehicle in the system.
 */
package models;

public class Vehicle {
    private String licensePlate;
    private String ownerName;
    private long entryTime; 

    //Constructor
    public Vehicle(String licensePlate, String ownerName, long entryTime) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.entryTime = entryTime;
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
}