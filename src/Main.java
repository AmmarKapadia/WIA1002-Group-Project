import java.util.Scanner;
import models.Vehicle;
import models.ParkingSlot;
import managers.*;

/**
 * Combines everyone's code into a runnable console program.
 *
 * UPDATED (Sprint 1 fixes):
 *   - Pre-loads 8 demo parking slots into the MinHeap at startup so the very
 *     first arrival actually gets parked.
 *   - Constructs HashMapManager and passes it (plus SearchManager) into the
 *     refactored GateManager so all four storage structures stay in sync.
 *   - Adds menu option 2: Process Exit (uses HashMap O(1) lookup).
 *   - Adds menu option 4: Quick Status (HashMap O(1) yes/no parked check).
 *   - Robust input handling — non-numeric and out-of-range inputs no longer
 *     crash the program.
 */
public class Main {
    public static void main(String[] args) {
        // ---- Initialise managers ----
        RecordManager   recordManager   = new RecordManager();    // LinkedList
        SlotManager     slotManager     = new SlotManager();      // MinHeap
        SearchManager   searchManager   = new SearchManager();    // BST
        HashMapManager  hashMapManager  = new HashMapManager(53); // HashMap (size = small prime)

        GateManager gateManager = new GateManager(
                slotManager, recordManager, searchManager, hashMapManager);

        // ---- Pre-load 8 demo parking slots so the heap is not empty ----
        ParkingSlot[] demoSlots = {
            new ParkingSlot("S01",  8),
            new ParkingSlot("S02", 10),
            new ParkingSlot("S03", 18),
            new ParkingSlot("S04", 15),
            new ParkingSlot("S05", 16),
            new ParkingSlot("S06", 12),
            new ParkingSlot("S07", 20),
            new ParkingSlot("S08", 14)
        };
        slotManager.loadSlot(demoSlots);
        System.out.println("Loaded " + demoSlots.length + " parking slots into the available heap.");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n=== Smart Parking Management System ===");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Process Entry");
            System.out.println("2. Process Exit");
            System.out.println("3. Find Vehicle (BST search)");
            System.out.println("4. Quick Status (HashMap O(1) lookup)");
            System.out.println("5. Show All Records");
            System.out.println("6. Undo Last Action");
            System.out.println("7. Exit System");
            System.out.print("Select an option [1-7]: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: {
                        System.out.print("Enter License Plate: ");
                        String plate = scanner.nextLine().trim();
                        System.out.print("Enter Owner Name: ");
                        String name = scanner.nextLine().trim();

                        if (plate.isEmpty() || name.isEmpty()) {
                            System.out.println("Error: license plate and owner name cannot be empty.");
                            break;
                        }

                        Vehicle newVehicle = new Vehicle(plate, name, System.currentTimeMillis());
                        gateManager.addVehicleToQueue(newVehicle);
                        Vehicle processed = gateManager.processNextArrival();

                        if (processed != null) {
                            System.out.println("Vehicle " + plate + " has been registered and parked.");
                            System.out.println("  -> " + processed);
                        }
                        break;
                    }

                    case 2: {
                        System.out.print("Enter License Plate to exit: ");
                        String exitPlate = scanner.nextLine().trim();
                        // O(1) HashMap lookup to find the vehicle quickly
                        Vehicle exiting = hashMapManager.lookup(exitPlate);
                        if (exiting != null) {
                            gateManager.processExit(exiting);
                        } else {
                            System.out.println("Error: No parked vehicle found with plate " + exitPlate);
                        }
                        break;
                    }

                    case 3: {
                        System.out.print("Enter License Plate to search: ");
                        String searchPlate = scanner.nextLine().trim();
                        Vehicle found = searchManager.findVehicle(searchPlate);
                        if (found != null) {
                            System.out.println("Vehicle found: " + found);
                        } else {
                            System.out.println("Error: vehicle not found.");
                        }
                        break;
                    }

                    case 4: {
                        System.out.print("Enter License Plate to check: ");
                        String statusPlate = scanner.nextLine().trim();
                        if (hashMapManager.isParked(statusPlate)) {
                            Vehicle v = hashMapManager.lookup(statusPlate);
                            System.out.println("Status: PARKED");
                            System.out.println("  -> " + v);
                        } else {
                            System.out.println("Status: NOT PARKED (or already exited)");
                        }
                        break;
                    }

                    case 5:
                        System.out.println("Displaying all parking records...");
                        recordManager.displayAllRecords();
                        break;

                    case 6:
                        gateManager.undoLastAction();
                        break;

                    case 7:
                        running = false;
                        System.out.println("System shutting down. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option! Please enter a number between 1 and 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input Error: Please enter a valid menu number.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
