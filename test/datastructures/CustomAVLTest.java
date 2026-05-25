package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import models.Vehicle;

/**
 * Tests for CustomAVL — self-balancing BST keyed by Vehicle.licensePlate.
 * insert() takes a Vehicle, search() returns a Vehicle (or null).
 * Covers: empty, insert, search hit/miss, all four rotation cases,
 * delete with return value, height bound after many inserts.
 *
 * @author Xu Zimao
 */
public class CustomAVLTest {

    private CustomAVL tree;

    @Before
    public void setUp() {
        tree = new CustomAVL();
    }

    @Test
    public void testEmpty_searchReturnsNull() {
        assertNull(tree.search("ABC1"));
    }

    @Test
    public void testEmpty_deleteReturnsFalse() {
        assertFalse(tree.delete("NOPE"));
    }

    @Test
    public void testInsertSingle_searchFindsIt() {
        Vehicle v = makeVehicle("ABC1");
        tree.insert(v);
        assertNotNull(tree.search("ABC1"));
        assertEquals("ABC1", tree.search("ABC1").getLicensePlate());
    }

    @Test
    public void testInsertMultiple_allRetrievable() {
        tree.insert(makeVehicle("ABC1"));
        tree.insert(makeVehicle("DEF2"));
        tree.insert(makeVehicle("GHI3"));
        assertNotNull(tree.search("ABC1"));
        assertNotNull(tree.search("DEF2"));
        assertNotNull(tree.search("GHI3"));
    }

    @Test
    public void testSearch_missReturnsNull() {
        tree.insert(makeVehicle("ABC1"));
        assertNull(tree.search("XYZ9"));
    }

    @Test
    public void testLLRotation_stillSearchable() {
        // Descending inserts → left-heavy → triggers LL rotation
        tree.insert(makeVehicle("ZZZ"));
        tree.insert(makeVehicle("YYY"));
        tree.insert(makeVehicle("XXX"));
        assertNotNull(tree.search("XXX"));
        assertNotNull(tree.search("YYY"));
        assertNotNull(tree.search("ZZZ"));
        // After LL rotation the height should be exactly 2 (3 nodes balanced).
        assertEquals(2, tree.getRootHeight());
    }

    @Test
    public void testRRRotation_stillSearchable() {
        // Ascending inserts → right-heavy → triggers RR rotation
        tree.insert(makeVehicle("AAA"));
        tree.insert(makeVehicle("BBB"));
        tree.insert(makeVehicle("CCC"));
        assertNotNull(tree.search("AAA"));
        assertNotNull(tree.search("BBB"));
        assertNotNull(tree.search("CCC"));
        assertEquals(2, tree.getRootHeight());
    }

    @Test
    public void testLRRotation_stillSearchable() {
        // Sequence that triggers left-right rotation
        tree.insert(makeVehicle("CCC"));
        tree.insert(makeVehicle("AAA"));
        tree.insert(makeVehicle("BBB"));
        assertNotNull(tree.search("AAA"));
        assertNotNull(tree.search("BBB"));
        assertNotNull(tree.search("CCC"));
        assertEquals(2, tree.getRootHeight());
    }

    @Test
    public void testRLRotation_stillSearchable() {
        // Sequence that triggers right-left rotation
        tree.insert(makeVehicle("AAA"));
        tree.insert(makeVehicle("CCC"));
        tree.insert(makeVehicle("BBB"));
        assertNotNull(tree.search("AAA"));
        assertNotNull(tree.search("BBB"));
        assertNotNull(tree.search("CCC"));
        assertEquals(2, tree.getRootHeight());
    }

    @Test
    public void testDelete_existingKey_returnsTrue() {
        tree.insert(makeVehicle("ABC1"));
        tree.insert(makeVehicle("DEF2"));
        tree.insert(makeVehicle("GHI3"));
        boolean removed = tree.delete("DEF2");
        assertTrue(removed);
        assertNull(tree.search("DEF2"));
        // Others still findable
        assertNotNull(tree.search("ABC1"));
        assertNotNull(tree.search("GHI3"));
    }

    @Test
    public void testDelete_missingKey_returnsFalse() {
        tree.insert(makeVehicle("ABC1"));
        assertFalse(tree.delete("NOPE"));
        assertNotNull(tree.search("ABC1"));  // existing data untouched
    }

    @Test
    public void testManyInsertions_heightStaysLog2Bound() {
        // Insert 15 sorted vehicles. A plain BST would degenerate to height 15;
        // AVL guarantees height ≤ ~1.44 * log2(n+2). For n=15 that's about 6.
        String[] plates = {"A1","A2","A3","A4","A5","A6","A7","A8",
                           "A9","B0","B1","B2","B3","B4","B5"};
        for (String p : plates) tree.insert(makeVehicle(p));
        for (String p : plates) {
            assertNotNull("Should find " + p, tree.search(p));
        }
        // For 15 nodes, AVL height bound is ~5-6. Plain BST would be 15.
        assertTrue("AVL height should be ≤ 6 for 15 sorted inserts; was "
                   + tree.getRootHeight(),
                   tree.getRootHeight() <= 6);
    }

    /** Helper: builds a Vehicle with the given plate (other fields are dummy). */
    private Vehicle makeVehicle(String plate) {
        return new Vehicle(plate, "Owner_" + plate, System.currentTimeMillis());
    }
}
