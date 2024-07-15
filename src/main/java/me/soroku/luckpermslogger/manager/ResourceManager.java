package me.soroku.luckpermslogger.manager;


import me.soroku.luckpermslogger.config.FileConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ResourceManager {
    private final File dataDirectory;

    public ResourceManager(File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public FileConfiguration loadConfig(String fileName) {
        File file = new File(dataDirectory, fileName);
        try {
            if (!file.exists()) {
                saveDefaultConfig(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileConfiguration(file);
    }

    private void saveDefaultConfig(String fileName) throws IOException {
        File file = new File(dataDirectory, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new FileNotFoundException("Default " + fileName + " not found in resources");
            }
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
