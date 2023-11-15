/**
 * 专门用来控制模式选择的controller。有返回，切换页面等功能
 */

package com.gomoku.project04gomoku.mvc.ViewModel;

import com.gomoku.project04gomoku.GomokuStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class SelectModeController {
    public Button LocalWlan;
    FXMLLoader fxmlLoader;

    LocalMultiplayerController localplay = new LocalMultiplayerController();
    @FXML
    private Button GoBackMain;
    @FXML
    private Button Multiple;
    @FXML
    private Button MultipleBack;
    @FXML
    private Button Local;

    /**
     * 用来返回到二级菜单
     *
     * @param event
     * @throws IOException
     */
    public void GoBackToSelectMode(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/SelectMode.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) MultipleBack.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来返回到主菜单
     *
     * @param event
     * @throws IOException
     */
    public void GoBackToMain(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/menu.fxml"));
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) GoBackMain.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来进入三级菜单（多人游戏选项）
     *
     * @param event
     * @throws IOException
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
    @FXML
    public void GoToPVE(ActionEvent event) throws IOException {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/MultiplePlayer.fxml"));
            fxmlLoader.setController(new PVEController());
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) Multiple.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLocalMultiplayer(ActionEvent event) {
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
    @FXML
    private void goToLocalWLANMultiplayer(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/CreateOrJoin.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Stage stage = (Stage) Local.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }

    }
    @FXML
    private void goToCreate(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/WLANSetting.fxml"));
            fxmlLoader.setController(new LocalWLANMultiplayerController());
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            Stage stage = (Stage) Local.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load FXML for local multiplayer");
            e.printStackTrace();
        }

    }


}
