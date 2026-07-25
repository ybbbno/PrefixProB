package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.config.PrefixConfig;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RecManager extends BasicManagerHandler {
    private final PlayerManager manager;
    private final PrefixConfig config;

    private final HashMap<UUID, Boolean> recPlayers = new HashMap<>();

    public RecManager(PluginProvider plugin, PlayerManager manager, PrefixConfig config) {
        super(plugin);
        this.manager = manager;
        this.config = config;
    }

    @Override
    protected void onInit() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isRec(player)) {
                setRec(player, true);
            }
        });
    }

    @Override
    protected void onDeinit() {
        Bukkit.getOnlinePlayers().forEach(this::cleanup);
    }

    public void setRec(Player player, boolean rec) {
        UUID uuid = player.getUniqueId();

        Boolean currentRec = recPlayers.get(uuid);
        if (currentRec != null && currentRec == rec) return;

        recPlayers.put(uuid, rec);

        if (rec) {
            manager.setPlayerConfig(player, config.rec().config());
        } else {
            manager.removePlayerConfig(player, config.rec().config());
        }
    }

    public boolean isRec(Player player) {
        return recPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void cleanup(Player player) {
        recPlayers.remove(player.getUniqueId());
        manager.removePlayerConfig(player, config.rec().config());
    }
}
