package com.samvolvo.prefixPro;

import com.samvolvo.prefixPro.commands.AfkCommand;
import com.samvolvo.prefixPro.commands.PrefixProCommand;
import com.samvolvo.prefixPro.commands.RecCommand;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.config.PrefixConfigManager;
import com.samvolvo.prefixPro.listeners.CustomJoinMessage;
import com.samvolvo.prefixPro.listeners.PlayerListener;
import com.samvolvo.prefixPro.managers.PrefixRecManager;
import com.samvolvo.prefixPro.managers.SuffixAfkManager;
import me.deadybbb.ybmj.PluginProvider;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class PrefixPro extends PluginProvider {
    private SuffixAfkManager afkManager;
    private PrefixRecManager recManager;

    private PrefixConfigManager manager;
    private PrefixConfig config;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        manager = new PrefixConfigManager(this);
        config = manager.getConfig();

        afkManager = new SuffixAfkManager(this, config);
        afkManager.init();

        recManager = new PrefixRecManager(this, config);
        recManager.init();

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomJoinMessage(this), this);
        
        // Register commands
        getCommand("afk").setExecutor(new AfkCommand(this, afkManager));
        getCommand("rec").setExecutor(new RecCommand(this, recManager));
        getCommand("prefixprob").setExecutor(new PrefixProCommand(this));
    }

    @Override
    public void onDisable() {
        afkManager.deinit();
        recManager.deinit();
        logger.info("PrefixPro has been disabled!");
    }

    public void reload() {
        Bukkit.getScheduler().runTask(this, this::onDisable);
        Bukkit.getScheduler().runTask(this, this::onEnable);
    }

    public PrefixConfig getPrefixConfig() {
        return new PrefixConfig(config.messages(), config.display(), config.rec(), config.afk());
    }

    public SuffixAfkManager getAfkManager() {
        return afkManager;
    }

    public PrefixRecManager getRecManager() {
        return recManager;
    }
}
