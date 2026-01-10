package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

public class GreenState extends TimedTrafficLightState {
    public GreenState() { super(5.0); } // 5 seconds
    @Override
    protected void onFinish(TrafficLight light) { light.setState(new YellowState()); }
    @Override
    public TrafficLight.Color getColor() { return TrafficLight.Color.GREEN; }
}