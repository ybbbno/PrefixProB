package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.PrefixManager;
import com.samvolvo.prefixPro.PrefixPro;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LuckPermsListener {
    private final PrefixPro plugin;
    private final PrefixManager prefixManager;

    public LuckPermsListener(PrefixPro plugin, PrefixManager prefixManager) {
        this.plugin = plugin;
        this.prefixManager = prefixManager;
    }

    public void onNodeAdd(NodeAddEvent event) {
        // Add a small delay to ensure LuckPerms has updated its cache
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            updatePlayerIfOnline(event.getTarget());
        }, 1L);
    }

    public void onNodeRemove(NodeRemoveEvent event) {
        // Add a small delay to ensure LuckPerms has updated its cache
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            updatePlayerIfOnline(event.getTarget());
        }, 1L);
    }

    public void onDataRecalculate(UserDataRecalculateEvent event) {
        User user = event.getUser();
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null && player.isOnline()) {
            // Add a small delay to ensure LuckPerms has updated its cache
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                prefixManager.updatePlayerPrefix(player);
            }, 1L);
        }
    }

    private void updatePlayerIfOnline(Object target) {
        if (target instanceof User) {
            User user = (User) target;
            Player player = Bukkit.getPlayer(user.getUniqueId());
            if (player != null && player.isOnline()) {
                prefixManager.updatePlayerPrefix(player);
            }
        }
    }
} 