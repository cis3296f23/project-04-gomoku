package com.gomoku.project04gomoku.mvc.ViewModel;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class MenuViewModel {

    public void displayInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

}
