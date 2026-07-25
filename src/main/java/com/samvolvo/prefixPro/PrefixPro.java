package com.samvolvo.prefixPro;

import com.samvolvo.prefixPro.commands.AfkCommand;
import com.samvolvo.prefixPro.commands.PrefixProCommand;
import com.samvolvo.prefixPro.commands.RecCommand;
import com.samvolvo.prefixPro.commands.RpCommand;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.config.PrefixConfigManager;
import com.samvolvo.prefixPro.listeners.PlayerListener;
import com.samvolvo.prefixPro.managers.PlayerManager;
import com.samvolvo.prefixPro.managers.RecManager;
import com.samvolvo.prefixPro.managers.AfkManager;
import com.samvolvo.prefixPro.managers.RpManager;
import me.deadybbb.ybmj.PluginProvider;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public final class PrefixPro extends PluginProvider {
    private PrefixConfigManager manager;
    private PrefixConfig config;

    private PlayerManager playerManager;

    private AfkManager afkManager;
    private RecManager recManager;
    private RpManager rpManager;

    public static @NotNull LuckPerms luckPermsProvider;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            logger.severe("LuckPerms not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        luckPermsProvider = provider.getProvider();

        manager = new PrefixConfigManager(this);
        config = manager.getConfig();

        playerManager = new PlayerManager(this, config);
        playerManager.init();

        if (config.isRp()) {
            rpManager = new RpManager(this, playerManager, config);
            rpManager.init();
            getCommand("rp").setExecutor(new RpCommand(this));
        }

        if (config.isRec()) {
            recManager = new RecManager(this, playerManager, config);
            recManager.init();
            getCommand("rec").setExecutor(new RecCommand(this));
        }

        if (config.isAfk()) {
            afkManager = new AfkManager(this, playerManager, config);
            afkManager.init();
            getCommand("afk").setExecutor(new AfkCommand(this));
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("prefixprob").setExecutor(new PrefixProCommand(this));
    }

    @Override
    public void onDisable() {
        rpManager.deinit();
        recManager.deinit();
        afkManager.deinit();
    }

    public void reload() {
        Bukkit.getScheduler().runTask(this, this::onDisable);
        Bukkit.getScheduler().runTask(this, this::onEnable);
    }

    public PrefixConfig getPrefixConfig() {
        return new PrefixConfig(config.messages(), config.display(), config.rec(), config.afk(), config.rp());
    }

    public AfkManager getAfkManager() {
        return afkManager;
    }

    public RecManager getRecManager() {
        return recManager;
    }

    public RpManager getRpManager() {
        return rpManager;
    }

    public PlayerManager getPlayerManager() { return playerManager; }
}
