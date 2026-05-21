package controllers;

import app.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Vehicle;

public class SearchPaneController {

    @FXML private TextField searchField;
    @FXML private Label resultLabel;

    private AppContext appContext;

    /**
     * Injects the AppContext from MainController.
     */
    public void setup(AppContext context) {
        this.appContext = context;
    }

    /**
     * Handles the vehicle search lookup query.
     */
    @FXML
    private void handleSearch() {
        String plate = searchField.getText().trim().toUpperCase();
        if (plate.isEmpty()) {
            resultLabel.setText("Please provide a license plate.");
            return;
        }

        try {
            // Querying using HashMapManager's lookup method for O(1) efficiency
            Vehicle foundVehicle = appContext.getHashMapManager().lookup(plate);
            
            if (foundVehicle != null) {
                String slotID = (foundVehicle.getAssignedSlot() != null) ? 
                                foundVehicle.getAssignedSlot().getSlotID() : "No slot assigned";
                                
                // Displaying concise status details using data from models.Vehicle
                resultLabel.setText(String.format("Status: FOUND\nPlate: %s\nOwner: %s\nSlot: %s", 
                        foundVehicle.getLicensePlate(), 
                        foundVehicle.getOwnerName(), 
                        slotID));
            } else {
                resultLabel.setText("Status: NOT FOUND\nVehicle '" + plate + "' is not inside the parking lot.");
            }
        } catch (Exception e) {
            resultLabel.setText("Search error: " + e.getMessage());
        }
    }
}