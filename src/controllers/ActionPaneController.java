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
    @FXML private javafx.scene.control.Label statusLabel;
    private AppContext context;

    public void setContext(AppContext context) {
        this.context = context;
    }

    @FXML
    private void handleEntry() {
        if (context == null) return;
        String plate = plateTextField.getText().trim().toUpperCase();
        String owner = ownerTextField.getText().trim();
        if (plate.isEmpty() || owner.isEmpty()) {
            setStatus("Plate and owner name are both required.", "status-error");
            return;
        }
        
        if (context.getHashMapManager().isParked(plate)) {
            setStatus("Vehicle " + plate + " is already parked.", "status-error");
            plateTextField.clear();
            ownerTextField.clear();
            return;
        }

        Vehicle newVehicle = new Vehicle(plate, owner, System.currentTimeMillis());
        context.getGateManager().addVehicleToQueue(newVehicle);
        Vehicle processedVehicle = context.getGateManager().processNextArrival();

        // Refresh first so map redraws cleanly before highlight is drawn on top
        if (context.getMainController() != null) {
            context.getMainController().refreshAll();
        }

        if (processedVehicle != null && processedVehicle.getAssignedSlot() != null) {
            String targetSlotID = processedVehicle.getAssignedSlot().getSlotID();
            Route route = context.getRouteManager().getRoute(targetSlotID);
            if (context.getMainController() != null && route != null) {
                context.getMainController().showRoute(route);
            }
            setStatus("Parked " + plate + " in slot " + targetSlotID, "status-success");
        } else {
            setStatus("Parking is full. " + plate + " added to wait queue.", "status-warning");
        }

        plateTextField.clear();
        ownerTextField.clear();
    }

    @FXML
    private void handleExit() {
        if (context == null) return;
        String plate = plateTextField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            setStatus("Enter a plate number.", "status-error");
            return;
        }

        Vehicle v = context.getHashMapManager().lookup(plate);
        if (v != null) {
            context.getGateManager().processExit(v);
            setStatus("Exited " + plate + " successfully.", "status-success");
        } else {
            setStatus("Vehicle " + plate + " is not currently parked.", "status-error");
        }

        if (context.getMainController() != null) {
            context.getMainController().refreshAll();
        }
        plateTextField.clear();
        ownerTextField.clear();
    }
    
    private void setStatus(String text, String styleClass) {
        statusLabel.setText(text);
        statusLabel.getStyleClass().removeAll("status-success", "status-error", "status-warning");
        statusLabel.getStyleClass().add(styleClass);
    }
}
