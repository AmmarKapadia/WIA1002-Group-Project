package controllers;

import app.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Vehicle;

public class SearchPaneController {

    @FXML private TextField searchTextField;
    @FXML private Label resultLabel;

    private AppContext context;

    public void setContext(AppContext context) {
        this.context = context;
    }

    @FXML
    private void handleSearch() {
        if (context == null) return;
        String plate = searchTextField.getText().trim().toUpperCase();
        // Clear previous status classes before applying a new one
        resultLabel.getStyleClass().removeAll("status-success", "status-error", "status-warning");

        if (plate.isEmpty()) {
            resultLabel.setText("Please enter a plate number.");
            resultLabel.getStyleClass().add("status-warning");
            return;
        }
        Vehicle vehicle = context.getHashMapManager().lookup(plate);
        if (vehicle != null && vehicle.getAssignedSlot() != null) {
            String slotID = vehicle.getAssignedSlot().getSlotID();
            resultLabel.setText("Vehicle [" + plate + "] is parked at slot: " + slotID);
            resultLabel.getStyleClass().add("status-success");
        } else {
            resultLabel.setText("Vehicle [" + plate + "] not found or already exited.");
            resultLabel.getStyleClass().add("status-error");
        }
    }
}
