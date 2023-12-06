package com.gomoku.project04gomoku.mvc.ViewModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Utility class for scanning resources, specifically music files.
 */
public class ResourceScanner {
    /**
     * Scans the specified resource folder for music files with the ".mp3" extension.
     *
     * @param resourceFolder The folder containing the music files.
     * @return A list of music file names.
     */
    public static List<String> scanForMusicFiles(String resourceFolder) {
        List<String> musicFiles = new ArrayList<>();
        try {
            musicFiles = Files.walk(Paths.get(ClassLoader.getSystemResource(resourceFolder).toURI()))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(".mp3")) //
                    .collect(Collectors.toList());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return musicFiles;
    }
}
