package com.gomoku.project04gomoku.mvc.ViewModel;

/**
 * 处理主菜单事件
 */

import com.gomoku.project04gomoku.GomokuStart;
import com.gomoku.project04gomoku.mvc.test.MenuViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    public Button TestChange;
    FXMLLoader fxmlLoader;

    private final MenuViewModel viewModel = new MenuViewModel();
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        viewModel.displayInformationDialog("This is a dialog.");
    }

    @FXML
    private Button GameStart;

    @FXML
    private Button Setting;
    @FXML

    /**
     * 用来进入二级菜单
     * @param event
     * @throws IOException
     */
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
    @FXML
    protected void GoToSetting(ActionEvent event) {
        try {
            fxmlLoader = new FXMLLoader(GomokuStart.class.getResource("view/Setting.fxml"));
            fxmlLoader.setController(new SettingController());
            Scene root = new Scene(fxmlLoader.load(), 800, 600);


            Stage stage = (Stage) Setting.getScene().getWindow();


            stage.setScene(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public  void handleButtonTest()
    {
        welcomeText.setText("你好");
    }
}
