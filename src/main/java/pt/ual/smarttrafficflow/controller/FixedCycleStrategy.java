package pt.ual.smarttrafficflow.controller;

import pt.ual.smarttrafficflow.model.Intersection;
import pt.ual.smarttrafficflow.model.TrafficLight;

public class FixedCycleStrategy implements TrafficStrategy {
    @Override
    public void applyStrategy(Intersection intersection, double deltaTime) {
        // Simplesmente atualiza cada semáforo com base no tempo real
        for (TrafficLight light : intersection.getLights()) {
            light.update(deltaTime);
        }
    }
}