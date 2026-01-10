package pt.ual.smarttrafficflow.model.state;

import pt.ual.smarttrafficflow.model.TrafficLight;

public class YellowState extends TimedTrafficLightState {
    public YellowState() { super(2.0); } // 2 seconds
    @Override
    protected void onFinish(TrafficLight light) { light.setState(new RedState()); }
    @Override
    public TrafficLight.Color getColor() { return TrafficLight.Color.YELLOW; }
}