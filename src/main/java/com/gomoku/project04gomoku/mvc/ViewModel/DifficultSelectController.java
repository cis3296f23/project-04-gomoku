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

public class DifficultSelectController {
    @FXML
    private ComboBox<String> difficultyComboBox;

    FXMLLoader fxmlLoader;
    // ...

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
