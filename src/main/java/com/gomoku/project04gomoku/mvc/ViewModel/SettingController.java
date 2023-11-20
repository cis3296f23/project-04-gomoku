package com.gomoku.project04gomoku.mvc.ViewModel;
import com.gomoku.project04gomoku.GomokuStart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {

    @FXML
    private Slider bgmVolumeSlider;

    FXMLLoader fxmlLoader;
    @FXML
    private ComboBox<String> bgmSelectionBox;


    public void initialize() {

        bgmVolumeSlider.setValue(loadVolumeSetting());


        bgmSelectionBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeBGM(newValue);
        });
    }


    private double loadVolumeSetting() {
        // TODO:
        return 50.0;
    }


    private void changeBGM(String bgmName) {
        // TODO:
        System.out.println("BGM changed to: " + bgmName);
    }


    @FXML
    private void applySettings(ActionEvent event) {

        double volume = bgmVolumeSlider.getValue();
        String selectedBGM = bgmSelectionBox.getSelectionModel().getSelectedItem();

        saveVolumeSetting(volume);
        changeBGM(selectedBGM);

        System.out.println("Settings applied: Volume is " + volume + ", BGM is " + selectedBGM);
    }


    private void saveVolumeSetting(double volume) {

        System.out.println("Volume saved: " + volume);
    }


    @FXML
    private void cancelSettings(ActionEvent event) {

        bgmVolumeSlider.setValue(loadVolumeSetting());
        System.out.println("Settings cancelled");
    }
    @FXML
    protected void GoBackMain(ActionEvent event) {
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
