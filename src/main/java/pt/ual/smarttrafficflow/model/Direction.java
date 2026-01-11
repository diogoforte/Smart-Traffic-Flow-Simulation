package pt.ual.smarttrafficflow.model;

// Enumeração que define as direções de movimento e as suas coordenadas
public enum Direction {
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // Retorna a direção contrária à atual
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            default -> EAST;
        };
    }

    // Verifica se o movimento é no eixo X
    public boolean isHorizontal() {
        return this == EAST || this == WEST;
    }

    // Verifica se o movimento é no eixo Y
    public boolean isVertical() {
        return this == NORTH || this == SOUTH;
    }
}