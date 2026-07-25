package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.managers.PlayerManager;
import com.samvolvo.prefixPro.managers.RecManager;
import com.samvolvo.prefixPro.managers.AfkManager;
import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.utils.ColorUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.ChatColor;

public class PlayerListener implements Listener {
    private final PrefixPro plugin;
    private final PlayerManager manager;
    private final AfkManager afkManager;

    public PlayerListener(PrefixPro plugin) {
        this.plugin = plugin;
        this.manager = plugin.getPlayerManager();
        this.afkManager = plugin.getAfkManager();
    }

    @EventHandler
    public void onSneakEvent(PlayerToggleSneakEvent event) {
        if (afkManager.isAfk(event.getPlayer()) && event.isSneaking()) {
            afkManager.setAfk(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.getPlayer().getServer().getScheduler().runTaskLater(
            plugin,
            () -> {
                if (afkManager.isAfk(player)) {
                    afkManager.setAfk(player, true);
                }

//                if (recManager.isRec(player)) {
//                    recManager.setRec(player, true);
//                }
            },
            1L
        );

        if (player.hasPermission("prefixprob.afk")) {
            afkManager.updateActivity(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        afkManager.cleanup(player);
//        recManager.cleanup(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("prefixprob.afk")) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        // Only real movement (block change)
        if (from.getBlockX() == to.getBlockX() &&
            from.getBlockY() == to.getBlockY() &&
            from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        if (afkManager.isAfk(player)) {
            // Allow intentional movement to disable AFK
            if (player.isSprinting() || player.isSneaking() ||
                player.getVelocity().lengthSquared() > 0.01) {
                afkManager.setAfk(player, false);
            } else {
                // Prevent being pushed by pistons/entities/etc.
                event.setCancelled(true);
            }
        } else {
            afkManager.updateActivity(player);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PrefixConfig config = plugin.getPrefixConfig();
        Player player = event.getPlayer();

        if (config.isChat()) {
            String prefix = manager.getPlayerPrefix(player);
            String suffix = manager.getPlayerSuffix(player);

            String finalName = ColorUtil.colorize(prefix) + ChatColor.RESET + player.getName() + ChatColor.RESET + ColorUtil.colorize(suffix);
            event.setFormat(finalName + ChatColor.RESET + ": %2$s");
        }

        afkManager.updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        afkManager.updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (afkManager.isAfk(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        event.getBlocks().forEach(block -> {
            block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (afkManager.isAfk(player)) {
                        event.setCancelled(true);
                    }
                }
            });
        });
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        event.getBlocks().forEach(block -> {
            block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (afkManager.isAfk(player)) {
                        event.setCancelled(true);
                    }
                }
            });
        });
    }
} 
