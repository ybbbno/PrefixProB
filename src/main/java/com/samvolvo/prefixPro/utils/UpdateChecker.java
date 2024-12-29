package com.samvolvo.prefixPro.utils;

import com.samvolvo.prefixPro.PrefixPro;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private final PrefixPro plugin;
    private final Logger logger;
    private final String modrinthProjectId;

    public UpdateChecker(PrefixPro plugin, String modrinthProjectId) {
        this.plugin = plugin;
        this.logger = new Logger();
        this.modrinthProjectId = modrinthProjectId;
    }

    public void checkForUpdates() {
        try {
            String currentVersion = plugin.getDescription().getVersion();
            
            URL url = new URL("https://api.modrinth.com/v2/project/" + modrinthProjectId + "/version");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                if (!response.toString().contains(currentVersion)) {
                    logger.warning("A new version of PrefixPro is available!");
                } else {
                    logger.info("You are running the latest version of PrefixPro!");
                }
            }
            
            connection.disconnect();
        } catch (Exception e) {
            logger.error("Failed to check for updates: " + e.getMessage());
        }
    }
}
