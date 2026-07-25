package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.config.PrefixConfig;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkManager extends BasicManagerHandler {
    private final PlayerManager manager;
    private final PrefixConfig config;

    private final Map<UUID, Long> lastActivity = new HashMap<>();

    private final Map<UUID, BukkitTask> afkTasks = new HashMap<>();
    private final Map<UUID, Boolean> afkPlayers = new HashMap<>();
    private final Map<UUID, BukkitTask> afkTitleTasks = new HashMap<>();

    private final Map<UUID, BukkitTask> countdownTasks = new HashMap<>();

    public AfkManager(PluginProvider plugin, PlayerManager manager, PrefixConfig config) {
        super(plugin);
        this.manager = manager;
        this.config = config;
    }

    @Override
    protected void onInit() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isAfk(player)) {
                setAfk(player, true);
            } else {
                updateActivity(player);
            }
        });
    }

    @Override
    protected void onDeinit() {
        Bukkit.getOnlinePlayers().forEach(this::cleanup);
    }

    public void cancelPendingAfkTasks(Player player) {
        UUID uuid = player.getUniqueId();
        cancelAfkTask(player);

        BukkitTask countdown = countdownTasks.remove(uuid);
        if (countdown != null) countdown.cancel();
    }

    public void registerCountdownTask(Player player, BukkitTask task) {
        cancelPendingAfkTasks(player);
        countdownTasks.put(player.getUniqueId(), task);
    }

    public void updateActivity(Player player) {
        if (!player.hasPermission("prefixprob.afk")) return;

        cancelPendingAfkTasks(player);

        if (isAfk(player) && !player.isSneaking()) return;

        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());

        if (isAfk(player)) {
            setAfk(player, false);
        } else {
            scheduleAfkCheck(player);
        }
    }

    public boolean isAfk(Player player) {
        return afkPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void scheduleAfkCheck(Player player) {
        cancelAfkTask(player);

        if (!isInit() || !config.afk().auto() || afkTasks.containsKey(player.getUniqueId())) {
            return;
        }

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
        if (!player.hasPermission("prefixprob.afk")) return;

        UUID uuid = player.getUniqueId();
        Boolean current = afkPlayers.get(uuid);
        if (current != null && current == afk) return;

        afkPlayers.put(uuid, afk);

        if (afk) {
            player.setInvulnerable(true);
            player.sendMessage(config.playerNowAfkMessage());
            manager.setPlayerConfig(player, config.afk().config());
            startAfkTitle(player);
            cancelPendingAfkTasks(player);
        } else {
            player.setInvulnerable(false);
            player.sendMessage(config.playerNoLongerAfkMessage());
            manager.removePlayerConfig(player, config.afk().config());
            stopAfkTitle(player);
            scheduleAfkCheck(player);
        }
    }

    public void startAfkTitle(Player player) {
        stopAfkTitle(player);

        if (!isInit()) return;

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
        UUID uuid = player.getUniqueId();

        cancelPendingAfkTasks(player);
        stopAfkTitle(player);

        if (Boolean.TRUE.equals(afkPlayers.get(uuid))) {
            player.setInvulnerable(false);
            manager.removePlayerConfig(player, config.afk().config());
        }

        afkTasks.remove(uuid);
        afkTitleTasks.remove(uuid);
        countdownTasks.remove(uuid);
        lastActivity.remove(uuid);
        afkPlayers.remove(uuid);
    }
}
