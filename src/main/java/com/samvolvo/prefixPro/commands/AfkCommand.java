package com.samvolvo.prefixPro.commands;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.managers.AfkManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

public class AfkCommand implements CommandExecutor {
    private final PrefixPro plugin;
    private final AfkManager manager;

    public AfkCommand(PrefixPro plugin) {
        this.plugin = plugin;
        this.manager = plugin.getAfkManager();
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

        if (manager.isAfk(player)) {
            manager.setAfk(player, false);
        } else {
            manager.cancelPendingAfkTasks(player);

            startAfkCountdown(player);
        }
        return true;
    }

    private void startAfkCountdown(Player player) {
        AtomicInteger ticks = new AtomicInteger(0);
        PrefixConfig config = plugin.getPrefixConfig();

        int countdown = config.afk().countdown() * 20;
        String actionbarCountdown = config.afk().actionbarCountdown();

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!player.isOnline() || manager.isAfk(player)) {
                return;
            }

            int remainingTime = (countdown - ticks.addAndGet(20)) / 20;
            player.sendActionBar(actionbarCountdown.replace("%s", (remainingTime + 1)+""));

            if (remainingTime < 0)
                manager.setAfk(player, true);
        }, 0L, 20L);

        manager.registerCountdownTask(player, task);
    }
} 