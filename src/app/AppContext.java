// app/AppContext.java
package app;

import managers.GateManager;
import managers.HashMapManager;
import managers.HistoryManager;
import managers.RecordManager;
import managers.SearchManager;
import managers.SlotManager;
import managers.StatsManager;

public class AppContext {
    
    // Hold all manager singletons
    private final StatsManager statsManager;
    private final HistoryManager historyManager;
    private final GateManager gateManager;
    
    // Base managers required for injection
    private final SlotManager slotManager;
    private final RecordManager recordManager;
    private final SearchManager searchManager;
    private final HashMapManager hashMapManager;

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
    }

    // Getters for the UI controllers
    public StatsManager getStatsManager() { return statsManager; }
    public HistoryManager getHistoryManager() { return historyManager; }
    public GateManager getGateManager() { return gateManager; }
    
    // Getters for base managers (if needed by other controllers)
    public SlotManager getSlotManager() { return slotManager; }
    public RecordManager getRecordManager() { return recordManager; }
    public SearchManager getSearchManager() { return searchManager; }
    public HashMapManager getHashMapManager() { return hashMapManager; }
}