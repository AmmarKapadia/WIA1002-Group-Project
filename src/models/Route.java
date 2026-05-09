/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;

/**
 * Immutable result of a Dijkstra path search: the ordered list of vertex
 * names from source to destination, plus the total path weight.
 *
 * @author Yim Zi Hao
 */
public class Route {
    private final ArrayList<String> path;
    private final int totalDistance;

    public Route(ArrayList<String> path, int totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
