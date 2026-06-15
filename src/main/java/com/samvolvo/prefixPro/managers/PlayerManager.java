package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.utils.ColorUtil;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Optional;

public class PlayerManager extends BasicManagerHandler {
    private static final String TEAM_PREFIX = "nt_";

    protected final ScoreboardManager scoreboardManager;

    protected PrefixConfig config;

    public PlayerManager(PluginProvider plugin, PrefixConfig config) {
        super(plugin);
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.config = config;
    }

    @Override
    protected void onInit() {
        Bukkit.getOnlinePlayers().forEach(this::clearPlayerNametag);
    }

    @Override
    protected void onDeinit() {
        Bukkit.getOnlinePlayers().forEach(this::clearPlayerNametag);
    }

   public void updatePlayer(Player player, String prefix, String suffix) {
        String teamName = getTeamName(player);
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

        Team oldTeam = scoreboard.getTeam(teamName);
        String currentPrefix = prefix;
        String currentSuffix =  suffix;

        if (oldTeam != null) {
            if (prefix == null) currentPrefix = oldTeam.getPrefix();
            if (suffix == null) currentSuffix = oldTeam.getSuffix();
            oldTeam.unregister();
        }

        Team team = scoreboard.registerNewTeam(teamName);
        team.addEntry(player.getName());

        if (config.isNametag()) {
            team.setPrefix(currentPrefix != null ? currentPrefix : "");
            team.setSuffix(currentSuffix != null ? currentSuffix : "");
        }

        updateTabList(player, currentPrefix, currentSuffix);
   }

    public void setPlayerPrefix(Player player, String prefix) {
        updatePlayer(player, prefix, null);
    }

    public void setPlayerSuffix(Player player, String suffix) {
        updatePlayer(player, null, suffix);
    }

    public void removePlayerPrefix(Player player) {
        updatePlayer(player, "", null);
    }

    public void removePlayerSuffix(Player player) {
        updatePlayer(player, null, "");
    }

    public void clearPlayerNametag(Player player) {
        String teamName = getTeamName(player);
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            team.unregister();
        }

        player.setPlayerListName(player.getName());
    }

    public String getPlayerPrefix(Player player) {
        return getTeam(player)
                .map(Team::getPrefix)
                .orElse("");
    }

    public String getPlayerSuffix(Player player) {
        return getTeam(player)
                .map(Team::getSuffix)
                .orElse("");
    }

    private String getTeamName(Player player) {
        return TEAM_PREFIX + player.getName();
    }

    private Optional<Team> getTeam(Player player) {
        String teamName = getTeamName(player);
        return Optional.ofNullable(scoreboardManager.getMainScoreboard().getTeam(teamName));
    }

    private void updateTabList(Player player, String prefix, String suffix) {
        if (!config.isTab()) {
            return;
        }

        String finalPrefix = prefix != null ? ColorUtil.colorize(prefix) : "";
        String finalSuffix = suffix != null ? ColorUtil.colorize(suffix) : "";

        String finalName = finalPrefix + ChatColor.RESET + player.getName() + ChatColor.RESET + suffix;
        player.setPlayerListName(finalName);

        PrefixNode prefixNode = PrefixNode.builder(finalPrefix, 100).build();
        SuffixNode suffixNode = SuffixNode.builder(finalSuffix, 150).build();

        User user = PrefixPro.luckPermsProvider.getUserManager().getUser(player.getUniqueId());

        if (finalPrefix.isEmpty()) {
            user.data().clear(n -> n.getType().matches(prefixNode));
        } else {
            user.data().add(prefixNode);
        }

        if (finalSuffix.isEmpty()) {
            user.data().clear(n -> n.getType().matches(suffixNode));
        } else {
            user.data().add(suffixNode);
        }
    }

    /**
     * Ensures the player's name inherits the final colors from the prefix when no reset is present.
     */
    public String applyPrefixColorToName(String prefix, String playerName) {
        if (prefix == null || prefix.isEmpty()) {
            return playerName;
        }
        String lastColors = ColorUtil.getLastColors(prefix);
        return lastColors.isEmpty() ? playerName : lastColors + playerName;
    }
}
