package com.samvolvo.prefixPro;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class PrefixManager {
    private final LuckPerms luckPerms;
    private final ScoreboardManager scoreboardManager;
    private final PrefixPro plugin;

    public PrefixManager(PrefixPro plugin, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
        this.scoreboardManager = org.bukkit.Bukkit.getScoreboardManager();
    }

    public String getPrefix(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return prefix != null ? ChatColor.translateAlternateColorCodes('&', prefix) + " " : "";
    }

    public void updatePlayerPrefix(Player player) {
        String prefix = getPrefix(player);
        String teamName = "nt_" + player.getName();
        
        // Get the main scoreboard
        Scoreboard mainScoreboard = scoreboardManager.getMainScoreboard();
        
        // Handle nametag team
        Team team = mainScoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }
        
        // Create new team for nametag
        team = mainScoreboard.registerNewTeam(teamName);
        if (plugin.getConfig().getBoolean("display.nametag", true)) {
            team.setPrefix(prefix);
        }
        team.addEntry(player.getName());
        
        // Update tab list name if enabled
        if (plugin.getConfig().getBoolean("display.tab", true)) {
            player.setPlayerListName(prefix + ChatColor.RESET + player.getName());
        }
        
        // Keep original name for vanilla interactions
        player.setDisplayName(player.getName());
        player.setCustomNameVisible(false);
    }

    public void updatePlayerAfk(Player player, String suffix) {
        String prefix = getPrefix(player);
        String teamName = "nt_" + player.getName();
        
        Team team = player.getScoreboard().getTeam(teamName);
        if (team != null) {
            if (plugin.getConfig().getBoolean("display.nametag", true)) {
                team.setSuffix(suffix);
            }
        }
        
        if (plugin.getConfig().getBoolean("display.tab", true)) {
            player.setPlayerListName(prefix + ChatColor.RESET + player.getName() + suffix);
        }
    }
} 