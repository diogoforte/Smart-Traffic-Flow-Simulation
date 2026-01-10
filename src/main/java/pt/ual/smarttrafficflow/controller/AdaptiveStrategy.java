package pt.ual.smarttrafficflow.controller;

import pt.ual.smarttrafficflow.model.Intersection;

public class AdaptiveStrategy implements TrafficStrategy {
    @Override
    public void applyStrategy(Intersection intersection, double deltaTime) {
        // Lógica futura: Se houver muitos carros numa fila,
        // prolongar o tempo do GreenState.
    }
}