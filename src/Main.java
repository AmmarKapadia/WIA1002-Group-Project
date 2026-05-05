import java.util.Scanner;
import models.Vehicle;
import managers.*;

/**
 * Combine everyone's code
 * Not completely done
 * @author XU ZIMAO
 */
public class Main {
    public static void main(String[] args) {
        // Initialize managers from all members
        RecordManager recordManager = new RecordManager(); // Member 2
        // GateManager gateManager = new GateManager();     // Member 3 Not done yet
        SlotManager slotManager = new SlotManager();     // Member 4
        SearchManager searchManager = new SearchManager(); // Member 5
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Smart Parking Management System (Sprint 1) ===");

        while (running) {
            // Display Menu
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Process Entry");
            System.out.println("2. Find Vehicle");
            System.out.println("3. Undo Last Action");
            System.out.println("4. Show All Records");
            System.out.println("5. Exit");
            System.out.print("Select an option [1-5]: ");

            try {
                // Prevent crashes from non-integer input using try/catch
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter License Plate:");
                        String plate = scanner.nextLine();
                        System.out.print("Enter Owner Name:");
                        String name = scanner.nextLine();
                        
                        // Core Data Flow logic
                        Vehicle newVehicle = new Vehicle(plate, name, System.currentTimeMillis());
                        recordManager.addRecord(newVehicle);
                        searchManager.addVehicleRecord(newVehicle);
                        slotManager.assignBestSlot();

                        System.out.println("Vehicle" + plate + "has been registered successfully.");
                        break;

                    case 2:
                        System.out.print("Enter License Plate to Search: ");
                        String searchPlate = scanner.nextLine();
                        Vehicle found = searchManager.findVehicle(searchPlate);
                        if (found != null) {
                            System.out.println("Vehicle Found:" + found.getOwnerName() + "(Entry Time:" + found.getEntryTime() + ")");
                        } else {
                            System.out.println("Error: Vehicle not found.");
                        }
                        break;

                    case 3:
                        // Trigger undo mechanism from Member 3

                        // gateManager.undoLastAction();
                        // @Undo 

                        System.out.println("Undo operation executed.");
                        break;

                    case 4:
                        System.out.println("Displaying all parking records...");
                        // This would typically call Member 2's RecordManager logic
                        // @Undo

                        break;

                    case 5:
                        running = false;
                        System.out.println("System shutting down. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option! Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                // Handling non-numeric input[cite: 1, 2]
                System.out.println("Input Error: Please enter a valid menu number.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred:" + e.getMessage());
            }
        }
        scanner.close();
    }
}
