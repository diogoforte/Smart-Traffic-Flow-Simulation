package pt.ual.smarttrafficflow.controller;

import pt.ual.smarttrafficflow.model.Intersection;

public interface TrafficStrategy {
    // Define como os semáforos de uma intersecção devem comportar-se
    void applyStrategy(Intersection intersection, double deltaTime);
}