package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;

import app.AppContext;
import managers.StatsManager;

/**
 * Stats panel: shows the parking system's live statistics.
 * Four components stacked vertically:
 *   1. Numbers row (Parked / Available / Entries / Exits)
 *   2. Occupancy progress bar with percentage label
 *   3. Peak entry hour label
 *   4. 24-hour static heatmap (one cell per hour-of-day)
 *
 * Reads live state from StatsManager via the injected AppContext.
 * Refreshed by MainController.refreshAll() after every entry, exit, or undo.
 *
 * @author Ammar Kapadia
 */
public class StatsPaneController {

    // --- Numbers row ---
    @FXML private Label parkedValueLabel;
    @FXML private Label availableValueLabel;
    @FXML private Label entriesValueLabel;
    @FXML private Label exitsValueLabel;

    // --- Occupancy bar ---
    @FXML private Label occupancyLabel;
    @FXML private ProgressBar occupancyBar;

    // --- Peak hour ---
    @FXML private Label peakHourLabel;

    // --- 24-hour heatmap ---
    @FXML private HBox heatmapContainer;

    private AppContext context;
    private final Rectangle[] heatmapCells = new Rectangle[24];
    private final Label[]     heatmapHourLabels = new Label[24];

    // Endpoints of the heatmap color gradient.
    // Cream (#F5EFE6) for zero, espresso (#3E2723) for peak.
    private static final Color HEATMAP_MIN = Color.web("#F5EFE6");
    private static final Color HEATMAP_MAX = Color.web("#3E2723");

    /**
     * Called automatically by FXMLLoader after FXML is loaded.
     * Builds the 24 empty heatmap cells. We can't poll StatsManager
     * yet because the context hasn't been injected — that happens
     * in setContext() below.
     */
    public void initialize() {
        for (int hour = 0; hour < 24; hour++) {
            Rectangle cell = new Rectangle(22, 30);
            cell.setFill(HEATMAP_MIN);
            cell.setStroke(Color.web("#E8DDCB"));
            cell.setStrokeWidth(1.0);

            // Hour label below each cell ("00", "01", ..., "23")
            Label hourLabel = new Label(String.format("%02d", hour));
            hourLabel.getStyleClass().add("stat-label");

            VBox cellBox = new VBox(2, cell, hourLabel);
            cellBox.setAlignment(Pos.CENTER);

            heatmapCells[hour] = cell;
            heatmapHourLabels[hour] = hourLabel;
            heatmapContainer.getChildren().add(cellBox);
        }
    }

    /**
     * Called once by MainController after FXML load completes,
     * passing in the shared AppContext. Triggers the first refresh.
     */
    public void setContext(AppContext context) {
        this.context = context;
        refresh();
    }

    /**
     * Re-polls StatsManager and updates every visual component.
     * Called by MainController.refreshAll() after each state-changing action.
     */
    public void refresh() {
        if (context == null) return;
        StatsManager stats = context.getStatsManager();

        // 1. Numbers row
        parkedValueLabel.setText(String.valueOf(stats.getTotalParked()));
        availableValueLabel.setText(String.valueOf(stats.getAvailableSlotCount()));
        entriesValueLabel.setText(String.valueOf(stats.getTotalEntries()));
        exitsValueLabel.setText(String.valueOf(stats.getTotalExits()));

        // 2. Occupancy bar + percentage label
        double occupancy = stats.getOccupancyPercentage();
        occupancyBar.setProgress(occupancy / 100.0);
        occupancyLabel.setText(String.format("%.1f%% Occupied", occupancy));

        // 3. Peak hour line
        String peak = stats.getPeakEntryHour();
        if (peak == null) {
            peakHourLabel.setText("Peak entry hour: (no entries yet)");
        } else {
            peakHourLabel.setText(String.format("Peak entry hour: %s:00 (%d entries)",
                    peak, stats.getPeakEntryCount()));
        }

        // 4. 24-hour heatmap
        refreshHeatmap(stats);
    }

    /**
     * Update each of the 24 heatmap cells. Cell color interpolates between
     * cream (zero entries) and espresso (peak entries) based on that hour's
     * entry count divided by the current peak count.
     */
    private void refreshHeatmap(StatsManager stats) {
        int peakCount = stats.getPeakEntryCount();

        for (int hour = 0; hour < 24; hour++) {
            int count = getEntryCountForHour(stats, hour);

            Color cellColor;
            if (peakCount == 0) {
                // No entries anywhere — every cell stays cream
                cellColor = HEATMAP_MIN;
            } else {
                double t = (double) count / (double) peakCount;
                cellColor = interpolateColor(HEATMAP_MIN, HEATMAP_MAX, t);
            }

            heatmapCells[hour].setFill(cellColor);
        }
    }

    /**
     * Looks up the entry count for the given hour-of-day via StatsManager.
     */
    private int getEntryCountForHour(StatsManager stats, int hour) {
        return stats.getEntryCountForHour(String.format("%02d", hour));
    }

    /**
     * Linear interpolation between two colors. t=0 returns 'from',
     * t=1 returns 'to', anything between is a proportional mix.
     */
    private Color interpolateColor(Color from, Color to, double t) {
        if (t < 0) t = 0;
        if (t > 1) t = 1;
        double r = from.getRed()   + t * (to.getRed()   - from.getRed());
        double g = from.getGreen() + t * (to.getGreen() - from.getGreen());
        double b = from.getBlue()  + t * (to.getBlue()  - from.getBlue());
        return new Color(r, g, b, 1.0);
    }
}