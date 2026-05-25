package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CustomHashMap&lt;V&gt; — String key, generic value, separate-chaining.
 * Covers: empty state, put/get/remove, update semantics, null-key safety,
 * size tracking, key collisions, keys() retrieval.
 *
 * @author Ammar Kapadia
 */
public class CustomHashMapTest {

    private CustomHashMap<String> map;

    @Before
    public void setUp() {
        map = new CustomHashMap<>(11);
    }

    @Test
    public void testEmpty_getReturnsNull() {
        assertNull(map.get("ABC1"));
    }

    @Test
    public void testEmpty_sizeIsZero() {
        assertEquals(0, map.size());
    }

    @Test
    public void testEmpty_containsKeyReturnsFalse() {
        assertFalse(map.containsKey("ABC1"));
    }

    @Test
    public void testPut_thenGetReturnsValue() {
        map.put("ABC1", "Alice");
        assertEquals("Alice", map.get("ABC1"));
    }

    @Test
    public void testPut_incrementsSize() {
        map.put("ABC1", "Alice");
        map.put("DEF2", "Bob");
        assertEquals(2, map.size());
    }

    @Test
    public void testPut_existingKey_updatesValue_sizeUnchanged() {
        map.put("ABC1", "Alice");
        map.put("ABC1", "Alice Updated");
        assertEquals("Alice Updated", map.get("ABC1"));
        assertEquals(1, map.size());
    }

    @Test
    public void testPut_nullKey_silentlyIgnored() {
        // CustomHashMap.put() has a null-key guard that returns early.
        map.put(null, "X");
        assertEquals(0, map.size());
    }

    @Test
    public void testGet_nullKey_returnsNull() {
        map.put("ABC1", "Alice");
        assertNull(map.get(null));
    }

    @Test
    public void testRemove_removesEntryAndReturnsValue() {
        map.put("ABC1", "Alice");
        String removed = map.remove("ABC1");
        assertEquals("Alice", removed);
        assertNull(map.get("ABC1"));
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove_nonExistentKey_returnsNull() {
        map.put("ABC1", "Alice");
        assertNull(map.remove("NOPE"));
        assertEquals(1, map.size());  // unchanged
    }

    @Test
    public void testRemove_nullKey_returnsNull() {
        assertNull(map.remove(null));
    }

    @Test
    public void testContainsKey_afterPut_returnsTrue() {
        map.put("ABC1", "Alice");
        assertTrue(map.containsKey("ABC1"));
    }

    @Test
    public void testCollisionHandling_smallTableManyKeys() {
        // Force lots of collisions by using a tiny table.
        CustomHashMap<String> tiny = new CustomHashMap<>(3);
        for (int i = 0; i < 30; i++) {
            tiny.put("KEY" + i, "VAL" + i);
        }
        // Every key must still be retrievable even though buckets are crowded.
        for (int i = 0; i < 30; i++) {
            assertEquals("VAL" + i, tiny.get("KEY" + i));
        }
        assertEquals(30, tiny.size());
    }

    @Test
    public void testKeys_returnsAllKeys() {
        map.put("ABC1", "Alice");
        map.put("DEF2", "Bob");
        map.put("GHI3", "Carol");
        String[] keys = map.keys();
        assertEquals(3, keys.length);
        // Hash maps don't guarantee iteration order — check membership.
        boolean foundAlice = false, foundBob = false, foundCarol = false;
        for (String k : keys) {
            if ("ABC1".equals(k)) foundAlice = true;
            if ("DEF2".equals(k)) foundBob   = true;
            if ("GHI3".equals(k)) foundCarol = true;
        }
        assertTrue(foundAlice && foundBob && foundCarol);
    }
}
