/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import datastructures.CustomGraph;
import datastructures.CustomMinHeap;
import datastructures.CustomMinHeap.Entry;
import datastructures.Edge;
import datastructures.EdgeLinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import models.Route;

/**
 * This class is used to apply Dijkstra Algorithm to get minimum distance
 * between two vertices.
 *
 * @author Yim Zi Hao
 */
public class DijkstraSolver {

    /**
     * Calculate the shortest distance from two vertices in a graph.
     *
     * @param graph
     * @param source
     * @param destination
     * @return
     */
    public Route solve(CustomGraph graph, String source, String destination) {
        // TODO(Sprint 2 cleanup): Replace java.util.HashMap with CustomHashMap<Integer>
        // and java.util.HashMap<String,String> with CustomHashMap<String> once Member 1
        // genericises CustomHashMap. The rubric awards marks for using custom DS.
        
        // (1) Initialise a distance map: source = 0, all others = Integer.MAX_VALUE.
        HashMap<String, Integer> dist = new HashMap<>();
        for (String v : graph.getAllVertices()) {
            dist.put(v, Integer.MAX_VALUE);
        }
        dist.put(source, 0);

        // (2) Initialise a predecessor map.
        HashMap<String, String> prev = new HashMap<>();
        for (String v : graph.getAllVertices()) {
            prev.put(v, null);
        }

        // (3) Push (source, 0) into the min-heap
        CustomMinHeap heap = new CustomMinHeap();
        heap.offer(source, 0);

        // (4) While heap not empty: pop (u, du); if u == destination → reconstruct path; 
        // otherwise relax every edge (u, v, w): 
        // if du + w < dist[v], update dist[v], predecessor[v] = u, push (v, dist[v]). 
        while (!heap.isEmpty()) {
            Entry current = heap.poll();
            String u = current.key;
            int du = current.distance;

            // if u == destination → reconstruct path
            if (u.equals(destination)) {
                return reconstructPath(prev, destination, dist.get(destination));
            }

            // lazy deletion — skip if a shorter path to u was already found
            if (du > dist.get(u)) {
                continue;
            }

            // relax every edge (u, v, w):
            EdgeLinkedList neighbours = graph.getNeighbors(u);
            if (neighbours == null) {
                continue;
            }

            for (Edge edge : neighbours) {
                String v = edge.getDestination();
                int newDist = du + edge.getWeight();
                if (dist.containsKey(v) && newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    heap.offer(v, newDist);
                }
            }
        }
        return null;
    }

    /**
     * Rebuilds the shortest path from source to destination by walking the
     * predecessor map backwards.
     *
     * @param prev
     * @param destination
     * @param totalDist
     * @return
     */
    private Route reconstructPath(HashMap<String, String> prev, String destination, int totalDist) {
        ArrayList<String> path = new ArrayList<>();
        String current = destination;

        // walk backwards from destination to source
        while (current != null) {
            path.add(0, current); // insert at front to reverse
            current = prev.get(current);
        }

        return new Route(path, totalDist);
    }
}
