package com.gomoku.project04gomoku.mvc.test;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
/**
 * The MenuViewModel class provides methods for displaying information dialogs.
 */
public class MenuViewModel {
    /**
     * Displays an information dialog with the given message.
     *
     * @param message The message to be displayed in the information dialog.
     */
    public void displayInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }


}
