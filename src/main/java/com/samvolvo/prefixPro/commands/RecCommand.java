package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.managers.PrefixRecManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RecCommand implements CommandExecutor {
    private final PrefixPro plugin;
    private final PrefixRecManager manager;

    public RecCommand(PrefixPro plugin, PrefixRecManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        PrefixConfig config = plugin.getPrefixConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(config.playerConsoleOnlyMessage());
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("prefixprob.rec")) {
            player.sendMessage(config.playerNoPermissionMessage());
            return true;
        }

        manager.setRec(player, !manager.isRec(player));
        return true;
    }
}
