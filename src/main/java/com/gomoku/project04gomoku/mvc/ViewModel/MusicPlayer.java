package com.gomoku.project04gomoku.mvc.ViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static final String CONFIG_FILE_PATH = "settings.properties";
    public static void initializeMusicPlayer() throws URISyntaxException {

        Properties props = loadSettings();
        URL musicResource = MusicPlayer.class.getResource("/bgm/default_music.mp3");
        assert musicResource != null;
        File musicFile = new File(musicResource.getFile());
        String musicFilePath = musicFile.getAbsolutePath();
        saveVolumeSetting(0.5,musicFilePath);
        String musicFileName = props.getProperty("bgm", musicFilePath); // default bgm
        double volume = Double.parseDouble(props.getProperty("volume", "0.5")); // default volume



        playMusic(musicFileName, volume);
    }
    private static void saveVolumeSetting(double volume, String selectedBGM) {

        Properties props = new Properties();
        props.setProperty("volume", String.valueOf(volume));
        props.setProperty("bgm", selectedBGM);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static Properties loadSettings() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }
    public static void playMusic(String musicFile, double volume) throws URISyntaxException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        System.out.println(musicFile);
        String musicFileName = musicFile.replace("\\", "/").replace("%20", " ");
        System.out.println(musicFileName);
        Media sound = new Media(new File(musicFileName).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
    public static void setVolume(double volume)
    {
        mediaPlayer.setVolume(volume);
    }
}
