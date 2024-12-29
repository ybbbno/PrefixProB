package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.PrefixManager;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckPermsListener {
    private final JavaPlugin plugin;
    private final PrefixManager prefixManager;

    public LuckPermsListener(JavaPlugin plugin, PrefixManager prefixManager) {
        this.plugin = plugin;
        this.prefixManager = prefixManager;
    }

    public void onNodeAdd(NodeAddEvent event) {
        updatePlayerIfOnline(event.getTarget());
    }

    public void onNodeRemove(NodeRemoveEvent event) {
        updatePlayerIfOnline(event.getTarget());
    }

    public void onDataRecalculate(UserDataRecalculateEvent event) {
        User user = event.getUser();
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player != null && player.isOnline()) {
            Bukkit.getScheduler().runTask(plugin, () -> prefixManager.updatePlayerPrefix(player));
        }
    }

    private void updatePlayerIfOnline(Object target) {
        if (target instanceof User) {
            User user = (User) target;
            Player player = Bukkit.getPlayer(user.getUniqueId());
            if (player != null && player.isOnline()) {
                // Run task on next tick to ensure LuckPerms has updated its cache
                Bukkit.getScheduler().runTask(plugin, () -> prefixManager.updatePlayerPrefix(player));
            }
        }
    }
} 