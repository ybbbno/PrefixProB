package com.samvolvo.prefixPro;

import com.samvolvo.prefixPro.commands.AfkCommand;
import com.samvolvo.prefixPro.commands.PrefixProCommand;
import com.samvolvo.prefixPro.listeners.PlayerListener;
import com.samvolvo.prefixPro.managers.AfkManager;
import com.samvolvo.prefixPro.managers.ConfigManager;
import com.samvolvo.prefixPro.utils.Messages;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrefixPro extends JavaPlugin {
    private LuckPerms luckPerms;
    private PrefixManager prefixManager;
    private AfkManager afkManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize config manager
        configManager = new ConfigManager(this);
        
        // Initialize messages with config prefix
        Messages.updatePrefix(configManager.getConfig());
        
        // Get LuckPerms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            getLogger().severe(Messages.CONSOLE_LUCKPERMS_NOT_FOUND);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        luckPerms = provider.getProvider();
        prefixManager = new PrefixManager(this, luckPerms);
        afkManager = new AfkManager(this);

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        // Register commands
        getCommand("afk").setExecutor(new AfkCommand(this));
        getCommand("prefixpro").setExecutor(new PrefixProCommand(this));
        
        getLogger().info(Messages.CONSOLE_PLUGIN_ENABLED);
    }

    @Override
    public void onDisable() {
        getLogger().info(Messages.CONSOLE_PLUGIN_DISABLED);
    }

    public void reload() {
        configManager.reloadConfig();
        Messages.updatePrefix(configManager.getConfig());
        // Refresh all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            prefixManager.updatePlayerPrefix(player);
            if (afkManager.isAfk(player)) {
                afkManager.setAfk(player, false);
            }
        });
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public AfkManager getAfkManager() {
        return afkManager;
    }

    public FileConfiguration getConfig() {
        return configManager.getConfig();
    }

    @Override
    public void reloadConfig() {
        configManager.reloadConfig();
    }
}
