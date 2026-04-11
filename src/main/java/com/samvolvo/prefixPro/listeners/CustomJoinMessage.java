package com.samvolvo.prefixPro.listeners;

import com.samvolvo.prefixPro.PrefixPro;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CustomJoinMessage implements Listener {
    private PrefixPro plugin;
    private FileConfiguration config;

    public CustomJoinMessage(PrefixPro plugin){
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    // Custom Join Message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        if (!config.getBoolean("messages.join-leave-message.enabled")){
            event.setJoinMessage(null);
            return;
        }

        String playerName = event.getPlayer().getName();

        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&e" + playerName +" joined the game"));
    }

    // Custom Leave message
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){

        if (!config.getBoolean("messages.join-leave-message.enabled")){
            event.setQuitMessage(null);
            return;
        }

        String playerName = event.getPlayer().getName();

        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&e" + playerName + " left the game"));
    }


}
