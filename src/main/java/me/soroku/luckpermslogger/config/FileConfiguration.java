package me.soroku.luckpermslogger.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConfiguration {
    private final File file;
    private Map<String, Object> config;

    // Main
    public FileConfiguration(File file) {
        this.file = file;
        loadConfig();
    }

    @SuppressWarnings("unchecked")
    private void loadConfig() {
        try {
            if (!file.exists()) {
                config = new HashMap<>();
            } else {
                Yaml yaml = new Yaml(new Constructor(Map.class));
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    config = yaml.load(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (FileWriter writer = new FileWriter(file)) {
                yaml.dump(config, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object getValue(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = config;
        for (int i = 0; i < keys.length - 1; i++) {
            Object value = currentMap.get(keys[i]);
            if (!(value instanceof Map)) {
                return null;
            }
            currentMap = (Map<String, Object>) value;
        }
        return currentMap.get(keys[keys.length - 1]);
    }

    private void setValue(String path, Object value) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = config;
        for (int i = 0; i < keys.length - 1; i++) {
            Object obj = currentMap.get(keys[i]);
            if (!(obj instanceof Map)) {
                obj = new HashMap<String, Object>();
                currentMap.put(keys[i], obj);
            }
            currentMap = (Map<String, Object>) obj;
        }
        currentMap.put(keys[keys.length - 1], value);
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    // Setters
    public void setString(String path, String value) {
        setValue(path, value);
        saveConfig();
    }

    public void setInt(String path, int value) {
        setValue(path, value);
        saveConfig();
    }

    public void setLong(String path, long value) {
        setValue(path, value);
        saveConfig();
    }

    public void setBoolean(String path, boolean value) {
        setValue(path, value);
        saveConfig();
    }

    public void setDouble(String path, double value) {
        setValue(path, value);
        saveConfig();
    }

    public void setList(String path, List<?> value) {
        setValue(path, value);
        saveConfig();
    }

    public void set(String path, Object value) {
        setValue(path, value);
        saveConfig();
    }

    // Getters
    public String getString(String path) {
        Object value = getValue(path);
        return value != null ? value.toString() : null;
    }

    public int getInt(String path) {
        Object value = getValue(path);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(getString(path));
        } catch (NumberFormatException e) {
            return 0; // or throw an exception
        }
    }

    public long getLong(String path) {
        Object value = getValue(path);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(getString(path));
        } catch (NumberFormatException e) {
            return 0L; // or throw an exception
        }
    }

    public boolean getBoolean(String path) {
        Object value = getValue(path);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(getString(path));
    }

    public double getDouble(String path) {
        Object value = getValue(path);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(getString(path));
        } catch (NumberFormatException e) {
            return 0.0; // or throw an exception
        }
    }

    public List<?> getList(String path) {
        Object value = getValue(path);
        if (value instanceof List) {
            return (List<?>) value;
        }
        return null; // or throw an exception
    }
}
