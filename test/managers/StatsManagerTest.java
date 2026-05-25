package managers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import models.Vehicle;
import models.ParkingSlot;
import java.util.Calendar;

/**
 * Tests for StatsManager — live statistics polled from SlotManager and
 * HashMapManager, plus its own internal hourly entry counter.
 * Covers: initial zeroes, recordEntry/recordExit counter increments,
 * peak hour detection, per-hour count lookup, occupancy percentage,
 * null-safety on hour-key lookups.
 *
 * @author Ammar Kapadia
 */
public class StatsManagerTest {

    private StatsManager stats;
    private SlotManager slotManager;
    private HashMapManager hashMapManager;

    @Before
    public void setUp() {
        slotManager    = new SlotManager();
        hashMapManager = new HashMapManager();
        // Pre-load 5 demo slots so availableSlotCount and percentages can be computed
        slotManager.loadSlot(new ParkingSlot[]{
            new ParkingSlot("S01", 8),
            new ParkingSlot("S02", 10),
            new ParkingSlot("S03", 18),
            new ParkingSlot("S04", 15),
            new ParkingSlot("S05", 16)
        });
        stats = new StatsManager(slotManager, hashMapManager);
    }

    @Test
    public void testInitial_totalEntriesZero() {
        assertEquals(0, stats.getTotalEntries());
    }

    @Test
    public void testInitial_totalExitsZero() {
        assertEquals(0, stats.getTotalExits());
    }

    @Test
    public void testInitial_totalParkedZero() {
        assertEquals(0, stats.getTotalParked());
    }

    @Test
    public void testInitial_availableMatchesLoadedSlots() {
        assertEquals(5, stats.getAvailableSlotCount());
    }

    @Test
    public void testInitial_occupancyZero() {
        assertEquals(0.0, stats.getOccupancyPercentage(), 0.01);
    }

    @Test
    public void testInitial_peakHourNull() {
        assertNull(stats.getPeakEntryHour());
    }

    @Test
    public void testInitial_peakCountZero() {
        assertEquals(0, stats.getPeakEntryCount());
    }

    @Test
    public void testRecordEntry_incrementsLifetimeCounter() {
        stats.recordEntry(makeVehicleAtHour("ABC1", 14));
        assertEquals(1, stats.getTotalEntries());
    }

    @Test
    public void testRecordExit_incrementsLifetimeCounter() {
        stats.recordExit(makeVehicleAtHour("ABC1", 14));
        assertEquals(1, stats.getTotalExits());
    }

    @Test
    public void testRecordEntry_nullVehicle_ignored() {
        stats.recordEntry(null);  // guard clause must protect us
        assertEquals(0, stats.getTotalEntries());
    }

    @Test
    public void testRecordExit_nullVehicle_ignored() {
        stats.recordExit(null);
        assertEquals(0, stats.getTotalExits());
    }

    @Test
    public void testPeakHour_singleHourPopulated() {
        stats.recordEntry(makeVehicleAtHour("A1", 14));
        stats.recordEntry(makeVehicleAtHour("A2", 14));
        stats.recordEntry(makeVehicleAtHour("A3", 14));
        assertEquals("14", stats.getPeakEntryHour());
        assertEquals(3, stats.getPeakEntryCount());
    }

    @Test
    public void testPeakHour_multipleHoursPopulated() {
        stats.recordEntry(makeVehicleAtHour("A1", 10));
        stats.recordEntry(makeVehicleAtHour("A2", 14));
        stats.recordEntry(makeVehicleAtHour("A3", 14));
        stats.recordEntry(makeVehicleAtHour("A4", 14));
        stats.recordEntry(makeVehicleAtHour("A5", 18));
        assertEquals("14", stats.getPeakEntryHour());
        assertEquals(3, stats.getPeakEntryCount());
    }

    @Test
    public void testEntryCountForHour_matchesActual() {
        stats.recordEntry(makeVehicleAtHour("A1", 14));
        stats.recordEntry(makeVehicleAtHour("A2", 14));
        stats.recordEntry(makeVehicleAtHour("A3", 15));
        assertEquals(2, stats.getEntryCountForHour("14"));
        assertEquals(1, stats.getEntryCountForHour("15"));
    }

    @Test
    public void testEntryCountForHour_zeroForUnusedHour() {
        stats.recordEntry(makeVehicleAtHour("A1", 14));
        assertEquals(0, stats.getEntryCountForHour("09"));
        assertEquals(0, stats.getEntryCountForHour("23"));
    }

    @Test
    public void testEntryCountForHour_nullKeyReturnsZero() {
        assertEquals(0, stats.getEntryCountForHour(null));
    }

    /** Helper: builds a Vehicle whose entryTime falls on the given hour-of-day. */
    private Vehicle makeVehicleAtHour(String plate, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Vehicle(plate, "Owner_" + plate, cal.getTimeInMillis());
    }
}
