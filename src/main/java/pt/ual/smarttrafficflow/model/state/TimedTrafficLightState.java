package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

public abstract class TimedTrafficLightState implements TrafficLightState {
    private final double duration;
    private double elapsed = 0;

    protected TimedTrafficLightState(double duration) {
        this.duration = duration;
    }

    @Override
    public void update(TrafficLight light, double deltaTime) {
        elapsed += deltaTime;
        if (elapsed >= duration) {
            elapsed = 0;
            onFinish(light);
        }
    }

    protected abstract void onFinish(TrafficLight light);
}