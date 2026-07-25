package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import com.samvolvo.prefixPro.config.types.PlayerConfig;
import com.samvolvo.prefixPro.utils.ColorUtil;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlayerManager extends BasicManagerHandler {
    private static final String TEAM_PREFIX = "nt_";

    protected final ScoreboardManager scoreboardManager;
    protected PrefixConfig config;

    private final HashMap<UUID, List<PlayerConfig>> configs = new HashMap<>();

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

        if (!config.isNametagVisible())
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        team.setPrefix(currentPrefix != null ? currentPrefix : "");
        team.setSuffix(currentSuffix != null ? currentSuffix : "");

        updateTabList(player, currentPrefix, currentSuffix);
    }

    public void setPlayerConfig(Player player, PlayerConfig config) {
        UUID uuid = player.getUniqueId();

        List<PlayerConfig> configList = configs.getOrDefault(uuid, new ArrayList<>());
        if (configList.isEmpty() || configList.stream().noneMatch(
                p -> p.name().equalsIgnoreCase(config.name()))) {
            configList.add(config);
        }

        updatePlayer(player, "", "");

        String prefix = getPrefixes(configList);
        String suffix = getSuffixes(configList);

        updatePlayer(player, prefix, suffix);

        configs.put(uuid, configList);
    }

    public void removePlayerConfig(Player player, PlayerConfig config) {
        UUID uuid = player.getUniqueId();
        List<PlayerConfig> configList = configs.getOrDefault(uuid, new ArrayList<>());

        configList.removeIf(p -> p.name().equalsIgnoreCase(config.name()));

        String prefix = getPrefixes(configList);
        String suffix = getSuffixes(configList);

        updatePlayer(player, prefix, suffix);

        configs.put(uuid, configList);
    }

    private @NotNull String getPrefixes(List<PlayerConfig> configList) {
        if (configList == null || configList.isEmpty()) {
            return "";
        }

        PriorityQueue<PlayerConfig> prefixes = new PriorityQueue<>(
                (p1, p2) -> Integer.compare(p1.prefixPriority(), p2.prefixPriority())
        );
        prefixes.addAll(configList);

        StringBuilder prefix = new StringBuilder();
        while (!prefixes.isEmpty()) {
            prefix.append(prefixes.poll().prefix());
        }
        return prefix.toString();
    }

    private @NotNull String getSuffixes(List<PlayerConfig> configList) {
        if (configList == null || configList.isEmpty()) {
            return "";
        }

        PriorityQueue<PlayerConfig> suffixes = new PriorityQueue<>(
                (p1, p2) -> Integer.compare(p1.suffixPriority(), p2.suffixPriority())
        );
        suffixes.addAll(configList);

        StringBuilder suffix = new StringBuilder();
        while (!suffixes.isEmpty()) {
            suffix.append(suffixes.poll().suffix());
        }
        return suffix.toString();
    }

    private void setPlayerPrefix(Player player, String prefix) {
        updatePlayer(player, prefix, null);
    }

    private void setPlayerSuffix(Player player, String suffix) {
        updatePlayer(player, null, suffix);
    }

    private void removePlayerPrefix(Player player) {
        updatePlayer(player, "", null);
    }

    private void removePlayerSuffix(Player player) {
        updatePlayer(player, null, "");
    }

    private void clearPlayerNametag(Player player) {
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

        String finalName = finalPrefix + ChatColor.RESET + player.getName() + ChatColor.RESET + finalSuffix;
        player.setPlayerListName(finalName);

        PrefixNode prefixNode = PrefixNode.builder(finalPrefix, 150).build();
        SuffixNode suffixNode = SuffixNode.builder(finalSuffix, 150).build();

        User user = PrefixPro.luckPermsProvider.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

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

        PrefixPro.luckPermsProvider.getUserManager().saveUser(user);
    }
}
