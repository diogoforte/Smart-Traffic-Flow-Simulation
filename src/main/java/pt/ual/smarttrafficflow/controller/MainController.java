package pt.ual.smarttrafficflow.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import pt.ual.smarttrafficflow.model.World;

public class MainController {
    private World world;
    private SimulationController simController;

    @FXML
    private Canvas mainCanvas;

    public void initialize() {
        this.world = new World();
        this.simController = new SimulationController(world);
        // Aqui vais configurar o AnimationTimer para o desenho
    }

    @FXML
    protected void onStartStopClick() {
        simController.togglePause();
    }
}