package pt.ual.smarttrafficflow.util;

// Exceção personalizada para erros na estrutura ou leitura do mapa
public class MapInvalidException extends Exception {
    public MapInvalidException(String message) {
        super(message);
    }
}