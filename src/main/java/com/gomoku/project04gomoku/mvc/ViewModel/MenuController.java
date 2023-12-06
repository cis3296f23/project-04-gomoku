package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.mvc.test.MenuViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Controller class for handling events in the main menu.
 */
public class MenuController {
    /**
     * FXMLLoader for loading FXML files.
     */
    FXMLLoader fxmlLoader;
    /**
     * View model for the main menu.
     */
    private final MenuViewModel viewModel = new MenuViewModel();
    /**
     * Label displaying welcome text.
     */
    @FXML
    private Label welcomeText;
    /**
     * Handles the action when the "Hello" button is clicked.
     */
    @FXML
    protected void onHelloButtonClick() {
        viewModel.displayInformationDialog("This is a dialog.");
    }
    /**
     * Button for starting the game.
     */
    @FXML
    private Button GameStart;
    /**
     * Button for accessing the settings.
     */
    @FXML
    private Button Setting;

    /**
     * Switches to the mode selection menu.
     *
     * @param event The event triggered by the button click.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @FXML
    protected void switchToModeSelect(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/SelectMode.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) GameStart.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens the settings window.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    protected void GoToSetting(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Setting.fxml"));
            fxmlLoader.setController(new SettingController());


            Parent root = fxmlLoader.load();
            Stage SettingWindow = new Stage();
            SettingWindow.initModality(Modality.WINDOW_MODAL);
            Node source =(Node) event.getSource();
            Stage Parentstage = (Stage) source.getScene().getWindow();
            SettingWindow.setTitle("Setting");
            SettingWindow.initOwner(Parentstage);
            SettingWindow.setScene(new Scene(root));
            SettingWindow.setResizable(false);
            // Add logic when close
//            SettingWindow.setOnCloseRequest(Event::consume);
            SettingWindow.setOnCloseRequest(e -> {
                // create a dialog windows
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setHeaderText("Warning");
                alert.setContentText("Are you sure to exit?");
                alert.getDialogPane().getStylesheets().add( Objects.requireNonNull(GomokuStart.class.getResource("css/alert.css")).toExternalForm());
                // wait for response
                alert.showAndWait().ifPresent(response -> {

                    if (response != ButtonType.OK) {

                        e.consume();
                    }

                });
                Properties settingfile = SettingController.loadSettings();
                ;
                MusicPlayer.setVolume(Double.parseDouble(settingfile.getProperty("volume", "0.5")));

            });

            SettingWindow.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the action when the exit button is clicked.
     *
     * @param event The event triggered by the button click.
     */
    @FXML
    public void handleExitButtonAction(ActionEvent event) {
        Platform.exit();
    }


}
