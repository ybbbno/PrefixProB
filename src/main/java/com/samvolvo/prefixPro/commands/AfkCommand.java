package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCommand implements CommandExecutor {
    private final PrefixPro plugin;

    public AfkCommand(PrefixPro plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PLAYER_CONSOLE_ONLY);
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("prefixpro.afk")) {
            player.sendMessage(Messages.PLAYER_NO_PERMISSION);
            return true;
        }

        plugin.getAfkManager().setAfk(player, !plugin.getAfkManager().isAfk(player));
        return true;
    }
} 