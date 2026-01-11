package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

// Representa o estado vermelho de um semáforo
public class RedState extends TimedTrafficLightState {
    public RedState() {
        super(5.0); // Duração de 5 segundos
    }

    @Override
    protected void onFinish(TrafficLight light) {
        // Muda para o estado verde após o tempo terminar
        light.setState(new GreenState());
    }

    @Override
    public TrafficLight.Color getColor() {
        return TrafficLight.Color.RED;
    }
}