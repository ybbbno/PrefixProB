package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.PrefixPro;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class AfkListener implements Listener {
    private PrefixPro plugin;

    public AfkListener(PrefixPro plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneakEvent(PlayerToggleSneakEvent event) {
        if (plugin.getAfkManager().isAfk(event.getPlayer()) && event.isSneaking()) {
            plugin.getAfkManager().setAfk(event.getPlayer(), false);
        }
    }
}
