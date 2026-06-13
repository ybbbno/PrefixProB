package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.config.PrefixConfig;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SuffixAfkManager extends PlayerManager {
    private final Map<UUID, Long> lastActivity = new HashMap<>();
    private final Map<UUID, BukkitTask> afkTasks = new HashMap<>();
    private final Map<UUID, Boolean> afkPlayers = new HashMap<>();
    private final Map<UUID, BukkitTask> afkTitleTasks = new HashMap<>();

    public SuffixAfkManager(PluginProvider plugin, PrefixConfig config) {
        super(plugin, config);
    }

    @Override
    protected void onInit() {
        super.onInit();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isAfk(player)) {
                setAfk(player, true);
            }
        });
    }

    @Override
    protected void onDeinit() {
        super.onDeinit();

        Bukkit.getOnlinePlayers().forEach(this::cleanup);
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

    public boolean isAfk(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void scheduleAfkCheck(Player player) {
        cancelAfkTask(player);

        if (!config.afk().auto()) return;

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> setAfk(player, true),
                config.afk().time() * 20L
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
            plugin.logger.warning("Player " + player.getName() + " attempted to use AFK without permission");
            return;
        }

        Boolean currentAfk = afkPlayers.get(player.getUniqueId());
        if (currentAfk != null && currentAfk == afk) return;

        afkPlayers.put(player.getUniqueId(), afk);

        if (afk) {
            player.setInvulnerable(true);
            player.sendMessage(config.playerNowAfkMessage());
            setPlayerSuffix(player, config.afk().suffix());
            startAfkTitle(player);
            cancelAfkTask(player);
        } else {
            player.setInvulnerable(false);
            player.sendMessage(config.playerNoLongerAfkMessage());
            removePlayerSuffix(player);
            stopAfkTitle(player);
            scheduleAfkCheck(player);
        }
    }

    public void startAfkTitle(Player player) {
        stopAfkTitle(player);

        BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (!player.isOnline() || !isAfk(player)) {
                stopAfkTitle(player);
                return;
            }

            player.sendTitle(config.afk().title(), config.afk().subtitle(), 0, 40, 0);
        }, 0L, 30L);

        afkTitleTasks.put(player.getUniqueId(), task);
    }

    public void stopAfkTitle(Player player) {
        BukkitTask task = afkTitleTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }

        player.resetTitle();
    }

    public void cleanup(Player player) {
        cancelAfkTask(player);
        lastActivity.remove(player.getUniqueId());
        afkPlayers.remove(player.getUniqueId());
        removePlayerSuffix(player);
    }
}
