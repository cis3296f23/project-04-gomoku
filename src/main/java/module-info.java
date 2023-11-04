module com.gomoku.project04gomoku {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.gomoku.project04gomoku to javafx.fxml;
    exports com.gomoku.project04gomoku;
    exports com.gomoku.project04gomoku.controller;
    opens com.gomoku.project04gomoku.controller to javafx.fxml;
}