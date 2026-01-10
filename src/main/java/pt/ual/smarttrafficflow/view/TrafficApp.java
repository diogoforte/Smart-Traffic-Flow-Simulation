// java
            package pt.ual.smarttrafficflow.view;

            import javafx.animation.AnimationTimer;
            import javafx.application.Application;
            import javafx.geometry.Insets;
            import javafx.geometry.Pos;
            import javafx.scene.Scene;
            import javafx.scene.control.Alert;
            import javafx.scene.control.ChoiceDialog;
            import javafx.scene.control.Button;
            import javafx.scene.control.Label;
            import javafx.scene.control.Slider;
            import javafx.scene.layout.BorderPane;
            import javafx.scene.layout.HBox;
            import javafx.scene.layout.StackPane;
            import javafx.stage.Stage;
            import pt.ual.smarttrafficflow.controller.SimulationController;
            import pt.ual.smarttrafficflow.model.*;
            import pt.ual.smarttrafficflow.model.state.GreenState;
            import pt.ual.smarttrafficflow.model.state.YellowState;
            import pt.ual.smarttrafficflow.model.state.RedState;
            import pt.ual.smarttrafficflow.util.MapInvalidException;
            import pt.ual.smarttrafficflow.util.MapLoader;
            import java.io.File;
            import java.util.*;

            public class TrafficApp extends Application {
                private World world = new World();
                private SimulationCanvas canvas;
                private char[][] mapData;
                private SimulationController controller;
                private AnimationTimer timer;
                private double speedScale = 1.0;

                @Override
                public void start(Stage stage) {
                    // 1. Localizar pasta de mapas
                    File mapsFolder = new File("C:\\Users\\djngf\\Desktop\\SmartTrafficFlow\\src\\main\\resources\\pt\\ual\\smarttrafficflow\\maps");
                    String[] files = mapsFolder.list((dir, name) -> name.endsWith(".txt"));

                    if (files == null || files.length == 0) {
                        System.err.println("Erro: Pasta de mapas vazia ou inexistente.");
                        return;
                    }

                    // 2. Diálogo de Seleção
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(files[0], Arrays.asList(files));
                    dialog.setTitle("Selecionador de Mapas");
                    dialog.setHeaderText("Configuração da Simulação");
                    dialog.setContentText("Escolha o mapa:");

                    Optional<String> result = dialog.showAndWait();

                    if (result.isPresent()) {
                        try {
                            // 3. Carregar e Validar o Mapa (M5)
                            mapData = MapLoader.loadMap("maps/" + result.get(), world);

                            // Se o mapa for válido, inicia a simulação
                            setupSimulation(stage);

                        } catch (MapInvalidException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Mapa Inválido");
                            alert.setHeaderText("Falha na validação do cenário");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    }
                }

                private void setupSimulation(Stage stage) {
                    controller = new SimulationController(world);
                    int tileSize = 50;

                    double canvasWidth = mapData[0].length * tileSize;
                    double canvasHeight = mapData.length * tileSize;

                    canvas = new SimulationCanvas(canvasWidth, canvasHeight, world);

                    // Top controls: Back (emoji), speed slider, set-color buttons (emoji) and reset speed
                    Button backBtn = new Button("🔙"); // back emoji

                    // Slider for speed control
                    Slider speedSlider = new Slider(0.25, 8.0, 1.0);
                    speedSlider.setShowTickLabels(true);
                    speedSlider.setShowTickMarks(true);
                    speedSlider.setMajorTickUnit(1.0);
                    speedSlider.setMinorTickCount(3);
                    speedSlider.setBlockIncrement(0.25);
                    speedSlider.setPrefWidth(180);

                    Label speedLabel = new Label("Speed: 1.00x");

                    // Buttons to set all lights to a color (using circle emojis)
                    Button setGreenBtn = new Button("🟢"); // green ball emoji
                    Button setYellowBtn = new Button("🟡"); // yellow ball emoji
                    Button setRedBtn = new Button("🔴"); // red ball emoji

                    // Reset speed button
                    Button resetSpeedBtn = new Button("🔁");

                    backBtn.setOnAction(e -> {
                        // stop timer and reset world, then reopen picker
                        if (timer != null) timer.stop();
                        this.world = new World();
                        this.mapData = null;
                        this.controller = null;
                        this.canvas = null;
                        this.speedScale = 1.0;
                        try {
                            start(stage);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    // Slider listener updates controller timescale and label
                    speedSlider.valueProperty().addListener((obs, oldV, newV) -> {
                        double v = Math.round(newV.doubleValue() * 100.0) / 100.0;
                        speedLabel.setText(String.format("Speed: %.2fx", v));
                        if (controller != null) controller.setTimeScale(v);
                    });

                    // Reset speed action
                    resetSpeedBtn.setOnAction(e -> {
                        speedSlider.setValue(1.0);
                        speedLabel.setText("Speed: 1.00x");
                        if (controller != null) controller.setTimeScale(1.0);
                    });

                    setGreenBtn.setOnAction(e -> {
                        for (TrafficLight l : world.getLights()) {
                            l.setState(new GreenState());
                        }
                    });
                    setYellowBtn.setOnAction(e -> {
                        for (TrafficLight l : world.getLights()) {
                            l.setState(new YellowState());
                        }
                    });
                    setRedBtn.setOnAction(e -> {
                        for (TrafficLight l : world.getLights()) {
                            l.setState(new RedState());
                        }
                    });

                    HBox topBar = new HBox(8, backBtn, speedSlider, speedLabel, resetSpeedBtn, setGreenBtn, setYellowBtn, setRedBtn);
                    topBar.setPadding(new Insets(6));
                    topBar.setAlignment(Pos.CENTER_LEFT);

                    BorderPane root = new BorderPane();
                    root.setTop(topBar);
                    root.setCenter(new StackPane(canvas));

                    // AnimationTimer stored so Back can stop it
                    timer = new AnimationTimer() {
                        private long last = 0;

                        @Override
                        public void handle(long now) {
                            if (last == 0) {
                                last = now;
                                return;
                            }
                            double dt = (now - last) / 1_000_000_000.0;
                            last = now;

                            controller.update(dt, mapData);
                            canvas.draw(mapData);
                        }
                    };
                    timer.start();

                    Scene scene = new Scene(root, canvasWidth, canvasHeight);
                    stage.setScene(scene);
                    stage.setTitle("Smart Traffic Flow - Simulação Ativa");
                    stage.setResizable(false);
                    stage.show();
                }

                public static void main(String[] args) {
                    launch(args);
                }
            }