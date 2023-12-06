package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for handling difficulty selection in the Gomoku game.
 * This class is associated with the "DifficultSelect.fxml" file.
 * It provides methods to handle user actions, such as changing the difficulty level
 * and navigating between different scenes (e.g., game board and main menu).
 */
public class DifficultSelectController {
    /**
     * ComboBox for selecting the difficulty level.
     */
    @FXML
    private ComboBox<String> difficultyComboBox;

    /**
     * FXMLLoader for loading FXML files.
     */
    FXMLLoader fxmlLoader;
    // ...

    /**
     * Handles the event when the user changes the difficulty level.
     * Loads the ChessBoard scene with the selected difficulty and switches to it.
     *
     * @param event The ActionEvent triggered by the difficulty selection.
     */
    @FXML
    private void handleDifficultyChange(ActionEvent event) {
        String selectedDifficulty = difficultyComboBox.getValue();

        // System.out.println(selectedDifficulty); //DEBUG
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/ChessBoard.fxml"));
            fxmlLoader.setController(new PVEController(selectedDifficulty));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Node source =(Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load PvE view");
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the main menu when the corresponding button is clicked.
     *
     * @param event The ActionEvent triggered by the "Go Back to Main" button.
     * @throws IOException If there is an issue loading the Menu.fxml file.
     */
    public void GoBackToMain(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);

            Node source =(Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
