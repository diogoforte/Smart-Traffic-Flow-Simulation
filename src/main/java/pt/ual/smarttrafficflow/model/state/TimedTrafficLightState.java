package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

// Classe base para estados de semáforo que dependem de um temporizador
public abstract class TimedTrafficLightState implements TrafficLightState {
    private final double duration;
    private double elapsed = 0;

    protected TimedTrafficLightState(double duration) {
        this.duration = duration;
    }

    @Override
    public void update(TrafficLight light, double deltaTime) {
        elapsed += deltaTime;
        // Verifica se o tempo de duração do estado expirou
        if (elapsed >= duration) {
            elapsed = 0;
            onFinish(light);
        }
    }

    // Método abstrato chamado quando o tempo termina
    protected abstract void onFinish(TrafficLight light);
}