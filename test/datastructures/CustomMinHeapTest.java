package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import datastructures.CustomMinHeap.Entry;

/**
 * Tests for CustomMinHeap — binary min-heap keyed by Entry.distance.
 * API uses offer/poll/peek (not insert/extractMin). Entry has public fields
 * key and distance (not getters).
 * Covers: empty heap, single insert, ordering across many inserts,
 * full extraction in ascending order, removal of arbitrary entry.
 *
 * @author Yim Zi Hao
 */
public class CustomMinHeapTest {

    private CustomMinHeap heap;

    @Before
    public void setUp() {
        heap = new CustomMinHeap();
    }

    @Test
    public void testEmpty_peekReturnsNull() {
        assertNull(heap.peek());
    }

    @Test
    public void testEmpty_pollReturnsNull() {
        assertNull(heap.poll());
    }

    @Test
    public void testEmpty_isEmptyTrue() {
        assertTrue(heap.isEmpty());
    }

    @Test
    public void testEmpty_sizeZero() {
        assertEquals(0, heap.size());
    }

    @Test
    public void testOfferOne_peekReturnsThat() {
        heap.offer("S01", 8);
        Entry top = heap.peek();
        assertNotNull(top);
        assertEquals("S01", top.key);
        assertEquals(8, top.distance);
    }

    @Test
    public void testOfferMultiple_peekReturnsSmallest() {
        heap.offer("S03", 18);
        heap.offer("S01", 8);
        heap.offer("S02", 10);
        Entry top = heap.peek();
        assertEquals("S01", top.key);
    }

    @Test
    public void testPoll_returnsAndRemovesSmallest() {
        heap.offer("S03", 18);
        heap.offer("S01", 8);
        heap.offer("S02", 10);
        Entry first = heap.poll();
        assertEquals("S01", first.key);
        Entry second = heap.peek();
        assertEquals("S02", second.key);
    }

    @Test
    public void testPollAll_ascendingDistanceOrder() {
        // Insert all 5 demo slots out of order: 18, 8, 16, 10, 15
        heap.offer("S03", 18);
        heap.offer("S01", 8);
        heap.offer("S05", 16);
        heap.offer("S02", 10);
        heap.offer("S04", 15);
        // Expect ascending: 8, 10, 15, 16, 18
        assertEquals(8,  heap.poll().distance);
        assertEquals(10, heap.poll().distance);
        assertEquals(15, heap.poll().distance);
        assertEquals(16, heap.poll().distance);
        assertEquals(18, heap.poll().distance);
        assertTrue(heap.isEmpty());
    }

    @Test
    public void testSize_tracksOfferAndPoll() {
        heap.offer("S01", 8);
        heap.offer("S02", 10);
        assertEquals(2, heap.size());
        heap.poll();
        assertEquals(1, heap.size());
    }

    @Test
    public void testRemove_arbitraryEntry_returnsTrue() {
        heap.offer("S01", 8);
        heap.offer("S02", 10);
        heap.offer("S03", 18);
        // Remove the middle element by reference (matches by key in remove()).
        boolean removed = heap.remove(new Entry("S02", 10));
        assertTrue(removed);
        assertEquals(2, heap.size());
        // After removal, remaining poll order should still be ascending.
        assertEquals(8,  heap.poll().distance);
        assertEquals(18, heap.poll().distance);
    }

    @Test
    public void testRemove_nonExistentEntry_returnsFalse() {
        heap.offer("S01", 8);
        boolean removed = heap.remove(new Entry("S99", 99));
        assertFalse(removed);
        assertEquals(1, heap.size());
    }

    @Test
    public void testGrowBeyondDefaultCapacity_stillCorrect() {
        // DEFAULT_CAPACITY is 11. Insert 20 entries to force the internal
        // array resize and verify ordering still holds.
        for (int i = 20; i >= 1; i--) {
            heap.offer("K" + i, i);
        }
        assertEquals(20, heap.size());
        // Polling should still give us 1, 2, 3, ... 20 in order.
        for (int i = 1; i <= 20; i++) {
            assertEquals(i, heap.poll().distance);
        }
    }
}
