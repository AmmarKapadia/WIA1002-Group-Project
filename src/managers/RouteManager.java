/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import datastructures.CustomGraph;
import models.Route;

/**
 * High-level API for the route navigation feature. Looks up the shortest
 * driving path from the entry gate to a given destination using
 * DijkstraSolver, and prints turn-by-turn directions.
 *
 * @author Yim Zi Hao
 */
public class RouteManager {

    /** Canonical name of the entry-gate vertex in the parking-lot graph. */
    private static final String GATE_ID = "GATE";

    private CustomGraph graph;
    private DijkstraSolver solver;

    public RouteManager(DijkstraSolver solver) {
        this.solver = solver;
    }

    public void setGraph(CustomGraph graph) {
        this.graph = graph;
    }

    /**
     * Get the shortest route from GATE to a specific slot or vertex.
     *
     * @param destination
     * @return Route to destination, or null if unreachable / not in graph
     */
    public Route getRoute(String destination) {
        if (graph == null) {
            System.out.println("No graph loaded.");
            return null;
        }
        if (!graph.hasVertex(destination)) {
            System.out.println("Destination not found in graph: " + destination);
            return null;
        }
        return solver.solve(graph, GATE_ID, destination);
    }

    /**
     * Prints turn-by-turn directions for a given route.
     * Output format: "GATE → INT_A → INT_C → S03    Total distance: 18 m"
     *
     * @param route
     */
    public void printDirections(Route route) {
        if (route == null) {
            System.out.println("No route available.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < route.getPath().size(); i++) {
            sb.append(route.getPath().get(i));
            if (i < route.getPath().size() - 1) {
                sb.append(" → ");
            }
        }
        sb.append("    Total distance: ").append(route.getTotalDistance()).append(" m");
        System.out.println(sb.toString());
    }

    /**
     * Convenience method — get and print in one call.
     *
     * @param destination
     */
    public void navigateTo(String destination) {
        Route route = getRoute(destination);
        printDirections(route);
    }
}
