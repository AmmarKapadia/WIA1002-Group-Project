/**
 * Author: Ammar Kapadia
 * Description: System statistics module for Sprint 2.
 *              Polls SlotManager + HashMapManager in O(1) for live occupancy
 *              numbers, and maintains a CustomHashMap<Integer> keyed by hour-of-day
 *              ("00".."23") for entry frequency / peak-hour detection.
 *              No menu logic here — Main.java wires recordEntry/recordExit to
 *              GateManager via gateManager.setStatsManager(...).
 */
package managers;

import datastructures.CustomHashMap;
import models.Vehicle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsManager {

    private final SlotManager slotManager;
    private final HashMapManager hashMapManager;

    /** Hour-of-day ("00".."23") -> number of entries recorded in that hour. */
    private final CustomHashMap<Integer> hourlyEntryCount;

    private int totalEntries;
    private int totalExits;

    public StatsManager(SlotManager slotManager, HashMapManager hashMapManager) {
        this.slotManager      = slotManager;
        this.hashMapManager   = hashMapManager;
        // 31 is prime, plenty for 24 distinct hour buckets
        this.hourlyEntryCount = new CustomHashMap<>(31);
        this.totalEntries     = 0;
        this.totalExits       = 0;
    }

    /** Called by GateManager whenever a vehicle is parked. */
    public void recordEntry(Vehicle v) {
        if (v == null) return;
        totalEntries++;
        String hour = formatHour(v.getEntryTime());
        Integer current = hourlyEntryCount.get(hour);
        hourlyEntryCount.put(hour, (current == null) ? 1 : current + 1);
    }

    /** Called by GateManager whenever a vehicle exits. */
    public void recordExit(Vehicle v) {
        if (v == null) return;
        totalExits++;
    }

    /** Currently-parked vehicle count, polled in O(1) from HashMapManager. */
    public int getTotalParked() {
        return hashMapManager.size();
    }

    public int getAvailableSlotCount() {
        return slotManager.availableSlotCount();
    }

    public double getOccupancyPercentage() {
        int total = getTotalParked() + getAvailableSlotCount();
        if (total == 0) return 0.0;
        return (getTotalParked() * 100.0) / total;
    }

    /** Returns the hour string ("00".."23") with the highest entry count, or null if no entries. */
    public String getPeakEntryHour() {
        String peak = null;
        int maxCount = 0;
        for (String hour : hourlyEntryCount.keys()) {
            Integer count = hourlyEntryCount.get(hour);
            if (count != null && count > maxCount) {
                maxCount = count;
                peak = hour;
            }
        }
        return peak;
    }

    public int getPeakEntryCount() {
        String peak = getPeakEntryHour();
        if (peak == null) return 0;
        Integer count = hourlyEntryCount.get(peak);
        return (count == null) ? 0 : count;
    }

    /**
     * Returns the entry count for a specific hour-of-day ("00".."23"),
     * or 0 if there have been no entries during that hour.
     * Used by the GUI heatmap to color all 24 cells with proper gradient
     * intensity, not just the single peak cell.
     */
    public int getEntryCountForHour(String hourKey) {
        if (hourKey == null) return 0;
        Integer count = hourlyEntryCount.get(hourKey);
        return (count == null) ? 0 : count;
    }

    public int getTotalEntries() { return totalEntries; }
    public int getTotalExits()   { return totalExits; }

    /** Formatted console summary, wired into the "Show Statistics" menu option in Sprint 2. */
    public void printSummary() {
        int total = getTotalParked() + getAvailableSlotCount();
        System.out.println("\n=== Parking Statistics ===");
        if (total == 0) {
            System.out.println("(no parking slots loaded yet)");
            return;
        }
        System.out.printf("Total parked: %d / %d (%.1f%% occupied)%n",
                getTotalParked(), total, getOccupancyPercentage());
        System.out.println("Available slots: " + getAvailableSlotCount());
        String peak = getPeakEntryHour();
        if (peak == null) {
            System.out.println("Peak entry hour: (no entries yet)");
        } else {
            System.out.println("Peak entry hour: " + peak + ":00 ("
                    + getPeakEntryCount() + " entries)");
        }
        System.out.println("Total entries: " + totalEntries);
        System.out.println("Total exits:   " + totalExits);
    }

    /** Converts a millisecond timestamp to a "00".."23" hour-of-day string. */
    private String formatHour(long timestampMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(new Date(timestampMillis));
    }
}