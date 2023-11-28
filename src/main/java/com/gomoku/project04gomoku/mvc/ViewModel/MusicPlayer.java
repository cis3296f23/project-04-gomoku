package com.gomoku.project04gomoku.mvc.ViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        System.out.println(musicResource.toString());

        String musicFile = props.getProperty("bgm", String.valueOf(musicResource.toURI())); // default bgm
        double volume = Double.parseDouble(props.getProperty("volume", "0.5")); // default volume


        playMusic(musicFile, volume);
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
    public static void playMusic(String musicFile, double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media sound = new Media(new File(musicFile).toURI().toString());
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
