import java.util.Scanner;

// Import all custom managers, data structures, and models
import managers.GateManager;
import managers.HashMapManager;
import managers.RecordManager;
import managers.SearchManager;
import managers.SlotManager;
import managers.StatsManager;
import managers.DijkstraSolver;
import managers.RouteManager;
import datastructures.CustomGraph;
import models.Vehicle;
import models.ParkingSlot;

/**
 * Main Integration Hub for Sprint 2
 */
public class Main {

    // --- SYSTEM MANAGERS ---
    private static SlotManager slotManager;
    private static RecordManager recordManager;
    private static SearchManager searchManager;
    private static HashMapManager hashMapManager;
    private static GateManager gateManager;
    
    private static StatsManager statsManager;
    private static RouteManager routeManager;
    private static CustomGraph parkingGraph;

    public static void main(String[] args) {
        setupSystem();
        runMenu();
    }

    /**
     * Initializes all managers, pre-loads slots, and builds the demo graph.
     */
    private static void setupSystem() {
        System.out.println("Initializing Smart Parking Management System...");
        
        // 1. Initialize Base Storage Managers
        slotManager = new SlotManager();
        recordManager = new RecordManager();
        searchManager = new SearchManager(); // Member 5: Now uses CustomAVL internally
        hashMapManager = new HashMapManager();

        // 2. Initialize Gate Manager and inject dependencies
        gateManager = new GateManager(slotManager, recordManager, searchManager, hashMapManager);

        // --- MEMBER 1 INTEGRATION (StatsManager) ---
        // StatsManager needs SlotManager and HashMapManager to poll O(1) stats.
        statsManager = new StatsManager(slotManager, hashMapManager);
        gateManager.setStatsManager(statsManager); // Inject into GateManager to track entries/exits

        // --- MEMBER 4 INTEGRATION (Route Navigation) ---
        // RouteManager needs DijkstraSolver.
        DijkstraSolver dijkstraSolver = new DijkstraSolver();
        routeManager = new RouteManager(dijkstraSolver);

        // --- MEMBER 2 & 5 INTEGRATION (Graph Layout) ---
        parkingGraph = new CustomGraph();
        
        String[] vertices = {"GATE", "INT_A", "INT_B", "INT_C", "S01", "S02", "S03", "S04", "S05"};
        for (String v : vertices) {
            parkingGraph.addVertex(v);
        }

        parkingGraph.addEdge("GATE", "INT_A", 5);
        parkingGraph.addEdge("GATE", "INT_B", 6);
        parkingGraph.addEdge("INT_A", "INT_C", 4);
        parkingGraph.addEdge("INT_A", "S01", 3);
        parkingGraph.addEdge("INT_B", "INT_C", 7);
        parkingGraph.addEdge("INT_B", "S02", 4);
        parkingGraph.addEdge("INT_C", "S03", 9);
        parkingGraph.addEdge("INT_C", "S04", 6);
        parkingGraph.addEdge("INT_B", "S05", 10);

        routeManager.setGraph(parkingGraph);

        // Load mock slots into SlotManager (matching the graph nodes)
        // Note: Assuming a standard constructor ParkingSlot(slotID, distance)
        ParkingSlot[] initialSlots = new ParkingSlot[]{
            new ParkingSlot("S01", 8), new ParkingSlot("S02", 10),
            new ParkingSlot("S03", 18), new ParkingSlot("S04", 15), 
            new ParkingSlot("S05", 16)
        };
        slotManager.loadSlot(initialSlots);

        System.out.println("System Initialized Successfully.\n");
    }

