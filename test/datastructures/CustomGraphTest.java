package datastructures;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CustomGraph — adjacency-list graph with String vertex labels and
 * Edge-LinkedList neighbour lists. Covers: empty graph, addVertex, addEdge
 * bidirectional semantics, hasVertex, getAllVertices, getNeighbors.
 *
 * @author Nicolas
 */
public class CustomGraphTest {

    private CustomGraph graph;

    @Before
    public void setUp() {
        graph = new CustomGraph();
    }

    @Test
    public void testEmpty_hasVertexFalse() {
        assertFalse(graph.hasVertex("GATE"));
    }

    @Test
    public void testEmpty_getAllVerticesEmpty() {
        String[] vertices = graph.getAllVertices();
        assertEquals(0, vertices.length);
    }

    @Test
    public void testAddVertex_thenHasVertex() {
        graph.addVertex("GATE");
        assertTrue(graph.hasVertex("GATE"));
    }

    @Test
    public void testAddEdge_bothVerticesExist() {
        graph.addEdge("GATE", "INT_A", 5);
        assertTrue(graph.hasVertex("GATE"));
        assertTrue(graph.hasVertex("INT_A"));
    }

    @Test
    public void testAddEdge_isBidirectional() {
        // addEdge() inserts both A→B and B→A. Both should have non-empty
        // neighbour lists.
        graph.addEdge("GATE", "INT_A", 5);
        assertFalse(graph.getNeighbors("GATE").isEmpty());
        assertFalse(graph.getNeighbors("INT_A").isEmpty());
    }

    @Test
    public void testAddEdge_neighborContainsDestination() {
        graph.addEdge("GATE", "INT_A", 5);
        EdgeLinkedList neighbors = graph.getNeighbors("GATE");
        boolean found = false;
        int foundWeight = -1;
        for (Edge e : neighbors) {
            if (e.getDestination().equals("INT_A")) {
                found = true;
                foundWeight = e.getWeight();
            }
        }
        assertTrue("GATE should have INT_A as neighbour", found);
        assertEquals(5, foundWeight);
    }

    @Test
    public void testMultipleEdges_allVerticesPresent() {
        graph.addEdge("GATE",  "INT_A", 5);
        graph.addEdge("GATE",  "INT_B", 6);
        graph.addEdge("INT_A", "S01",   3);
        graph.addEdge("INT_B", "S02",   4);
        assertTrue(graph.hasVertex("GATE"));
        assertTrue(graph.hasVertex("INT_A"));
        assertTrue(graph.hasVertex("INT_B"));
        assertTrue(graph.hasVertex("S01"));
        assertTrue(graph.hasVertex("S02"));
    }

    @Test
    public void testGetAllVertices_countMatches() {
        graph.addEdge("GATE",  "INT_A", 5);
        graph.addEdge("INT_A", "S01",   3);
        graph.addEdge("INT_A", "S02",   4);
        String[] vertices = graph.getAllVertices();
        // 4 distinct vertices: GATE, INT_A, S01, S02
        assertEquals(4, vertices.length);
    }

    @Test
    public void testGetNeighbors_nonExistentVertex_returnsNull() {
        graph.addEdge("GATE", "INT_A", 5);
        assertNull(graph.getNeighbors("NOWHERE"));
    }

    @Test
    public void testAddVertex_duplicate_noChangeToVertexCount() {
        graph.addVertex("GATE");
        graph.addVertex("GATE");
        assertEquals(1, graph.getAllVertices().length);
    }
}
