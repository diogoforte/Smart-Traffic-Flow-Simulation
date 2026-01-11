package pt.ual.smarttrafficflow.model;

import pt.ual.smarttrafficflow.model.state.GreenState;
import pt.ual.smarttrafficflow.model.state.TrafficLightState;

public class TrafficLight {
    private final double x;
    private final double y;
    private TrafficLightState state;

    public TrafficLight(double x, double y) {
        this(x, y, new GreenState());
    }

    public TrafficLight(double x, double y, TrafficLightState initial) {
        this.x = x;
        this.y = y;
        this.state = initial;
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void update(double deltaTime) {
        state.update(this, deltaTime);
    }

    public Color getCurrentColor() {
        return state.getColor();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public enum Color {RED, YELLOW, GREEN}
}