module Calculadora {
    requires javafx.fxml;
    requires transitive javafx.controls;
    exports lnandobr.application to javafx.graphics;
    exports lnandobr.controller to javafx.fxml;
    opens lnandobr.controller to javafx.fxml;
}