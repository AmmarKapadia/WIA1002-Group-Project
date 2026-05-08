package datastructures;

/**
 * Adjacency-list graph for the parking-lot layout.
 * Vertex names are Strings (e.g. "GATE", "INT_A", "S01"); each maps to an
 * EdgeLinkedList of outgoing edges. Undirected by default — addEdge(A, B, w)
 * inserts both A→B and B→A. Use addDirectedEdge for one-way roads.
 */
public class CustomGraph {

    // Local hash map: vertex name -> EdgeLinkedList of outgoing edges.
    // (Will be replaced by the team's generic CustomHashMap once Member 1
    //  refactors it; kept self-contained for now to unblock Sprint 2.)
    private class GraphHashMap {
        private class HashNode {
            String key;
            EdgeLinkedList value;
            HashNode next;
            HashNode(String k, EdgeLinkedList v) { this.key = k; this.value = v; }
        }

        private HashNode[] table;
        private int size;

        public GraphHashMap(int capacity) {
            table = new HashNode[capacity];
            size = 0;
        }

        public void put(String key, EdgeLinkedList value) {
            int index = Math.abs(key.hashCode() % table.length);
            HashNode current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            HashNode newNode = new HashNode(key, value);
            newNode.next = table[index];
            table[index] = newNode;
            size++;
        }

        public EdgeLinkedList get(String key) {
            int index = Math.abs(key.hashCode() % table.length);
            HashNode current = table[index];
            while (current != null) {
                if (current.key.equals(key)) return current.value;
                current = current.next;
            }
            return null;
        }

        public boolean containsKey(String key) {
            return get(key) != null;
        }

        public int size() {
            return size;
        }

        /** Returns every key currently in the map. Used by getAllVertices(). */
        public String[] keys() {
            String[] result = new String[size];
            int idx = 0;
            for (int i = 0; i < table.length; i++) {
                HashNode current = table[i];
                while (current != null) {
                    result[idx++] = current.key;
                    current = current.next;
                }
            }
            return result;
        }
    }

    private GraphHashMap adjacencyList;

    public CustomGraph() {
        this.adjacencyList = new GraphHashMap(20);
    }

    /** Add a vertex if it doesn't already exist. */
    public void addVertex(String label) {
        if (!adjacencyList.containsKey(label)) {
            adjacencyList.put(label, new EdgeLinkedList());
        }
    }

    /** Add an UNDIRECTED edge — inserts both src→dst and dst→src. */
    public void addEdge(String src, String dst, int weight) {
        addVertex(src);
        addVertex(dst);
        adjacencyList.get(src).add(new Edge(src, dst, weight));
        adjacencyList.get(dst).add(new Edge(dst, src, weight));
    }

    /** Add a one-way edge — only src→dst. Use for one-way roads. */
    public void addDirectedEdge(String src, String dst, int weight) {
        addVertex(src);
        addVertex(dst);
        adjacencyList.get(src).add(new Edge(src, dst, weight));
    }

    /** Returns the EdgeLinkedList of outgoing edges for the given vertex,
     *  or null if the vertex isn't in the graph. */
    public EdgeLinkedList getNeighbors(String vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean hasVertex(String vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /** Returns every vertex name currently in the graph. */
    public String[] getAllVertices() {
        return adjacencyList.keys();
    }

    /** Prints every vertex and its neighbours with edge weights.
     *  Used by the "Show Parking Map" menu option in Sprint 2. */
    public void printGraph() {
        System.out.println("\n=== Parking Map Layout (Graph) ===");
        String[] vertices = getAllVertices();
        if (vertices.length == 0) {
            System.out.println("(graph is empty)");
            return;
        }
        for (String v : vertices) {
            System.out.print("  " + v + " -> ");
            EdgeLinkedList neighbors = getNeighbors(v);
            if (neighbors == null || neighbors.isEmpty()) {
                System.out.println("(no neighbours)");
                continue;
            }
            boolean first = true;
            for (Edge e : neighbors) {
                if (!first) System.out.print(", ");
                System.out.print(e.getDestination() + " (" + e.getWeight() + "m)");
                first = false;
            }
            System.out.println();
        }
    }
}
