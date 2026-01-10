package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

public class RedState extends TimedTrafficLightState {
    public RedState() { super(5.0); } // 5 seconds
    @Override
    protected void onFinish(TrafficLight light) { light.setState(new GreenState()); }
    @Override
    public TrafficLight.Color getColor() { return TrafficLight.Color.RED; }
}