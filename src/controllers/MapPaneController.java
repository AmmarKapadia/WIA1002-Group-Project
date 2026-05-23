package controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

import app.AppContext;

/**
 * Renders the parking lot as a graph: 9 vertices, 9 weighted edges,
 * slots colored by real-time occupancy from HashMapManager.
 *
 * @author Nicolas
 */
public class MapPaneController {

    @FXML private Canvas mapCanvas;

    private final Map<String, double[]> nodePositions = new HashMap<>();
    private AppContext context;  // injected by MainController.setContext(...)

    public void initialize() {
        nodePositions.put("GATE",   new double[]{250, 380});
        nodePositions.put("INT_A",  new double[]{180, 280});
        nodePositions.put("INT_B",  new double[]{320, 280});
        nodePositions.put("INT_C",  new double[]{250, 200});
        nodePositions.put("S01",    new double[]{ 80, 200});
        nodePositions.put("S02",    new double[]{420, 200});
        nodePositions.put("S03",    new double[]{180, 100});
        nodePositions.put("S04",    new double[]{320, 100});
        nodePositions.put("S05",    new double[]{420, 320});
        // Don't call refresh() here — context isn't injected yet.
        // First refresh happens after MainController calls setContext().
    }

    /** Called once by MainController after FXML load. */
    public void setContext(AppContext ctx) {
        this.context = ctx;
        refresh();
    }

    public void refresh() {
        if (mapCanvas == null) return;
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        // Background
        gc.setFill(Color.web("#F5EFE6"));
        gc.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // Edges first so nodes render on top
        gc.setStroke(Color.web("#3E2723"));
        gc.setLineWidth(2.0);
        drawEdge(gc, "GATE",  "INT_A", "5m");
        drawEdge(gc, "GATE",  "INT_B", "6m");
        drawEdge(gc, "INT_A", "INT_C", "4m");
        drawEdge(gc, "INT_A", "S01",   "3m");
        drawEdge(gc, "INT_B", "INT_C", "7m");
        drawEdge(gc, "INT_B", "S02",   "4m");
        drawEdge(gc, "INT_C", "S03",   "9m");
        drawEdge(gc, "INT_C", "S04",   "6m");
        drawEdge(gc, "INT_B", "S05",  "10m");

        // Nodes
        for (Map.Entry<String, double[]> entry : nodePositions.entrySet()) {
            String name = entry.getKey();
            double x = entry.getValue()[0];
            double y = entry.getValue()[1];

            if (name.equals("GATE")) {
                gc.setFill(Color.web("#3E2723"));
                gc.fillOval(x - 20, y - 20, 40, 40);
                gc.setStroke(Color.web("#F5EFE6"));
                gc.strokeOval(x - 20, y - 20, 40, 40);
                gc.setFill(Color.web("#F5EFE6"));
                gc.fillText(name, x - 14, y + 4);

            } else if (name.startsWith("INT")) {
                gc.setFill(Color.web("#FBF7F0"));
                gc.fillOval(x - 12, y - 12, 24, 24);
                gc.setStroke(Color.web("#A1887F"));
                gc.strokeOval(x - 12, y - 12, 24, 24);
                gc.setFill(Color.web("#3E2723"));
                gc.fillText(name, x - 15, y + 30);

            } else {
                // Slot — read REAL occupancy from HashMapManager
                boolean isOccupied = isSlotOccupied(name);

                if (isOccupied) {
                    gc.setFill(Color.web("#3E2723"));
                } else {
                    gc.setFill(Color.web("#F5EFE6"));
                }
                gc.fillRect(x - 15, y - 15, 30, 30);
                gc.setStroke(Color.web("#3E2723"));
                gc.strokeRect(x - 15, y - 15, 30, 30);
                gc.setFill(isOccupied ? Color.web("#F5EFE6") : Color.web("#3E2723"));
                gc.fillText(name, x - 10, y + 5);
            }
        }
    }

    /**
     * Returns true if any currently-parked vehicle is in this slot.
     * Reads live state from the real HashMapManager via the injected AppContext.
     */
    private boolean isSlotOccupied(String slotID) {
        if (context == null) return false;
        // Check every parked vehicle to see if its slot matches.
        // For 5 slots this is trivially fast.
        for (String plate : context.getHashMapManager().getAllParkedPlates()) {
            var v = context.getHashMapManager().lookup(plate);
            if (v != null && v.getAssignedSlot() != null
                    && slotID.equals(v.getAssignedSlot().getSlotID())) {
                return true;
            }
        }
        return false;
    }

    private void drawEdge(GraphicsContext gc, String from, String to, String weight) {
        double[] p1 = nodePositions.get(from);
        double[] p2 = nodePositions.get(to);
        if (p1 != null && p2 != null) {
            gc.strokeLine(p1[0], p1[1], p2[0], p2[1]);
            double midX = (p1[0] + p2[0]) / 2;
            double midY = (p1[1] + p2[1]) / 2;
            gc.setFill(Color.web("#6D4C41"));  // warm-brown, not blue
            gc.fillText(weight, midX, midY);
        }
    }

    public double[] getNodePosition(String vertex) { return nodePositions.get(vertex); }
    public Canvas getCanvas() { return mapCanvas; }
}
