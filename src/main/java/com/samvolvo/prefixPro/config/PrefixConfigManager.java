package com.samvolvo.prefixPro.config;

import com.samvolvo.prefixPro.config.types.AfkConfig;
import com.samvolvo.prefixPro.config.types.DisplayConfig;
import com.samvolvo.prefixPro.config.types.MessagesConfig;
import com.samvolvo.prefixPro.config.types.RecConfig;
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
        RecConfig rec = getRec();
        AfkConfig afk = getAfk();

        return new PrefixConfig(messages, display, rec, afk);
    }

    @Contract(" -> new")
    private @NotNull MessagesConfig getMessages() {
        ConfigurationSection section = config.getConfigurationSection("messages");
        if (section == null) return new MessagesConfig(
            "§c[!] ",
            "§fYou are now AFK",
            "§fYou are no longer AFK",
            "You don't have permission to use this command!",
            "This command can only be used by players!",
            "Usage: /prefixprob reload",
            "§aPlugin reloaded!",
            true
        );

        String prefix = section.getString("prefix", "§c[!] ");
        String playerNowAfk = section.getString("player_now_afk", "§fYou are now AFK");
        String playerNoLongerAfk = section.getString("player_no_longer_afk", "§fYou are no longer AFK");
        String playerNoPermission = section.getString("player_no_permission", "You don't have permission to use this command!");
        String playerConsoleOnly = section.getString("player_console_only", "This command can only be used by players!");
        String commandUsage = section.getString("command_usage", "Usage: /prefixprob reload");
        String commandPluginReloaded = section.getString("command_plugin_reloaded", "§aPlugin reloaded!");
        boolean joinLeaveMessage = section.getBoolean("join-leave-message", true);

        return new MessagesConfig(
            prefix,
            playerNowAfk,
            playerNoLongerAfk,
            playerNoPermission,
            playerConsoleOnly,
            commandUsage,
            commandPluginReloaded,
            joinLeaveMessage
        );
    }

    @Contract(" -> new")
    private @NotNull DisplayConfig getDisplay() {
        ConfigurationSection section = config.getConfigurationSection("display");
        if (section == null) return new DisplayConfig(true, true, true);

        boolean tab = section.getBoolean("tab", true);
        boolean chat = section.getBoolean("chat", true);
        boolean nametag = section.getBoolean("nametag", true);

        return new DisplayConfig(tab, chat, nametag);
    }

    @Contract(" -> new")
    private @NotNull AfkConfig getAfk() {
        ConfigurationSection section = config.getConfigurationSection("afk");
        if (section == null) return new AfkConfig(
            300,
            " &7[⌚]",
            "§cAFK",
            "§7Sneak To exit AFK",
            true
        );

        int time = section.getInt("auto-time", 300);
        String suffix = section.getString("suffix", " &7[⌚]");
        String screenTitle = section.getString("screen_title", " §cAFK");
        String screenSubtitle = section.getString("screen_subtitle", "§7Sneak To exit AFK");
        boolean auto = section.getBoolean("auto-enabled", true);

        return new AfkConfig(time, suffix, screenTitle, screenSubtitle, auto);
    }

    private @NotNull RecConfig getRec() {
        ConfigurationSection section = config.getConfigurationSection("rec");
        if (section == null) return new RecConfig("§4[⏺] §f");

        String prefix = section.getString("prefix", "§4[⏺] §f");

        return new RecConfig(prefix);
    }
}
