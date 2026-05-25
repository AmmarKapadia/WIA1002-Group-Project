package controllers;

import app.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.ParkingSlot;
import models.Route;
import models.Vehicle;

public class ActionPaneController {

    @FXML private TextField plateTextField;
    @FXML private TextField ownerTextField;
    private AppContext context;

    public void setContext(AppContext context) {
        this.context = context;
    }

    @FXML
    private void handleEntry() {
        if (context == null) return;
        String plate = plateTextField.getText().trim().toUpperCase();
        String owner = ownerTextField.getText().trim();
        if (plate.isEmpty() || owner.isEmpty()) return;
        // 1. Create a vehicle object and add it to the entry queue
        Vehicle newVehicle = new Vehicle(plate, owner, System.currentTimeMillis());
        context.getGateManager().addVehicleToQueue(newVehicle);

        // 2. Schedule the processing of the current queued vehicle (assign it to a parking slot)
        Vehicle processedVehicle = context.getGateManager().processNextArrival();

        // 3. Refresh ALL panels FIRST so the map redraws cleanly (slot flips chocolate, stats update)
        if (context.getMainController() != null) {
            context.getMainController().refreshAll();
        }

        // 4. NOW draw the highlight on top of the refreshed map — it will stay visible for 5 seconds
        if (processedVehicle != null && processedVehicle.getAssignedSlot() != null) {
            String targetSlotID = processedVehicle.getAssignedSlot().getSlotID();
            Route route = context.getRouteManager().getRoute(targetSlotID);
            if (context.getMainController() != null && route != null) {
                context.getMainController().showRoute(route);
            }
        }

        plateTextField.clear();
        ownerTextField.clear();
    }

    @FXML
    private void handleExit() {
        if (context == null) return;
        String plate = plateTextField.getText().trim().toUpperCase();
        if (plate.isEmpty()) return;

        // 1. Look up the Vehicle instance from the HashMap structure using the plate number
        Vehicle v = context.getHashMapManager().lookup(plate);
        
        if (v != null) {
            // 2. Call the backend GateManager to perform the exit logic (passing the Vehicle object)
            context.getGateManager().processExit(v);
        } else {
            System.out.println("Error: Vehicle with plate " + plate + " not found inside the parking lot.");
        }

        // 3. Global UI refresh
        if (context.getMainController() != null) {
            context.getMainController().refreshAll();
        }
        plateTextField.clear();
    }
}
