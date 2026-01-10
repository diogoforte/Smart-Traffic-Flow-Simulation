package pt.ual.smarttrafficflow.controller;

import pt.ual.smarttrafficflow.model.World;

public class SimulationController {
    private World world;
    private boolean running = true; // Começar a correr por defeito
    private double timeScale = 1.0;

    public SimulationController(World world) {
        this.world = world;
    }

    // Corrigido: Agora aceita o mapData para passar ao world.tick
    public void update(double deltaTime, char[][] mapData) {
        if (running && mapData != null) {
            world.tick(deltaTime * timeScale, mapData);
        }
    }

    public void togglePause() { this.running = !this.running; }
    public void setTimeScale(double scale) { this.timeScale = scale; }

    public void reset(World newWorld) {
        this.world = newWorld;
    }
}