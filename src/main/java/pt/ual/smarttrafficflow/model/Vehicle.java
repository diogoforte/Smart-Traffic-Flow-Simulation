package pt.ual.smarttrafficflow.model;

import pt.ual.smarttrafficflow.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vehicle {
    private final double speed;
    private final int TILE = Config.TILE_SIZE;
    private final double width = 30, height = 15;
    private final String colorHex;
    private final boolean ignoresLights;
    private final Random rnd = new Random();
    private double x, y;
    private Direction dir;
    private double vX, vY, vAngle;
    private int lastCrossingX = -1;
    private int lastCrossingY = -1;

    public Vehicle(double x, double y, double speed, Direction dir, String colorHex, boolean ignoresLights) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.dir = dir;
        this.colorHex = colorHex;
        this.ignoresLights = ignoresLights;
        alignToTileLane((int) (x / TILE), (int) (y / TILE), dir);
        this.vX = this.x;
        this.vY = this.y;
        this.vAngle = getTargetAngle(dir);
    }

    private double getTargetAngle(Direction d) {
        return switch (d) {
            case EAST -> 0.0;
            case WEST -> 180.0;
            case NORTH -> 270.0;
            case SOUTH -> 90.0;
        };
    }

    private boolean isRoadTile(char t) {
        return t == '1';
    }

    public void update(double deltaTime, World world, char[][] map) {
        double intended = speed * deltaTime;
        double maxDx = dir.dx * intended;
        double maxDy = dir.dy * intended;

        final double minGap = 8.0;
        final double safetyZone = 20.0;
        final double lateralTolerance = 10.0;
        final double lightRadius = 6.0;

        for (Vehicle other : world.getVehicles()) {
            if (other == this) continue;
            if (dir == Direction.EAST) {
                if (other.x > this.x && other.x < this.x + width + safetyZone && Math.abs(other.y - this.y) < lateralTolerance) {
                    double allowed = (other.x - this.x) - width - minGap;
                    if (allowed < maxDx) maxDx = Math.max(0, allowed);
                }
            } else if (dir == Direction.WEST) {
                if (other.x < this.x && other.x > this.x - width - safetyZone && Math.abs(other.y - this.y) < lateralTolerance) {
                    double allowed = (this.x - other.x) - width - minGap;
                    if (allowed < Math.abs(maxDx)) maxDx = -Math.max(0, allowed);
                }
            } else if (dir == Direction.SOUTH) {
                if (other.y > this.y && other.y < this.y + height + safetyZone && Math.abs(other.x - this.x) < lateralTolerance) {
                    double allowed = (other.y - this.y) - height - minGap;
                    if (allowed < maxDy) maxDy = Math.max(0, allowed);
                }
            } else if (dir == Direction.NORTH) {
                if (other.y < this.y && other.y > this.y - height - safetyZone && Math.abs(other.x - this.x) < lateralTolerance) {
                    double allowed = (this.y - other.y) - height - minGap;
                    if (allowed < Math.abs(maxDy)) maxDy = -Math.max(0, allowed);
                }
            }
        }

        if (!ignoresLights) {
            for (TrafficLight light : world.getLights()) {
                double lightCenterX = light.getX() + lightRadius;
                double lightCenterY = light.getY() + lightRadius;
                if (dir.isHorizontal() && Math.abs(lightCenterY - (this.y + height / 2.0)) < 15.0) {
                    double dist = lightCenterX - (this.x + (dir == Direction.EAST ? this.width : 0));
                    if ((dir == Direction.EAST && dist >= -1.0 && dist < 30) || (dir == Direction.WEST && -dist >= -1.0 && -dist < 30)) {
                        if (light.getCurrentColor() == TrafficLight.Color.RED || light.getCurrentColor() == TrafficLight.Color.YELLOW) {
                            maxDx = 0;
                            maxDy = 0;
                        }
                    }
                }
                if (dir.isVertical() && Math.abs(lightCenterX - (this.x + width / 2.0)) < 15.0) {
                    double dist = lightCenterY - (this.y + (dir == Direction.SOUTH ? this.height : 0));
                    if ((dir == Direction.SOUTH && dist >= -1.0 && dist < 30) || (dir == Direction.NORTH && -dist >= -1.0 && -dist < 30)) {
                        if (light.getCurrentColor() == TrafficLight.Color.RED || light.getCurrentColor() == TrafficLight.Color.YELLOW) {
                            maxDx = 0;
                            maxDy = 0;
                        }
                    }
                }
            }
        }

        this.x += maxDx;
        this.y += maxDy;

        int col = (int) ((x + width / 2.0) / TILE);
        int row = (int) ((y + height / 2.0) / TILE);

        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length) {
            List<Direction> options = availableDirectionsFromTile(col, row, map);
            List<Direction> forwardOptions = new ArrayList<>(options);
            forwardOptions.remove(dir.opposite());

            if (!forwardOptions.isEmpty()) {
                if (forwardOptions.size() > 1) {
                    if (col != lastCrossingX || row != lastCrossingY) {
                        this.dir = forwardOptions.get(rnd.nextInt(forwardOptions.size()));
                        alignToTileLane(col, row, this.dir);
                        lastCrossingX = col;
                        lastCrossingY = row;
                    }
                } else {
                    if (!forwardOptions.contains(dir)) {
                        this.dir = forwardOptions.getFirst();
                        alignToTileLane(col, row, this.dir);
                    }
                    lastCrossingX = -1;
                    lastCrossingY = -1;
                }
            } else if (options.contains(dir.opposite())) {
                this.dir = dir.opposite();
                alignToTileLane(col, row, this.dir);
                lastCrossingX = -1;
                lastCrossingY = -1;
            }
        }

        vX += (x - vX) * Math.min(1.0, 12.0 * deltaTime);
        vY += (y - vY) * Math.min(1.0, 12.0 * deltaTime);

        double targetA = getTargetAngle(dir);
        double diff = targetA - vAngle;
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;
        vAngle += diff * Math.min(1.0, 8.0 * deltaTime);

        double maxX = map[0].length * TILE - this.width;
        double maxY = map.length * TILE - this.height;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > maxX) x = maxX;
        if (y > maxY) y = maxY;
    }

    private List<Direction> availableDirectionsFromTile(int gx, int gy, char[][] map) {
        List<Direction> res = new ArrayList<>();
        int w = map[0].length, h = map.length;
        if (gx - 1 >= 0 && isRoadTile(map[gy][gx - 1])) res.add(Direction.WEST);
        if (gx + 1 < w && isRoadTile(map[gy][gx + 1])) res.add(Direction.EAST);
        if (gy - 1 >= 0 && isRoadTile(map[gy - 1][gx])) res.add(Direction.NORTH);
        if (gy + 1 < h && isRoadTile(map[gy + 1][gx])) res.add(Direction.SOUTH);
        return res;
    }

    private void alignToTileLane(int gx, int gy, Direction newDir) {
        double centerX = gx * TILE + TILE / 2.0;
        double centerY = gy * TILE + TILE / 2.0;
        double offset = 12.0;
        switch (newDir) {
            case EAST -> this.y = centerY - height / 2.0 + offset;
            case WEST -> this.y = centerY - height / 2.0 - offset;
            case SOUTH -> this.x = centerX - width / 2.0 - offset;
            case NORTH -> this.x = centerX - width / 2.0 + offset;
        }
    }

    public double getVX() {
        return vX;
    }

    public double getVY() {
        return vY;
    }

    public double getVAngle() {
        return vAngle;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getColorHex() {
        return colorHex;
    }

    public boolean isIgnoresLights() {
        return ignoresLights;
    }
}
