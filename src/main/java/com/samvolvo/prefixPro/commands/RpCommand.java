package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.managers.RecManager;
import com.samvolvo.prefixPro.managers.RpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RpCommand implements CommandExecutor {
    private final PrefixPro plugin;
    private final RpManager manager;

    public RpCommand(PrefixPro plugin) {
        this.plugin = plugin;
        this.manager = plugin.getRpManager();
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

        manager.setRp(player, !manager.isRp(player));
        return true;
    }
}
