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
        if (plate.isEmpty()) {
            resultLabel.setText("Please enter a plate number.");
            resultLabel.setStyle("-fx-text-fill: #D32F2F;"); // Red warning style
            return;
        }

        // Use HashMapManager for O(1) quick lookup of license plate mapping
        Vehicle vehicle = context.getHashMapManager().lookup(plate);

        if (vehicle != null && vehicle.getAssignedSlot() != null) {
            String slotID = vehicle.getAssignedSlot().getSlotID();
            resultLabel.setText("Vehicle [" + plate + "] is parked at slot: " + slotID);
            resultLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;"); // Green safe style
        } else {
            resultLabel.setText("Vehicle [" + plate + "] not found or already exited.");
            resultLabel.setStyle("-fx-text-fill: #7B5E57; -fx-font-style: italic;"); // Chocolate subtle style
        }
    }
}