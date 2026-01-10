package pt.ual.smarttrafficflow.view;

                           import javafx.scene.canvas.Canvas;
                           import javafx.scene.canvas.GraphicsContext;
                           import javafx.scene.paint.Color;
                           import javafx.scene.paint.CycleMethod;
                           import javafx.scene.paint.LinearGradient;
                           import javafx.scene.paint.RadialGradient;
                           import javafx.scene.paint.Stop;
                           import javafx.scene.transform.Rotate;
                           import pt.ual.smarttrafficflow.model.*;
                           import pt.ual.smarttrafficflow.util.Config;

                           public class SimulationCanvas extends Canvas {
                               private final World world;
                               private final int TILE = Config.TILE_SIZE;

                               public SimulationCanvas(double w, double h, World world) {
                                   super(w, h);
                                   this.world = world;
                               }

                               public void draw(char[][] map) {
                                   GraphicsContext gc = getGraphicsContext2D();
                                   gc.clearRect(0, 0, getWidth(), getHeight());

                                   int rows = map.length;
                                   int cols = map[0].length;

                                   for (int r = 0; r < rows; r++) {
                                       for (int c = 0; c < cols; c++) {
                                           char t = map[r][c];
                                           double x = c * TILE, y = r * TILE;

                                           // 0: Relva (Base)
                                           gc.setFill(Color.web("#4CAF50"));
                                           gc.fillRect(x, y, TILE, TILE);
                                           gc.setFill(Color.web("#43A047"));
                                           gc.fillRect(x + 5, y + 5, 2, 2);
                                           gc.fillRect(x + TILE - 10, y + TILE - 10, 2, 2);

                                           if (t == '1') { // Estrada
                                               gc.setFill(Color.web("#263238"));
                                               gc.fillRect(x, y, TILE, TILE);

                                               boolean hasN = (r > 0 && map[r - 1][c] == '1');
                                               boolean hasS = (r < rows - 1 && map[r + 1][c] == '1');
                                               boolean hasW = (c > 0 && map[r][c - 1] == '1');
                                               boolean hasE = (c < cols - 1 && map[r][c + 1] == '1');

                                               // Desenhar Guias/Bordas apenas onde não há estrada
                                               gc.setFill(Color.web("#546E7A"));
                                               if (!hasN) gc.fillRect(x, y, TILE, 3);
                                               if (!hasS) gc.fillRect(x, y + TILE - 3, TILE, 3);
                                               if (!hasW) gc.fillRect(x, y, 3, TILE);
                                               if (!hasE) gc.fillRect(x + TILE - 3, y, 3, TILE);

                                               // Linhas tracejadas centrais
                                               gc.setStroke(Color.web("#FFFFFF", 0.5));
                                               gc.setLineDashes(10);
                                               gc.setLineWidth(1.5);
                                               if (hasN || hasS) {
                                                   gc.strokeLine(x + TILE / 2.0, y, x + TILE / 2.0, y + TILE);
                                               }
                                               if (hasW || hasE) {
                                                   gc.strokeLine(x, y + TILE / 2.0, x + TILE, y + TILE / 2.0);
                                               }
                                               gc.setLineDashes(0);
                                           } else if (t == '3') { // Casa
                                               gc.setFill(Color.web("#D7CCC8"));
                                               gc.fillRect(x + 10, y + 18, TILE - 20, TILE - 25);
                                               gc.setFill(Color.web("#A52A2A"));
                                               gc.fillPolygon(new double[]{x + 5, x + TILE / 2.0, x + TILE - 5}, new double[]{y + 18, y + 5, y + 18}, 3);
                                               gc.setFill(Color.web("#5D4037"));
                                               gc.fillRect(x + TILE / 2.0 - 4, y + TILE - 15, 8, 8);
                                           } else if (t == '4') { // Prédio
                                               LinearGradient buildingGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#455A64")), new Stop(1, Color.web("#263238")));
                                               gc.setFill(buildingGrad);
                                               gc.fillRect(x + 5, y + 5, TILE - 10, TILE - 10);
                                               gc.setFill(Color.web("#FFEB3B", 0.8));
                                               for (int i = 0; i < 3; i++) {
                                                   for (int j = 0; j < 2; j++) {
                                                       gc.fillRect(x + 12 + j * 15, y + 12 + i * 12, 6, 6);
                                                   }
                                               }
                                           } else if (t == '5') { // Parque
                                               gc.setFill(Color.web("#81C784"));
                                               gc.fillRect(x, y, TILE, TILE);
                                               gc.setFill(Color.web("#BCAAA4"));
                                               gc.fillRoundRect(x + 5, y + TILE / 2.0 - 2, TILE - 10, 4, 2, 2);
                                               gc.setFill(Color.web("#E91E63"));
                                               gc.fillOval(x + 10, y + 10, 4, 4);
                                               gc.fillOval(x + TILE - 15, y + TILE - 15, 4, 4);
                                           } else if (t == '6') { // Candeeiro
                                               gc.setFill(Color.web("#212121"));
                                               gc.fillRect(x + TILE / 2.0 - 2, y + 12, 4, TILE - 12);
                                               RadialGradient glow = new RadialGradient(0, 0, x + TILE / 2.0, y + 8, 15, false, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#FFF176", 0.6)), new Stop(1, Color.TRANSPARENT));
                                               gc.setFill(glow);
                                               gc.fillOval(x + TILE / 2.0 - 15, y + 8 - 15, 30, 30);
                                               gc.setFill(Color.web("#FDD835"));
                                               gc.fillOval(x + TILE / 2.0 - 5, y + 3, 10, 10);
                                           } else if (t == '7') { // Estacionamento
                                               gc.setFill(Color.web("#455A64"));
                                               gc.fillRect(x, y, TILE, TILE);
                                               gc.setStroke(Color.web("#CFD8DC"));
                                               gc.setLineWidth(2);
                                               gc.strokeLine(x + 5, y + 5, x + TILE - 5, y + 5);
                                               gc.strokeLine(x + 5, y + TILE - 5, x + TILE - 5, y + TILE - 5);
                                               gc.setLineWidth(1);
                                           } else if (t == '8') { // Água
                                               LinearGradient waterGrad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#0288D1")), new Stop(1, Color.web("#01579B")));
                                               gc.setFill(waterGrad);
                                               gc.fillOval(x + 2, y + 2, TILE - 4, TILE - 4);
                                               gc.setStroke(Color.web("#B3E5FC", 0.5));
                                               gc.strokeOval(x + 6, y + 6, TILE - 12, TILE - 12);
                                           } else if (t == '9') { // Árvore
                                               gc.setFill(Color.web("#5D4037"));
                                               gc.fillRect(x + TILE / 2.0 - 3, y + TILE - 15, 6, 10);
                                               gc.setFill(Color.web("#2E7D32"));
                                               gc.fillOval(x + 8, y + 5, TILE - 16, TILE - 15);
                                               gc.setFill(Color.web("#388E3C"));
                                               gc.fillOval(x + 12, y + 2, TILE - 24, TILE - 20);
                                           }
                                       }
                                   }

                                   for (TrafficLight light : world.getLights()) {
                                       gc.setFill(Color.web("#212121"));
                                       gc.fillRoundRect(light.getX() - 4, light.getY() - 4, 20, 20, 6, 6);
                                       gc.setFill(switch (light.getCurrentColor()) {
                                           case GREEN -> Color.web("#00E676");
                                           case YELLOW -> Color.web("#FFEA00");
                                           default -> Color.web("#FF1744");
                                       });
                                       gc.fillOval(light.getX(), light.getY(), 12, 12);
                                   }

                                   for (Vehicle v : world.getVehicles()) {
                                       gc.save();
                                       Rotate rot = new Rotate(v.getVAngle(), v.getVX() + v.getWidth() / 2, v.getVY() + v.getHeight() / 2);
                                       gc.setTransform(rot.getMxx(), rot.getMyx(), rot.getMxy(), rot.getMyy(), rot.getTx(), rot.getTy());

                                       gc.setFill(Color.web("#000000", 0.2));
                                       gc.fillRoundRect(v.getVX() + 2, v.getVY() + 2, v.getWidth(), v.getHeight(), 8, 8);

                                       gc.setFill(Color.web(v.getColorHex()));
                                       gc.fillRoundRect(v.getVX(), v.getVY(), v.getWidth(), v.getHeight(), 8, 8);

                                       if (v.isIgnoresLights()) {
                                           long time = System.currentTimeMillis();
                                           boolean toggle = (time / 150) % 2 == 0;
                                           gc.setFill(toggle ? Color.BLUE : Color.RED);
                                           gc.fillOval(v.getVX() + v.getWidth() * 0.3, v.getVY() + 2, 6, 6);
                                           gc.setFill(toggle ? Color.RED : Color.BLUE);
                                           gc.fillOval(v.getVX() + v.getWidth() * 0.3, v.getVY() + v.getHeight() - 8, 6, 6);
                                       }

                                       gc.setFill(Color.web("#FFF9C4", 0.8));
                                       gc.fillOval(v.getVX() + v.getWidth() - 6, v.getVY() + 2, 5, 4);
                                       gc.fillOval(v.getVX() + v.getWidth() - 6, v.getVY() + v.getHeight() - 6, 5, 4);

                                       gc.restore();
                                   }
                               }
                           }