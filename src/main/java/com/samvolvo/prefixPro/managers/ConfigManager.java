package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.PrefixPro;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final PrefixPro plugin;
    private FileConfiguration config;
    private File configFile;
    private final int CURRENT_CONFIG_VERSION = 2;

    public ConfigManager(PrefixPro plugin) {
        this.plugin = plugin;
        createDataFolder(); // Ensure the data folder exists
        loadConfig();
    }

    private void createDataFolder() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                plugin.getLogger().info("Data folder created: " + dataFolder.getPath());
            } else {
                plugin.getLogger().warning("Could not create data folder: " + dataFolder.getPath());
            }
        }
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        checkConfigVersion();
    }

    private void checkConfigVersion() {
        InputStreamReader defaultConfigStream = new InputStreamReader(plugin.getResource("config.yml"));
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);

        int currentVersion = config.getInt("config-version", 1);
        if (currentVersion < CURRENT_CONFIG_VERSION) {
            plugin.getLogger().info("Updating config from version " + currentVersion + " to " + CURRENT_CONFIG_VERSION);
            updateConfig(defaultConfig, currentVersion);
        }
    }

    private void updateConfig(YamlConfiguration defaultConfig, int currentVersion) {
        boolean changes = false;

        // Update from version 1 to 2
        if (currentVersion < 2) {
            if (!config.contains("messages.prefix")) {
                config.set("messages.prefix", defaultConfig.getString("messages.prefix"));
                changes = true;
            }
        }

        // Always update version number
        config.set("config-version", CURRENT_CONFIG_VERSION);
        changes = true;

        if (changes) {
            try {
                saveConfigWithSpacing();
                plugin.getCustomLogger().info("Config has been updated successfully!");
            } catch (IOException e) {
                plugin.getCustomLogger().error("Could not save updated config!");
            }
        }
    }

    private void saveConfigWithSpacing() throws IOException {
        // Get all the lines from the default config (with comments and spacing)
        List<String> defaultLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getResource("config.yml")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                defaultLines.add(line);
            }
        }
        
        // Create a new file writer
        try (FileWriter writer = new FileWriter(configFile)) {
            boolean previousWasSection = false;
            int currentIndent = 0;
            
            for (String line : defaultLines) {
                String trimmedLine = line.trim();
                
                // Calculate indentation
                currentIndent = line.indexOf(trimmedLine);
                String indent = " ".repeat(currentIndent);
                
                // If this is a new section (ends with ':') and the previous line was also a section
                // add an extra newline for spacing
                if (trimmedLine.endsWith(":")) {
                    if (previousWasSection) {
                        writer.write("\n");
                    }
                    previousWasSection = true;
                    writer.write(line + "\n");
                } else if (trimmedLine.startsWith("#")) {
                    // Write comments as-is
                    writer.write(line + "\n");
                } else if (line.contains(":")) {
                    // Handle config values
                    String key = trimmedLine.split(":")[0];
                    String fullPath = getFullPath(line, defaultLines);
                    
                    if (config.contains(fullPath)) {
                        Object value = config.get(fullPath);
                        if (value != null) {
                            String formattedValue = formatValue(value);
                            writer.write(indent + key + ": " + formattedValue + "\n");
                        }
                    } else {
                        writer.write(line + "\n");
                    }
                    previousWasSection = false;
                } else {
                    writer.write(line + "\n");
                    previousWasSection = false;
                }
            }
        }
    }

    private String formatValue(Object value) {
        if (value instanceof String) {
            String stringValue = (String) value;
            // Add quotes if the string contains special characters or spaces
            if (stringValue.contains(" ") || stringValue.contains("&") || 
                stringValue.contains("[") || stringValue.contains("]") ||
                stringValue.contains("{") || stringValue.contains("}") ||
                stringValue.isEmpty()) {
                return "\"" + stringValue + "\"";
            }
        }
        return value.toString();
    }

    private String getFullPath(String line, List<String> allLines) {
        StringBuilder path = new StringBuilder();
        int currentIndent = line.indexOf(line.trim());
        String currentSection = line.trim().split(":")[0];
        
        // Add current section to path
        path.insert(0, currentSection);
        
        // Look backwards through lines to build full path
        for (int i = allLines.indexOf(line) - 1; i >= 0; i--) {
            String previousLine = allLines.get(i);
            if (previousLine.trim().isEmpty() || previousLine.trim().startsWith("#")) {
                continue;
            }
            
            int previousIndent = previousLine.indexOf(previousLine.trim());
            if (previousIndent < currentIndent && previousLine.trim().endsWith(":")) {
                String section = previousLine.trim().split(":")[0];
                path.insert(0, section + ".");
                currentIndent = previousIndent;
            }
        }
        
        return path.toString();
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        loadConfig();
    }
} 