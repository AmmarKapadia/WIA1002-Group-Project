/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javafx.util.Duration;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Route;

/**
 *
 * @author Yim Zi Hao
 */
public class RouteOverlayHelper {
    
    private PauseTransition activeTimer; // so a new highlight can cancel an old one
    
    public void highlightRoute(Route route, MapPaneController mapController) {
        if (route == null || mapController == null) {
            return;
        }
        
        // 1. Cancel any previous highlight timer still running
        if (activeTimer != null) {
            activeTimer.stop();
        }
        
        // 2. Draw the route on top of the map
        GraphicsContext gc = mapController.getCanvas().getGraphicsContext2D();
        gc.setStroke(Color.web("#C9985C")); // maple-gold
        gc.setLineWidth(4.0);
        ArrayList<String> path = route.getPath();
        for (int i = 0; i < path.size() - 1; i++) {
            double[] from = mapController.getNodePosition(path.get(i));
            double[] to = mapController.getNodePosition(path.get(i + 1));
            if (from != null && to != null) {
                gc.strokeLine(from[0], from[1], to[0], to[1]);
            }
        }
        
        // 3. Schedule the auto-clear after 5 seconds
        activeTimer = new PauseTransition(Duration.seconds(5));
        activeTimer.setOnFinished(e -> mapController.refresh());
        activeTimer.play();
    }
}
