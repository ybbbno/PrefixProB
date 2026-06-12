package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.managers.SuffixAfkManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCommand implements CommandExecutor {
    private final PrefixPro plugin;
    private final SuffixAfkManager manager;

    public AfkCommand(PrefixPro plugin, SuffixAfkManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PrefixConfig config = plugin.getPrefixConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(config.playerConsoleOnlyMessage());
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("prefixprob.afk")) {
            player.sendMessage(config.playerNoPermissionMessage());
            return true;
        }

        manager.setAfk(player, !manager.isAfk(player));
        return true;
    }
} 