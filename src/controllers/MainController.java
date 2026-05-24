package controllers;

import app.AppContext;
import javafx.fxml.FXML;
import models.Route;

public class MainController {

    // Inject sub-panel controllers (Naming convention: fx:id + Controller)
    @FXML private ActionPaneController actionPaneController;
    @FXML private SearchPaneController searchPaneController;
    @FXML private MapPaneController mapPaneController;
    @FXML private StatsPaneController statsPaneController;
    @FXML private HistoryPaneController historyPaneController;

    private AppContext context;
    private RouteOverlayHelper routeHelper; // Highlight helper for Member 4

    @FXML
    public void initialize() {
        routeHelper = new RouteOverlayHelper();
    }

    /**
     * Called by the App entry point upon startup to inject the context and propagate it downward
     */
    public void initContext(AppContext context) {
        this.context = context;
        this.context.setMainController(this); // Register with context

        // Bind the context to all sub-controllers
        if (actionPaneController != null) actionPaneController.setContext(context);
        if (searchPaneController != null) searchPaneController.setContext(context);
        if (mapPaneController != null) mapPaneController.setContext(context);
        if (statsPaneController != null) statsPaneController.setContext(context);
        if (historyPaneController != null) historyPaneController.setContext(context);
        
        // Initial data refresh upon loading
        refreshAll();
    }

    /**
     * Core method: Triggers a full UI refresh to keep all components synchronized
     */
    public void refreshAll() {
        // Refresh Member 2 map canvas (trigger redraw)
        if (mapPaneController != null) {
            mapPaneController.refresh();
        }
        
        // Refresh Member 1 data panel
        if (statsPaneController != null) {
            statsPaneController.refresh(); 
        }
        
        // Refresh Member 3 history list
        if (historyPaneController != null) {
            historyPaneController.refresh();
        }
    }

    /**
     * Calls Member 4 logic to highlight a path on the map for 5 seconds
     */
    public void showRoute(Route route) {
        if (routeHelper != null && mapPaneController != null) {
            routeHelper.highlightRoute(route, mapPaneController);
        }
    }
}