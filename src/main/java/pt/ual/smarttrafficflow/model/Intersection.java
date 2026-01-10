package pt.ual.smarttrafficflow.model;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
    private List<TrafficLight> lights;

    public Intersection() {
        this.lights = new ArrayList<>();
    }

    public void addLight(TrafficLight light) { lights.add(light); }
    public List<TrafficLight> getLights() { return lights; }
}