package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.config.PrefixConfig;
import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RpManager extends BasicManagerHandler {
    private final PlayerManager manager;
    private final PrefixConfig config;

    private final HashMap<UUID, Boolean> recPlayers = new HashMap<>();

    public RpManager(PluginProvider plugin, PlayerManager manager, PrefixConfig config) {
        super(plugin);
        this.manager = manager;
        this.config = config;
    }

    @Override
    protected void onInit() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isRp(player)) {
                setRp(player, true);
            }
        });
    }

    @Override
    protected void onDeinit() {
        Bukkit.getOnlinePlayers().forEach(this::cleanup);
    }

    public void setRp(Player player, boolean rp) {
        UUID uuid = player.getUniqueId();

        Boolean currentRec = recPlayers.get(uuid);
        if (currentRec != null && currentRec == rp) return;

        recPlayers.put(uuid, rp);

        if (rp) {
            manager.setPlayerConfig(player, config.rp().config());
        } else {
            manager.removePlayerConfig(player, config.rp().config());
        }
    }

    public boolean isRp(Player player) {
        return recPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void cleanup(Player player) {
        recPlayers.remove(player.getUniqueId());
        manager.removePlayerConfig(player, config.rp().config());
    }
}
