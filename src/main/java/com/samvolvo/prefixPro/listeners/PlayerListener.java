package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.utils.ColorUtil;
import com.samvolvo.prefixPro.PrefixPro;
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

    public PlayerListener(PrefixPro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getServer().getScheduler().runTaskLater(
            plugin,
            () -> plugin.getPrefixManager().updatePlayerPrefix(event.getPlayer()),
            1L
        );
        
        if (event.getPlayer().hasPermission("prefixpro.afk")) {
            plugin.getAfkManager().updateActivity(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getAfkManager().cleanup(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        // Only count movement if it's a block change (not just looking around)
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
            event.getFrom().getBlockY() != event.getTo().getBlockY() ||
            event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            
            Player player = event.getPlayer();
            
            if (plugin.getAfkManager().isAfk(player)) {
                // If player is actively moving (not just being pushed)
                if (player.isSprinting() || player.isSneaking()) {
                    plugin.getAfkManager().setAfk(player, false);
                } else {
                    // Cancel movement if being pushed
                    event.setTo(event.getFrom());
                }
            } else {
                plugin.getAfkManager().updateActivity(player);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (plugin.getConfig().getBoolean("display.chat", true)) {
            String prefix = plugin.getPrefixManager().getPrefix(event.getPlayer());
            String playerName = plugin.getPrefixManager()
                .applyPrefixColorToName(prefix, ColorUtil.colorize(event.getPlayer().getDisplayName()));
            String suffix = plugin.getAfkManager().isAfk(event.getPlayer()) 
                ? ColorUtil.colorize(plugin.getConfig().getString("afk.suffix", " &7[AFK]")) 
                : "";
            event.setFormat(prefix + playerName + suffix + ChatColor.RESET + ": %2$s");
        }
        plugin.getAfkManager().updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        plugin.getAfkManager().updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getAfkManager().isAfk(player)) {
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
                    if (plugin.getAfkManager().isAfk(player)) {
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
                    if (plugin.getAfkManager().isAfk(player)) {
                        event.setCancelled(true);
                    }
                }
            });
        });
    }
} 
