package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class PrefixProCommand implements CommandExecutor, TabCompleter {
    private final PrefixPro plugin;

    public PrefixProCommand(PrefixPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PrefixConfig config = plugin.getPrefixConfig();

        if (!sender.hasPermission("prefixprob.admin")) {
            sender.sendMessage(config.playerNoPermissionMessage());
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(config.commandUsageMessage());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(config.commandPluginReloadedMessage());
            return true;
        }

        sender.sendMessage(config.commandUsageMessage());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of("reload");
    }
} 