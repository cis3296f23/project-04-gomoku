package com.gomoku.project04gomoku.mvc.ViewModel;
import com.gomoku.project04gomoku.GomokuStart;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class SettingController {

    @FXML
    private Slider bgmVolumeSlider;

    @FXML
    private Slider bgmProgressSlider;

    FXMLLoader fxmlLoader;
    @FXML
    private ComboBox<String> bgmSelectionBox;

    private static final String CONFIG_FILE_PATH = "settings.properties";
    private static final Map<String,String> BgmPATHs = new HashMap<>();

    private double originalVolume;
    private String originalBGM;

    public void initialize() {

        originalVolume = loadVolumeSetting();
        originalBGM = loadBgmSetting();

        bgmVolumeSlider.setValue(originalVolume * 100);
        bgmSelectionBox.setItems(FXCollections.observableArrayList(MusicPlayer.loadBgmFiles()));
        bgmSelectionBox.setValue(loadBgmSetting());
        bgmVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            double volume = newValue.doubleValue() / 100;
            MusicPlayer.setVolume(volume);
        });

        bgmSelectionBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                changeBGM(newValue);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });


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

        List<String> bgmFileNames = MusicPlayer.loadBgmFiles();
        for(String filename: bgmFileNames)
        {
            String BgmPath = "bgm/"+filename;
            BgmPATHs.put(filename,BgmPath);
        }
        Properties props = loadSettings();
        String bgm = props.getProperty("bgm", "bgm1");
        for(String key: BgmPATHs.keySet())
        {
            if(BgmPATHs.get(key).equals(bgm))
            {
                return key;
            }
        }
        return "BGM NOT Found!";
    }


    private void changeBGM(String bgmName) throws URISyntaxException {
        String BgmPath = "bgm/"+bgmName;
        BgmPATHs.put(bgmName,BgmPath);
        MusicPlayer.playMusic(BgmPath,loadVolumeSetting());
        System.out.println("BGM changed to: " + bgmName);
    }




    @FXML
    private void applySettings(ActionEvent event) throws URISyntaxException {


        // Restore
        double volume = bgmVolumeSlider.getValue() / 100;
        String selectedBGM = bgmSelectionBox.getSelectionModel().getSelectedItem();

        // Save the new value
        originalVolume = volume;
        if(!bgmSelectionBox.getSelectionModel().getSelectedItem().equals(originalBGM))
        {
            originalBGM = selectedBGM;
            changeBGM(selectedBGM);

        }

        saveSetting(volume, selectedBGM);

        MusicPlayer.setVolume(volume);

        System.out.println("Settings applied: Volume is " + volume + ", BGM is " + selectedBGM);
    }


    private void saveSetting(double volume, String selectedBGM) {

        Properties props = new Properties();
        props.setProperty("volume", String.valueOf(volume));
        props.setProperty("bgm", BgmPATHs.get(selectedBGM));

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

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Check if value was changed
        if (bgmVolumeSlider.getValue() / 100 != originalVolume )
        {
            MusicPlayer.setVolume(originalVolume);
        }
        if(!bgmSelectionBox.getSelectionModel().getSelectedItem().equals(originalBGM)) {
            // Restore to original setting

            try {
                changeBGM(originalBGM);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            System.out.println("Original settings restored");

        }
        stage.close();
    }
}
