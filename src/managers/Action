package managers;

import models.Vehicle;
import models.ParkingSlot;

public class Action {
    private String type;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private long timestamp;
    private String description;

    public Action(String type, Vehicle vehicle,
                  ParkingSlot slot, String description) {
        this.type = type;
        this.vehicle = vehicle;
        this.slot = slot;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    public String getType(){
        return type; 
    }
    public Vehicle getVehicle(){
        return vehicle; 
    }
    public ParkingSlot getSlot(){
        return slot; 
    }
    public long getTimestamp(){  
        return timestamp; 
    }
    public String getDescription(){
        return description;
    }
}
