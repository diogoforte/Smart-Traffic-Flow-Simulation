package pt.ual.smarttrafficflow.model;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private String name;
    private List<Vehicle> vehiclesOnRoad;

    public Road(String name) {
        this.name = name;
        this.vehiclesOnRoad = new ArrayList<>();
    }

    public void addVehicle(Vehicle v) { vehiclesOnRoad.add(v); }
    public List<Vehicle> getVehicles() { return vehiclesOnRoad; }
}