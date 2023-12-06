package com.gomoku.project04gomoku;

import com.gomoku.project04gomoku.mvc.ViewModel.MusicPlayer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import com.gomoku.project04gomoku.app.models.Net;
import com.gomoku.project04gomoku.mvc.ViewModel.LocalWLANMultiplayerController;
/**
 * The main class responsible for launching the Gomoku application.
 */
public class GomokuStart extends Application implements EventHandler<WindowEvent> {
    /**
     * The path to the configuration file.
     */
    private static final String CONFIG_FILE_PATH = "settings.properties";
    /**
     * The main entry point for the Gomoku application.
     *
     * @param stage The primary stage for the application.
     * @throws IOException        If an error occurs while loading the FXML file.
     * @throws URISyntaxException If an error occurs in URI syntax.
     */
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Image icon = new Image(Objects.requireNonNull(GomokuStart.class.getResourceAsStream("pic/icon.jpg")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("Gomoku");
        stage.setScene(scene);
        stage.show();
        MusicPlayer.initializeMusicPlayer();

    }
    /**
     * Handles the window close event to ensure proper closure of network connections.
     *
     * @param event The window close event.
     */
    @Override
    public void handle(WindowEvent event) {
        try {
            Net.getInstance(LocalWLANMultiplayerController.netType).close();
        } catch (Exception e) {}
        System.exit(0);
    }
    /**
     * The main method to launch the Gomoku application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
