package com.gomoku.project04gomoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GomokuStart extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        stage.setResizable(false);
        stage.setTitle("Gomuku");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
