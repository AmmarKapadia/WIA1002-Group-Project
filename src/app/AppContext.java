package app;

import managers.GateManager;
import managers.HashMapManager;
import managers.HistoryManager;
import managers.RecordManager;
import managers.SearchManager;
import managers.SlotManager;
import managers.StatsManager;
import datastructures.CustomGraph;
import managers.DijkstraSolver;
import managers.RouteManager;
import models.ParkingSlot;

public class AppContext {
    
    // Hold all manager singletons
    private final StatsManager statsManager;
    private final HistoryManager historyManager;
    private final GateManager gateManager;
    private final CustomGraph parkingGraph;
    private final managers.RouteManager routeManager;
    
    // Base managers required for injection
    private final SlotManager slotManager;
    private final RecordManager recordManager;
    private final SearchManager searchManager;
    private final HashMapManager hashMapManager;

    // Main controller reference for cross-panel UI communication
    private controllers.MainController mainController;

    public AppContext() {
        // 1. Initialize base managers first (assuming default constructors)
        this.slotManager = new SlotManager();
        this.recordManager = new RecordManager();
        this.searchManager = new SearchManager();
        this.hashMapManager = new HashMapManager();

        // 2. Initialize dependent managers with required injections
        this.statsManager = new StatsManager(slotManager, hashMapManager);
        this.gateManager = new GateManager(slotManager, recordManager, searchManager, hashMapManager);
        // 3. Wire StatsManager into GateManager for the stats hook
        this.gateManager.setStatsManager(this.statsManager);
        // 4. Retrieve HistoryManager since it is initialized internally by GateManager
        this.historyManager = this.gateManager.getHistoryManager();
        // 5. Pre-load 5 demo slots into SlotManager (distances match the demo graph)
        slotManager.loadSlot(new ParkingSlot[]{
            new ParkingSlot("S01",  8),
            new ParkingSlot("S02", 10),
            new ParkingSlot("S03", 18),
            new ParkingSlot("S04", 15),
            new ParkingSlot("S05", 16)
        });
        // 6. Build the 9-vertex demo graph (matches Sprint 2 spec exactly)
        this.parkingGraph = new CustomGraph();
        parkingGraph.addEdge("GATE",  "INT_A", 5);
        parkingGraph.addEdge("GATE",  "INT_B", 6);
        parkingGraph.addEdge("INT_A", "INT_C", 4);
        parkingGraph.addEdge("INT_A", "S01",   3);
        parkingGraph.addEdge("INT_B", "INT_C", 7);
        parkingGraph.addEdge("INT_B", "S02",   4);
        parkingGraph.addEdge("INT_C", "S03",   9);
        parkingGraph.addEdge("INT_C", "S04",   6);
        parkingGraph.addEdge("INT_B", "S05",  10);
        // 7. Construct RouteManager with a DijkstraSolver and wire the graph
        this.routeManager = new RouteManager(new DijkstraSolver());
        this.routeManager.setGraph(parkingGraph);
    }

    // Getters for the UI controllers
    public StatsManager getStatsManager() { return statsManager; }
    public HistoryManager getHistoryManager() { return historyManager; }
    public GateManager getGateManager() { return gateManager; }
    public CustomGraph getParkingGraph() { return parkingGraph; }
    public managers.RouteManager getRouteManager() { return routeManager; }
    
    // Getters for base managers (if needed by other controllers)
    public SlotManager getSlotManager() { return slotManager; }
    public RecordManager getRecordManager() { return recordManager; }
    public SearchManager getSearchManager() { return searchManager; }
    public HashMapManager getHashMapManager() { return hashMapManager; }
    

    public void setMainController(controllers.MainController mainController) {
    this.mainController = mainController;
    }

    public controllers.MainController getMainController() {
        return mainController;
    }
}