package controllers;

import app.AppContext;
import javafx.fxml.FXML;

public class MainController {
    
    private AppContext appContext;

    // Injected nested controllers from <fx:include> tags inside MainWindow.fxml
    @FXML private ActionPaneController actionPaneController;
    @FXML private SearchPaneController searchPaneController;
    
    // Stubs for teammates' panel controllers (uncomment and import as they submit code)
    // @FXML private MapPaneController mapPaneController;
    // @FXML private StatsPaneController statsPaneController;
    // @FXML private HistoryPaneController historyPaneController;

    /**
     * Setup context injection called immediately after App startup.
     */
    public void setAppContext(AppContext context) {
        this.appContext = context;
        
        // Pass the core data context and references to sub-controllers
        if (actionPaneController != null) {
            actionPaneController.setup(this.appContext, this);
        }
        if (searchPaneController != null) {
            searchPaneController.setup(this.appContext);
        }
        
        System.out.println("Main Framework Wiring Complete.");
    }

    /**
     * Crucial coordination point for Sprint 3.
     * Triggers UI updates across all teammate panels whenever an action changes the system state.
     */
    public void refreshAll() {
        // 1. Refresh your search results if needed, or leave it to manual search
        
        // 2. Trigger updates for your group members once they replace the placeholder FXMLs
        // if (mapPaneController != null) { mapPaneController.refresh(); }
        // if (statsPaneController != null) { statsPaneController.refresh(); }
        // if (historyPaneController != null) { historyPaneController.refresh(); }
        
        System.out.println("Broadcasting global refresh signal to all functional panels.");
    }
}