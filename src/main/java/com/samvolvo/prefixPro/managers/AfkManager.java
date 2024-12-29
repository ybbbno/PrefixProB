package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkManager {
    private final PrefixPro plugin;
    private final Map<UUID, Long> lastActivity;
    private final Map<UUID, BukkitTask> afkTasks;
    private final Map<UUID, Boolean> afkPlayers;

    public AfkManager(PrefixPro plugin) {
        this.plugin = plugin;
        this.lastActivity = new HashMap<>();
        this.afkTasks = new HashMap<>();
        this.afkPlayers = new HashMap<>();
    }

    public void updateActivity(Player player) {
        if (!player.hasPermission("prefixpro.afk")) return;
        
        if (isAfk(player) && !player.isSneaking()) return;
        
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
        
        if (isAfk(player)) {
            setAfk(player, false);
        }
        
        scheduleAfkCheck(player);
    }

    public void scheduleAfkCheck(Player player) {
        cancelAfkTask(player);
        
        if (!plugin.getConfig().getBoolean("afk.auto-enabled")) return;
        
        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin,
            () -> setAfk(player, true),
            plugin.getConfig().getInt("afk.auto-time") * 20L
        );
        
        afkTasks.put(player.getUniqueId(), task);
    }

    public void cancelAfkTask(Player player) {
        BukkitTask task = afkTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public void setAfk(Player player, boolean afk) {
        if (!player.hasPermission("prefixpro.afk")) {
            plugin.getLogger().warning("Player " + player.getName() + " attempted to use AFK without permission");
            return;
        }
        
        Boolean currentAfk = afkPlayers.get(player.getUniqueId());
        if (currentAfk != null && currentAfk == afk) return;
        
        afkPlayers.put(player.getUniqueId(), afk);
        
        if (afk) {
            player.setInvulnerable(true);
            String suffix = ChatColor.translateAlternateColorCodes('&', 
                plugin.getConfig().getString("afk.suffix", " &7[AFK]"));
            plugin.getPrefixManager().updatePlayerAfk(player, suffix);
            player.sendMessage(Messages.PLAYER_NOW_AFK);
            cancelAfkTask(player);
        } else {
            player.setInvulnerable(false);
            plugin.getPrefixManager().updatePlayerAfk(player, "");
            player.sendMessage(Messages.PLAYER_NO_LONGER_AFK);
            scheduleAfkCheck(player);
        }
    }

    public boolean isAfk(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void cleanup(Player player) {
        cancelAfkTask(player);
        lastActivity.remove(player.getUniqueId());
        afkPlayers.remove(player.getUniqueId());
    }
} 