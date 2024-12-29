package com.samvolvo.prefixPro;

import com.samvolvo.prefixPro.commands.AfkCommand;
import com.samvolvo.prefixPro.commands.PrefixProCommand;
import com.samvolvo.prefixPro.listeners.PlayerListener;
import com.samvolvo.prefixPro.managers.AfkManager;
import com.samvolvo.prefixPro.managers.ConfigManager;
import com.samvolvo.prefixPro.managers.Metrics;
import com.samvolvo.prefixPro.utils.Logger;
import com.samvolvo.prefixPro.utils.Messages;
import com.samvolvo.prefixPro.utils.UpdateChecker;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class PrefixPro extends JavaPlugin {
    private LuckPerms luckPerms;
    private PrefixManager prefixManager;
    private AfkManager afkManager;
    private ConfigManager configManager;
    private UpdateChecker updateChecker;
    private final Logger logger = new Logger();

    @Override
    public void onEnable() {
        // Initialize config manager
        configManager = new ConfigManager(this);

        logger.loading("Booting PrefixPro");
        
        // Initialize messages with config prefix
        Messages.updatePrefix(configManager.getConfig());
        
        // Get LuckPerms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            logger.error(Messages.CONSOLE_LUCKPERMS_NOT_FOUND);
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

        updateChecker = new UpdateChecker(this);
        CheckForUpdates(updateChecker);
        Metrics metrics = new Metrics(this, 23462);
        
        logger.info(Messages.CONSOLE_PLUGIN_ENABLED);
    }

    public void CheckForUpdates(UpdateChecker updateChecker){
        List<String> nameless = updateChecker.generateUpdateMessage(getDescription().getVersion());
        if (!nameless.isEmpty()){
            for (String message : nameless){
                logger.warning(message);
            }
        }
    }

    @Override
    public void onDisable() {
        logger.info(Messages.CONSOLE_PLUGIN_DISABLED);
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

    public Logger getCustomLogger() {
        return logger;
    }

    @Override
    public void reloadConfig() {
        configManager.reloadConfig();
    }
}
