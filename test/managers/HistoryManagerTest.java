package managers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import models.Vehicle;
import models.ParkingSlot;

/**
 * Tests for HistoryManager — stack-backed action log used for undo.
 * Action constructor: Action(type, vehicle, slot, description).
 * Covers: empty state, recordAction, size tracking, popLast, peekLast,
 * LIFO ordering across multiple records.
 *
 * @author Sun Chengyang
 */
public class HistoryManagerTest {

    private HistoryManager history;

    @Before
    public void setUp() {
        history = new HistoryManager();
    }

    @Test
    public void testEmpty_sizeZero() {
        assertEquals(0, history.size());
    }

    @Test
    public void testEmpty_popLastReturnsNull() {
        assertNull(history.popLast());
    }

    @Test
    public void testEmpty_peekLastReturnsNull() {
        assertNull(history.peekLast());
    }

    @Test
    public void testRecordAction_sizeIncrements() {
        history.recordAction(makeAction("ENTER", "ABC1"));
        assertEquals(1, history.size());
    }

    @Test
    public void testRecordMultiple_sizeTracksCount() {
        history.recordAction(makeAction("ENTER", "A1"));
        history.recordAction(makeAction("ENTER", "A2"));
        history.recordAction(makeAction("ENTER", "A3"));
        assertEquals(3, history.size());
    }

    @Test
    public void testPeekLast_doesNotRemove() {
        history.recordAction(makeAction("ENTER", "A1"));
        Action peeked = history.peekLast();
        assertNotNull(peeked);
        assertEquals(1, history.size());  // size unchanged
        assertEquals("ENTER", peeked.getType());
    }

    @Test
    public void testPopLast_returnsAndRemovesMostRecent() {
        history.recordAction(makeAction("ENTER", "FIRST"));
        history.recordAction(makeAction("ENTER", "SECOND"));
        Action popped = history.popLast();
        assertNotNull(popped);
        // Most-recently-recorded action should be returned
        assertEquals("SECOND", popped.getVehicle().getLicensePlate());
        assertEquals(1, history.size());
    }

    @Test
    public void testPopAll_emptyAgain() {
        history.recordAction(makeAction("ENTER", "A1"));
        history.recordAction(makeAction("ENTER", "A2"));
        history.popLast();
        history.popLast();
        assertEquals(0, history.size());
        assertNull(history.popLast());
    }

    @Test
    public void testLifoOrder_acrossMultiplePops() {
        history.recordAction(makeAction("ENTER", "FIRST"));
        history.recordAction(makeAction("ENTER", "SECOND"));
        history.recordAction(makeAction("ENTER", "THIRD"));
        // Pop in reverse insertion order
        assertEquals("THIRD",  history.popLast().getVehicle().getLicensePlate());
        assertEquals("SECOND", history.popLast().getVehicle().getLicensePlate());
        assertEquals("FIRST",  history.popLast().getVehicle().getLicensePlate());
    }

    @Test
    public void testMixedTypes_recordedCorrectly() {
        history.recordAction(makeAction("ENTER", "A1"));
        history.recordAction(makeAction("EXIT",  "A1"));
        assertEquals("EXIT",  history.popLast().getType());
        assertEquals("ENTER", history.popLast().getType());
    }

    /** Helper: builds an Action with the given type and vehicle plate. */
    private Action makeAction(String type, String plate) {
        Vehicle v   = new Vehicle(plate, "Owner_" + plate, System.currentTimeMillis());
        ParkingSlot s = new ParkingSlot("S01", 8);
        return new Action(type, v, s, type + " " + plate);
    }
}
