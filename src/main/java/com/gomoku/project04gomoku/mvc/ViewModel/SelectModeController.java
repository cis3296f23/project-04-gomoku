/**
 * 专门用来控制模式选择的controller。有返回，切换页面等功能
 */

package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class specifically designed to control mode selection. It includes functionalities such as navigation and switching between pages.
 */
public class SelectModeController {
    /**
     * Button for local WLAN multiplayer.
     */
    public Button LocalWlan;
    /**
     * FXMLLoader for loading FXML files.
     */
    FXMLLoader fxmlLoader;
    /**
     * Controller for local multiplayer.
     */
    LocalMultiplayerController localplay = new LocalMultiplayerController();
    /**
     * Button to go back to the main menu.
     */
    @FXML
    private Button GoBackMain;
    /**
     * Button to navigate to multiple-player options.
     */
    @FXML
    private Button Multiple;
    /**
     * Button to navigate to local multiplayer.
     */
    @FXML
    private Button Local;
    /**
     * Button to create a lobby.
     */
   @FXML
   private Button CreateButton;
    /**
     * Returns to the mode selection menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     * @throws IOException If there is an issue loading the FXML file.
     */
    public void GoBackToSelectMode(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/SelectMode.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Node source =(Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns to the main menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     * @throws IOException If there is an issue loading the FXML file.
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

    /**
     * Navigates to the multiple-player options menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     * @throws IOException If there is an issue loading the FXML file.
     */
    public void GoToMultiplePlayer(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/MultiplePlayer.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) Multiple.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Navigates to the local multiplayer board.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    private void GoToLocalMultiplayer(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/ChessBoard.fxml"));
            fxmlLoader.setController(new LocalMultiplayerController());
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Stage stage = (Stage) Local.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();

        }

    }
    /**
     * Navigates to the local WLAN multiplayer lobby.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    private void GoToLocalWLANMultiplayer(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/LocalHost.fxml"));
            fxmlLoader.setController(new LocalWLANMultiplayerController());
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Node source =(Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }

    }
    /**
     * Navigates to the lobby join menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    private void GoToJoin(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/JoinLobby.fxml"));
            fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Stage stage = (Stage) CreateButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }

    }
    /**
     * Navigates to the lobby creation menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    private void GoToCreate(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/LobbyCreate.fxml"));
            fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Stage stage = (Stage) CreateButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }

    }
    /**
     * Navigates to the local WLAN multiplayer board.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    private void CreateHost(ActionEvent event)
    {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/ChessBoard.fxml"));
            fxmlLoader.setController(new LocalWLANMultiplayerController());
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Node source =(Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }
    }
    /**
     * Navigates to the single-player difficulty selection menu.
     *
     * @param event ActionEvent triggered when the button is clicked.
     */
    @FXML
    public void GoToSinglePlayer(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/PVEDifficultSelect.fxml"));
            fxmlLoader.setController(new DifficultSelectController());
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

}
