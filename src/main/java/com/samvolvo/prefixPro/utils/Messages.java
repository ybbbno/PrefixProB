package com.samvolvo.prefixPro.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
    private static String prefix = "&8[&6PrefixPro&8] &7"; // Default prefix

    // Console messages (no prefix for console)
    public static final String CONSOLE_PLUGIN_ENABLED = "PrefixPro has been enabled!";
    public static final String CONSOLE_PLUGIN_DISABLED = "PrefixPro has been disabled!";
    public static final String CONSOLE_LUCKPERMS_NOT_FOUND = "LuckPerms not found! Disabling plugin...";

    // Player messages (with prefix)
    public static String PLAYER_NOW_AFK;
    public static String PLAYER_NO_LONGER_AFK;
    public static String PLAYER_NO_PERMISSION;
    public static String PLAYER_CONSOLE_ONLY;
    
    // Command messages (with prefix)
    public static String COMMAND_USAGE;
    public static String COMMAND_CONFIG_RELOADED;
    public static String COMMAND_PLUGIN_RELOADED;

    public static void updatePrefix(FileConfiguration config) {
        prefix = ChatColor.translateAlternateColorCodes('&', 
            config.getString("messages.prefix", "&8[&6PrefixPro&8] &7"));
        
        // Initialize messages with prefix
        PLAYER_NOW_AFK = prefix + "You are now AFK";
        PLAYER_NO_LONGER_AFK = prefix + "You are no longer AFK";
        PLAYER_NO_PERMISSION = prefix + ChatColor.RED + "You don't have permission to use this command!";
        PLAYER_CONSOLE_ONLY = prefix + "This command can only be used by players!";
        
        COMMAND_USAGE = prefix + ChatColor.RED + "Usage: /prefixpro <config|plugin> reload";
        COMMAND_CONFIG_RELOADED = prefix + ChatColor.GREEN + "Configuration reloaded!";
        COMMAND_PLUGIN_RELOADED = prefix + ChatColor.GREEN + "Plugin reloaded!";
    }
} 