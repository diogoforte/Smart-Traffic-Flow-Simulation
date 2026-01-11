package pt.ual.smarttrafficflow.util;

import pt.ual.smarttrafficflow.model.Direction;
import pt.ual.smarttrafficflow.model.TrafficLight;
import pt.ual.smarttrafficflow.model.Vehicle;
import pt.ual.smarttrafficflow.model.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Utilitário para carregar a configuração do mapa e entidades de um ficheiro
public class MapLoader {
    public static char[][] loadMap(String path, World world) throws MapInvalidException {
        List<char[]> grid = new ArrayList<>();
        List<String> entities = new ArrayList<>();

        // Tenta abrir o ficheiro de recurso
        try (InputStream is = MapLoader.class.getResourceAsStream("/pt/ual/smarttrafficflow/" + path)) {
            if (is == null) throw new MapInvalidException("Ficheiro não encontrado: " + path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    // Ignora linhas vazias ou comentários
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    // Separa entidades da definição da grelha
                    if (line.startsWith("VEHICLE") || line.startsWith("LIGHT")) entities.add(line);
                    else grid.add(line.toCharArray());
                }
            }
        } catch (IOException e) {
            throw new MapInvalidException("Erro ao ler o ficheiro: " + e.getMessage());
        }

        if (grid.isEmpty()) throw new MapInvalidException("O mapa está vazio!");
        char[][] map = grid.toArray(new char[0][]);
        Random rnd = new Random();

        // Processa cada entidade encontrada no ficheiro
        for (String eLine : entities) {
            String[] parts = eLine.split("\\s+");
            if (parts.length < 4) continue;
            String type = parts[0], id = parts[1];
            int gx = Integer.parseInt(parts[2]), gy = Integer.parseInt(parts[3]);

            // Valida se a entidade está dentro dos limites do mapa
            if (gy < 0 || gy >= map.length || gx < 0 || gx >= map[0].length)
                throw new MapInvalidException("ERRO: " + id + " fora dos limites.");

            if ("VEHICLE".equals(type)) {
                if (parts.length < 7)
                    throw new MapInvalidException("ERRO: VEHICLE deve ter ID, X, Y, SPEED, COLOR e SPECIAL.");
                double speed = Double.parseDouble(parts[4]);
                String color = parts[5];
                boolean special = Boolean.parseBoolean(parts[6]);

                // Determina direções possíveis com base na estrada
                List<Direction> possible = new ArrayList<>();
                if (gx - 1 >= 0 && map[gy][gx - 1] == '1') possible.add(Direction.WEST);
                if (gx + 1 < map[0].length && map[gy][gx + 1] == '1') possible.add(Direction.EAST);
                if (gy - 1 >= 0 && map[gy - 1][gx] == '1') possible.add(Direction.NORTH);
                if (gy + 1 < map.length && map[gy + 1][gx] == '1') possible.add(Direction.SOUTH);

                Direction dir = possible.isEmpty() ? Direction.EAST : possible.get(rnd.nextInt(possible.size()));
                // Adiciona o veículo ao mundo com coordenadas convertidas para pixéis
                world.addVehicle(new Vehicle(gx * Config.TILE_SIZE, gy * Config.TILE_SIZE, speed, dir, color, special));
            } else if ("LIGHT".equals(type)) {
                // Adiciona um semáforo numa posição central da célula
                world.addLight(new TrafficLight(gx * Config.TILE_SIZE + 19, gy * Config.TILE_SIZE + 19));
            }
        }
        return map;
    }
}