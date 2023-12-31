package com.gomoku.project04gomoku.mvc.ViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Controller class for managing the music player in the application.
 */
public class MusicPlayer {
    /**
     * FXMLLoader for loading FXML files.
     */
    private static MediaPlayer mediaPlayer;
    /**
     * The file path for the configuration file.
     */
    private static final String CONFIG_FILE_PATH = "settings.properties";
    /**
     * Initializes the music player with default settings or settings from the configuration file.
     *
     * @throws URISyntaxException If there is an issue with the URI syntax.
     */
    public static void initializeMusicPlayer() throws URISyntaxException {
        File Config = new File(CONFIG_FILE_PATH);
        if(!Config.exists())
        {
            InitSetting(0.5, "bgm/default_music.mp3");
        }

        List<String> bgmList = loadBgmFiles();

        if(!bgmList.isEmpty())
        {
            String bgmpath = "bgm/"+bgmList.get(0);

            Properties props = loadSettings();
            props.setProperty("bgm",bgmpath);

            String musicFileName = props.getProperty("bgm"); // default bgm
            double volume = Double.parseDouble(props.getProperty("volume", "0.5")); // default volume


            loadBgmFiles();
            playMusic(musicFileName, volume);
        }

    }
    /**
     * Initializes the settings and saves them to the configuration file.
     *
     * @param volume        The default volume.
     * @param selectedBGM   The default selected background music.
     */
    private static void InitSetting(double volume, String selectedBGM) {

        Properties props = new Properties();


        props.setProperty("volume", String.valueOf(volume));
        props.setProperty("bgm", selectedBGM);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the MediaPlayer instance.
     *
     * @return The MediaPlayer instance.
     */
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Loads the list of available background music files.
     *
     * @return The list of available background music files.
     */
    public  static List<String> loadBgmFiles() {
        List<String> bgmFiles = new ArrayList<>();
        try {
            String bgmDirectoryPath="bgm";
            File bgmFile = new File(bgmDirectoryPath);
            if (!bgmFile.exists()) {
                // create while bgm folder
                boolean result = !bgmFile.mkdir();
                if (result) {
                    System.out.println("Folder created：" + bgmFile);
                } else {
                    System.out.println("Folder creation failed。");
                }
            } else {
                System.out.println("Folder already exist：" + bgmFile);
            }
            FilenameFilter mp3Filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".mp3");
                }
            };

            File[] mp3Files = bgmFile.listFiles(mp3Filter);
            if (mp3Files != null) {
                for (File file : mp3Files) {
                    // print file name
                    System.out.println("Found Mp3 file: " + file.getName());
                    bgmFiles.add(file.getName());
                }
            } else {
                System.out.println("Directory does not exist");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bgmFiles;
    }

    /**
     * Loads the settings from the configuration file.
     *
     * @return The properties object containing the settings.
     */
    private static Properties loadSettings() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return props;
    }
    /**
     * Plays the selected music file with the specified volume.
     *
     * @param musicFile The music file to play.
     * @param volume    The volume at which to play the music.
     * @throws URISyntaxException If there is an issue with the URI syntax.
     */
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
    /**
     * Sets the volume of the currently playing music.
     *
     * @param volume The volume level.
     */
    public static void setVolume(double volume)
    {
        mediaPlayer.setVolume(volume);
    }
}
