module pt.ual.smarttrafficflow {
    requires javafx.controls;
    requires java.logging;


    opens pt.ual.smarttrafficflow to javafx.fxml;
    exports pt.ual.smarttrafficflow;
    exports pt.ual.smarttrafficflow.view;
    opens pt.ual.smarttrafficflow.view to javafx.fxml;
    exports pt.ual.smarttrafficflow.controller;
    opens pt.ual.smarttrafficflow.controller to javafx.fxml;
}