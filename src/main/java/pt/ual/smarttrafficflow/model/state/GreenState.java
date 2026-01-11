package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

// Representa o estado verde de um semáforo
public class GreenState extends TimedTrafficLightState {
    public GreenState() {
        super(5.0); // Duração de 5 segundos
    }

    @Override
    protected void onFinish(TrafficLight light) {
        // Muda para o estado amarelo após o tempo terminar
        light.setState(new YellowState());
    }

    @Override
    public TrafficLight.Color getColor() {
        return TrafficLight.Color.GREEN;
    }
}