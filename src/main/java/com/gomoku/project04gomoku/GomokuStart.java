package com.gomoku.project04gomoku;

import com.gomoku.project04gomoku.mvc.ViewModel.MusicPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class GomokuStart extends Application {

    private static final String CONFIG_FILE_PATH = "settings.properties";


    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Image icon = new Image(Objects.requireNonNull(GomokuStart.class.getResourceAsStream("pic/icon.jpg")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("Gomuku");
        stage.setScene(scene);
        stage.show();
        MusicPlayer.initializeMusicPlayer();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
