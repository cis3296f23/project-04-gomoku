package com.gomoku.project04gomoku.mvc.ViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MusicPlayer {
    private static MediaPlayer mediaPlayer;
    private static final String CONFIG_FILE_PATH = "settings.properties";
    public static void initializeMusicPlayer() throws URISyntaxException {

        Properties props = loadSettings();
        //URL musicResource = MusicPlayer.class.getResource("/bgm/default_music.mp3");
        //assert musicResource != null;
        //File musicFile = new File(musicResource.getFile());
        //String musicFilePath = musicFile.getAbsolutePath();
        InitSetting(0.5, "bgm/default_music.mp3");
        List<String> bgmList = loadBgmFiles();
        if(!bgmList.isEmpty())
        {
            String musicFileName = props.getProperty("bgm"); // default bgm
            double volume = Double.parseDouble(props.getProperty("volume", "0.5")); // default volume


            loadBgmFiles();
            playMusic(musicFileName, volume);
        }

    }
    private static void InitSetting(double volume, String selectedBGM) {

        Properties props = new Properties();
        String bgmfolder="bgm";
        File bgmdirectory= new File(bgmfolder);
        if (!bgmdirectory.exists()) {
            // create while bgm folder
            boolean result = !bgmdirectory.mkdir();
            if (result) {
                System.out.println("Folder created：" + bgmdirectory);
            } else {
                System.out.println("Folder creation failed。");
            }
        } else {
            System.out.println("Folder already exist：" + bgmdirectory);
        }

        props.setProperty("volume", String.valueOf(volume));
        props.setProperty("bgm", selectedBGM);

        try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            props.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public   static List<String> loadBgmFiles() {
        List<String> bgmFiles = new ArrayList<>();
        try {
            String bgmDirectoryPath="bgm";
            File bgmFile = new File(bgmDirectoryPath);
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
        String musicFileName = musicFile.replace("\\", "/");
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
