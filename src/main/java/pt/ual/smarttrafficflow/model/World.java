package pt.ual.smarttrafficflow.model;

import java.util.ArrayList;
import java.util.List;

public class World {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<TrafficLight> lights = new ArrayList<>();

    // O tick agora recebe o mapa 'char[][] map'
    public void tick(double deltaTime, char[][] map) {
        for (TrafficLight light : lights) {
            light.update(deltaTime);
        }
        for (Vehicle v : vehicles) {
            // Passa o mapa para o veículo decidir se anda H ou V
            v.update(deltaTime, this, map);
        }
    }

    public void addVehicle(Vehicle v) {
        vehicles.add(v);
    }

    public void addLight(TrafficLight l) {
        lights.add(l);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<TrafficLight> getLights() {
        return lights;
    }
}