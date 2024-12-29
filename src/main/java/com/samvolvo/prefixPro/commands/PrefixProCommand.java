package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrefixProCommand implements CommandExecutor, TabCompleter {
    private final PrefixPro plugin;

    public PrefixProCommand(PrefixPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("prefixpro.admin")) {
            sender.sendMessage(Messages.PLAYER_NO_PERMISSION);
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(Messages.COMMAND_USAGE);
            return true;
        }

        if (args[1].equalsIgnoreCase("reload")) {
            if (args[0].equalsIgnoreCase("config")) {
                plugin.reloadConfig();
                sender.sendMessage(Messages.COMMAND_CONFIG_RELOADED);
                return true;
            } else if (args[0].equalsIgnoreCase("plugin")) {
                plugin.onDisable();
                plugin.onEnable();
                sender.sendMessage(Messages.COMMAND_PLUGIN_RELOADED);
                return true;
            }
        }

        sender.sendMessage(Messages.COMMAND_USAGE);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("config", "plugin").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.asList("reload").stream()
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }
} 