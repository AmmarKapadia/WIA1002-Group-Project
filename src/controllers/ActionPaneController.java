package controllers;

import app.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Vehicle;

public class ActionPaneController {

    @FXML private TextField plateField;
    @FXML private Label statusLabel;

    private AppContext appContext;
    private MainController mainController;

    /**
     * Injects the required contexts from MainController.
     */
    public void setup(AppContext context, MainController main) {
        this.appContext = context;
        this.mainController = main;
    }

    /**
     * Handles the vehicle entry workflow.
     */
    @FXML
    private void handleEntry() {
        String plate = plateField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            statusLabel.setText("Error: Please enter a license plate.");
            return;
        }

        try {
            // Create a new Vehicle instance using the exact constructor from Vehicle.java
            // Defaulting owner name to "Guest" and entry time to current system millis
            Vehicle vehicle = new Vehicle(plate, "Guest", System.currentTimeMillis());
            
            // 1. Add the vehicle to the waiting queue
            appContext.getGateManager().addVehicleToQueue(vehicle);
            
            // 2. Process the arrival to assign a slot and update stores
            Vehicle processedVehicle = appContext.getGateManager().processNextArrival();
            
            if (processedVehicle != null) {
                String slotID = processedVehicle.getAssignedSlot() != null ? 
                                processedVehicle.getAssignedSlot().getSlotID() : "Unknown";
                statusLabel.setText("Success: Vehicle [" + plate + "] parked at Slot [" + slotID + "].");
                plateField.clear();
                
                // Trigger global refresh to update Map, Stats, and History views
                mainController.refreshAll();
            } else {
                // processNextArrival returns null if parking is full
                statusLabel.setText("Waiting Queue: Lot is full! Vehicle [" + plate + "] is waiting in queue.");
                plateField.clear();
                
                // Refresh history or stats if needed even when just queued
                mainController.refreshAll();
            }
            
        } catch (Exception e) {
            statusLabel.setText("Entry failed: " + e.getMessage());
        }
    }

    /**
     * Handles the vehicle exit workflow.
     */
    @FXML
    private void handleExit() {
        String plate = plateField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            statusLabel.setText("Error: Please enter a license plate.");
            return;
        }

        try {
            // Find the active vehicle object from HashMapManager since processExit requires a Vehicle object
            Vehicle vehicleToExit = appContext.getHashMapManager().lookup(plate);
            
            if (vehicleToExit == null) {
                statusLabel.setText("Error: Vehicle [" + plate + "] is not registered in the system.");
                return;
            }
            
            // Pass the retrieved vehicle object to GateManager
            appContext.getGateManager().processExit(vehicleToExit);
            
            statusLabel.setText("Success: Vehicle [" + plate + "] has exited the lot.");
            plateField.clear();
            
            // Trigger global refresh across all panels
            mainController.refreshAll();
            
        } catch (Exception e) {
            statusLabel.setText("Exit failed: " + e.getMessage());
        }
    }
}