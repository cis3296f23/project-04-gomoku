package com.gomoku.project04gomoku.mvc.ViewModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceScanner {
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