    /**
     * Main console loop rendering the expanded Sprint 2 menu.
     */
    private static void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("=== Smart Parking Management System (Sprint 2) ===");
            System.out.println("MAIN MENU");
            System.out.println("1. Process Entry (assign slot + show shortest route)");
            System.out.println("2. Process Exit");
            System.out.println("3. Find Vehicle (AVL search, O(log n))");
            System.out.println("4. Quick Status (HashMap lookup, O(1))");
            System.out.println("5. Show Route to Slot");
            System.out.println("6. Show All Records");
            System.out.println("7. Show Parking Map (vertices + edges)");
            System.out.println("8. Show Statistics");
            System.out.println("9. Show Action History");
            System.out.println("10. Undo Last Action(s)");
            System.out.println("11. Exit System");
            System.out.print("Please enter your choice (1-11): ");

            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        processEntry(scanner);
                        break;
                    case 2:
                        processExit(scanner);
                        break;
                    case 3:
                        findVehicle(scanner);
                        break;
                    case 4:
                        quickStatus(scanner);
                        break;
                    case 5:
                        showRouteToSlot(scanner);
                        break;
                    case 6:
                        showAllRecords();
                        break;
                    case 7:
                        showParkingMap();
                        break;
                    case 8:
                        showStatistics();
                        break;
                    case 9:
                        showActionHistory();
                        break;
                    case 10:
                        undoLastActions(scanner);
                        break;
                    case 11:
                        System.out.println("Exiting System. Goodbye!");
                        isRunning = false;
                        break;
                    default:
                        System.out.println("[Error] Invalid choice. Please enter a number between 1 and 11.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Invalid input format. Please enter numerical digits only.");
            } catch (Exception e) {
                System.out.println("[Error] An unexpected error occurred: " + e.getMessage());
            }
            
            System.out.println();
        }
        scanner.close();
    }

    // --- MENU ACTION METHODS ---

    private static void processEntry(Scanner scanner) {
        System.out.print("Enter License Plate: ");
        String plate = scanner.nextLine().trim();
        
        if (plate.isEmpty()) {
            System.out.println("[Error] License plate cannot be empty.");
            return;
        }

        System.out.print("Enter Owner Name: ");
        String ownerName = scanner.nextLine().trim();

        if (ownerName.isEmpty()) {
            System.out.println("[Error] OwnerName cannot be empty.");
            return;
        }

        // Assuming a standard constructor Vehicle
        Vehicle newVehicle = new Vehicle(plate,ownerName,System.currentTimeMillis());
        gateManager.addVehicleToQueue(newVehicle);
        
        // Process arrival assigns a slot and logs stats/history
        Vehicle processedVehicle = gateManager.processNextArrival();

        // --- MEMBER 4 INTEGRATION (Route Printing) ---
        // Automatically print route after a successful entry
        if (processedVehicle != null && processedVehicle.getAssignedSlot() != null) {
            System.out.println("\n--- Navigation Directions ---");
            routeManager.navigateTo(processedVehicle.getAssignedSlot().getSlotID());
        }
    }

    private static void processExit(Scanner scanner) {
        System.out.print("Enter License Plate to Exit: ");
        String plate = scanner.nextLine().trim();

        // --- MEMBER 1/AK INTEGRATION (HashMap Lookup) ---
        // Fetch the active vehicle using the O(1) HashMap lookup before exiting
        Vehicle exitingVehicle = hashMapManager.lookup(plate);
        
        if (exitingVehicle != null) {
            gateManager.processExit(exitingVehicle);
        } else {
            System.out.println("Vehicle [" + plate + "] is not currently parked in the system.");
        }
    }

    private static void findVehicle(Scanner scanner) {
        System.out.print("Enter License Plate to Search: ");
        String plate = scanner.nextLine().trim();

        // --- MEMBER 5 INTEGRATION (AVL Search) ---
        Vehicle found = searchManager.findVehicle(plate);
        if (found != null) {
            System.out.println("Vehicle Found: " + found.getLicensePlate());
        } else {
            System.out.println("Vehicle [" + plate + "] not found in historical records.");
        }
    }

    private static void quickStatus(Scanner scanner) {
        System.out.print("Enter License Plate to check status: ");
        String plate = scanner.nextLine().trim();
        
        // --- MEMBER 1/AK INTEGRATION (HashMap isParked) ---
        if (hashMapManager.isParked(plate)) {
            System.out.println("Status: Vehicle [" + plate + "] is currently PARKED.");
        } else {
            System.out.println("Status: Vehicle [" + plate + "] is NOT parked.");
        }
    }

    private static void showRouteToSlot(Scanner scanner) {
        System.out.print("Enter Destination Slot ID (e.g., S03): ");
        String slotId = scanner.nextLine().trim().toUpperCase();

        // --- MEMBER 4 INTEGRATION (RouteManager API) ---
        routeManager.navigateTo(slotId);
    }

    private static void showAllRecords() {
        // --- RECORD MANAGER INTEGRATION ---
        recordManager.displayAllRecords();
    }

    private static void showParkingMap() {
        System.out.println("=== Parking Lot Map ===");
        // --- MEMBER 2 INTEGRATION (Graph Printing) ---
        parkingGraph.printGraph();
    }

    private static void showStatistics() {
        // --- MEMBER 1 INTEGRATION (StatsManager API) ---
        statsManager.printSummary();
    }

    private static void showActionHistory() {
        // --- MEMBER 3 INTEGRATION (HistoryManager via GateManager) ---
        // HistoryManager tracks the CustomStack of Actions
        gateManager.getHistoryManager().printHistory(10); // Print last 10 actions
    }

    private static void undoLastActions(Scanner scanner) {
        System.out.print("How many actions would you like to undo? ");
        String numInput = scanner.nextLine().trim();
        
        try {
            int n = Integer.parseInt(numInput);
            if (n <= 0) {
                System.out.println("Must undo at least 1 action.");
                return;
            }
            
            // --- MEMBER 3 INTEGRATION (Multi-step Undo) ---
            gateManager.undoLast(n);
            
        } catch (NumberFormatException e) {
            System.out.println("[Error] Please enter a valid number.");
        }
    }
}