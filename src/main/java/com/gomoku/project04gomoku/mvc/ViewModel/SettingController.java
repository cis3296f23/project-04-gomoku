package com.gomoku.project04gomoku.mvc.ViewModel;
import com.gomoku.project04gomoku.GomokuStart;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SettingController {

    @FXML
    private Slider bgmVolumeSlider;

    FXMLLoader fxmlLoader;
    @FXML
    private ComboBox<String> bgmSelectionBox;

    private static final String CONFIG_FILE_PATH = "settings.properties";



    public void initialize() {

        bgmVolumeSlider.setValue(loadVolumeSetting());
        bgmSelectionBox.setValue(loadBgmSetting());

        bgmVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100;
            MusicPlayer.setVolume(volume);
        });
        List<String> bgmFiles = loadBgmFiles();
        bgmSelectionBox.setItems(FXCollections.observableArrayList(bgmFiles));
        bgmSelectionBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeBGM(newValue);
        });
    }

    private List<String> loadBgmFiles() {
        List<String> bgmFiles = new ArrayList<>();
        try {
            InputStream in = getClass().getResourceAsStream("/bgm");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".mp3")) {
                    bgmFiles.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bgmFiles;
    }


    private Properties loadSettings() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }

    private double loadVolumeSetting() {
        Properties props = loadSettings();
        String volume = props.getProperty("volume", "50.0");
        return Double.parseDouble(volume);
    }
    private String loadBgmSetting() {
        Properties props = loadSettings();
        String bgm = props.getProperty("bgm", "bgm1");
        return bgm;
    }


    private void changeBGM(String bgmName) {


        System.out.println("BGM changed to: " + bgmName);
    }




    @FXML
    private void applySettings(ActionEvent event) {


        double volume = bgmVolumeSlider.getValue() /100;
        String selectedBGM = bgmSelectionBox.getSelectionModel().getSelectedItem();

        saveVolumeSetting(volume,selectedBGM);
        changeBGM(selectedBGM);
        MusicPlayer.setVolume(volume);

        System.out.println("Settings applied: Volume is " + volume + ", BGM is " + selectedBGM);
    }


    private void saveVolumeSetting(double volume, String selectedBGM) {

        Properties props = new Properties();
        props.setProperty("volume", String.valueOf(volume));
        props.setProperty("bgm", selectedBGM);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
