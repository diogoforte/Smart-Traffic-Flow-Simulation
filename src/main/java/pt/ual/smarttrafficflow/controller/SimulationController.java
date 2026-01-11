package pt.ual.smarttrafficflow.controller;

import pt.ual.smarttrafficflow.model.World;

// Controla o fluxo lógico da simulação (pausa, velocidade, atualizações)
public class SimulationController {
    private final World world;
    private double timeScale = 1.0;

    public SimulationController(World world) {
        this.world = world;
    }

    // Atualiza o estado do mundo se a simulação não estiver pausada
    public void update(double deltaTime, char[][] mapData) {
        if (mapData != null) {
            world.tick(deltaTime * timeScale, mapData);
        }
    }

    // Define a escala de tempo (ex: 2.0 para velocidade dupla)
    public void setTimeScale(double scale) {
        this.timeScale = scale;
    }
}