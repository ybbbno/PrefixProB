package com.samvolvo.prefixPro.config;

import com.samvolvo.prefixPro.config.types.*;
import me.deadybbb.ybmj.BasicConfigHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PrefixConfigManager extends BasicConfigHandler {
    public PrefixConfigManager(PluginProvider plugin) {
        super(plugin, "config.yml");
    }

    public PrefixConfig getConfig() {
        reloadConfig();

        MessagesConfig messages = getMessages();
        DisplayConfig display = getDisplay();
        RpConfig rp = getRp();
        RecConfig rec = getRec();
        AfkConfig afk = getAfk();

        return new PrefixConfig(messages, display, rec, afk, rp);
    }

    @Contract(" -> new")
    private @NotNull MessagesConfig getMessages() {
        String prefix = DefaultsConfig.m_prefix;
        String playerNowAfk = DefaultsConfig.m_playerNowAfk;
        String playerNoLongerAfk = DefaultsConfig.m_playerNoLongerAfk;
        String playerNoPermission = DefaultsConfig.m_playerNoPermission;
        String playerConsoleOnly = DefaultsConfig.m_playerConsoleOnly;
        String commandUsage = DefaultsConfig.m_commandUsage;
        String commandPluginReloaded = DefaultsConfig.m_commandPluginReloaded;

        ConfigurationSection section = config.getConfigurationSection("messages");
        if (section == null)
            return new MessagesConfig(
                prefix, playerNowAfk,
                playerNoLongerAfk, playerNoPermission,
                playerConsoleOnly, commandUsage,
                commandPluginReloaded);

        prefix = section.getString("prefix", prefix);
        playerNowAfk = section.getString("player-now-afk", playerNowAfk);
        playerNoLongerAfk = section.getString("player-no-longer-afk", playerNoLongerAfk);
        playerNoPermission = section.getString("player-no-permission", playerNoPermission);
        playerConsoleOnly = section.getString("player-console-only", playerConsoleOnly);
        commandUsage = section.getString("command-usage", commandUsage);
        commandPluginReloaded = section.getString("command-plugin-reloaded", commandPluginReloaded);

        return new MessagesConfig(
                prefix, playerNowAfk, playerNoLongerAfk,
                playerNoPermission, playerConsoleOnly, commandUsage,
                commandPluginReloaded);
    }

    @Contract(" -> new")
    private @NotNull DisplayConfig getDisplay() {
        boolean tab = DefaultsConfig.d_tab;
        boolean chat = DefaultsConfig.d_chat;
        boolean nametag = DefaultsConfig.d_nametag;

        ConfigurationSection section = config.getConfigurationSection("display");
        if (section == null)
            return new DisplayConfig(tab, chat, nametag);

        tab = section.getBoolean("tab", tab);
        chat = section.getBoolean("chat", chat);
        nametag = section.getBoolean("is-nametag-visible", nametag);

        return new DisplayConfig(tab, chat, nametag);
    }

    @Contract(" -> new")
    private @NotNull AfkConfig getAfk() {
        boolean enabled = DefaultsConfig.afk_enabled;
        String name = DefaultsConfig.afk_name;
        String prefix = DefaultsConfig.afk_prefix;
        int prefixPriority = DefaultsConfig.afk_prefixPriority;
        String suffix = DefaultsConfig.afk_suffix;
        int suffixPriority = DefaultsConfig.afk_suffixPriority;
        String screenTitle = DefaultsConfig.afk_screenTitle;
        String screenSubtitle = DefaultsConfig.afk_screenSubtitle;
        boolean auto = DefaultsConfig.afk_auto;
        int time = DefaultsConfig.afk_time;
        int countdown = DefaultsConfig.afk_countdown;
        String actionbarCountdown = DefaultsConfig.afk_actionbarCountdown;

        PlayerConfig player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        ConfigurationSection section = config.getConfigurationSection("afk");
        if (section == null)
            return new AfkConfig(enabled, player, screenTitle, screenSubtitle, auto, time, countdown, actionbarCountdown);

        prefix = section.getString("prefix", prefix);
        prefixPriority = section.getInt("prefix-priority", prefixPriority);
        suffix = section.getString("suffix", suffix);
        suffixPriority = section.getInt("suffix-priority", suffixPriority);
        screenTitle = section.getString("screen-title", screenTitle);
        screenSubtitle = section.getString("screen-subtitle", screenSubtitle);
        auto = section.getBoolean("auto-enabled", auto);
        time = section.getInt("auto-time", time);
        countdown = section.getInt("countdown", countdown);
        actionbarCountdown = section.getString("actionbar-countdown", actionbarCountdown);

        player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        return new AfkConfig(enabled, player, screenTitle, screenSubtitle, auto, time, countdown, actionbarCountdown);
    }

    private @NotNull RecConfig getRec() {
        boolean enabled = DefaultsConfig.rec_enabled;
        String name = DefaultsConfig.rec_name;
        String prefix = DefaultsConfig.rec_prefix;
        int prefixPriority = DefaultsConfig.rec_prefixPriority;
        String suffix = DefaultsConfig.rec_suffix;
        int suffixPriority = DefaultsConfig.rec_suffixPriority;

        PlayerConfig player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        ConfigurationSection section = config.getConfigurationSection("rec");
        if (section == null)
            return new RecConfig(enabled, player);

        prefix = section.getString("prefix", prefix);
        prefixPriority = section.getInt("prefix-priority", prefixPriority);
        suffix = section.getString("suffix", suffix);
        suffixPriority = section.getInt("suffix-priority", suffixPriority);

        player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        return new RecConfig(enabled, player);
    }

    private @NotNull RpConfig getRp() {
        boolean enabled = DefaultsConfig.rp_enabled;
        String name = DefaultsConfig.rp_name;
        String prefix = DefaultsConfig.rp_prefix;
        int prefixPriority = DefaultsConfig.rp_prefixPriority;
        String suffix = DefaultsConfig.rp_suffix;
        int suffixPriority = DefaultsConfig.rp_suffixPriority;

        PlayerConfig player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        ConfigurationSection section = config.getConfigurationSection("rp");
        if (section == null)
            return new RpConfig(enabled, player);

        prefix = section.getString("prefix", prefix);
        prefixPriority = section.getInt("prefix-priority", prefixPriority);
        suffix = section.getString("suffix", suffix);
        suffixPriority = section.getInt("suffix-priority", suffixPriority);

        player = new PlayerConfig(name, prefix, prefixPriority, suffix, suffixPriority);

        return new RpConfig(enabled, player);
    }
}
