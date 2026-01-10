// java
        package pt.ual.smarttrafficflow.model.state;

        import pt.ual.smarttrafficflow.model.TrafficLight;

        public interface TrafficLightState {
            void update(TrafficLight light, double deltaTime);
            TrafficLight.Color getColor();
        }