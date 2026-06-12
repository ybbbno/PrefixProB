package com.samvolvo.prefixPro.managers;

import com.samvolvo.prefixPro.PrefixPro;
import com.samvolvo.prefixPro.config.PrefixConfig;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PrefixRecManager extends PlayerManager {
    private final HashMap<UUID, Boolean> recPlayers = new HashMap<>();

    public PrefixRecManager(PluginProvider plugin, PrefixConfig config) {
        super(plugin, config);
    }

    @Override
    protected void onInit() {
        super.onInit();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (isRec(player)) {
                setRec(player, true);
            }
        });
    }

    @Override
    protected void onDeinit() {
        super.onDeinit();

        Bukkit.getOnlinePlayers().forEach(this::cleanup);
    }

    public void setRec(Player player, boolean rec) {
        UUID uuid = player.getUniqueId();

        Boolean currentRec = recPlayers.get(uuid);
        if (currentRec != null && currentRec == rec) return;

        recPlayers.put(uuid, rec);

        if (rec) {
            setPlayerPrefix(player, config.rec().prefix());
        } else {
            removePlayerPrefix(player);
        }
    }

    public boolean isRec(Player player) {
        return recPlayers.getOrDefault(player.getUniqueId(), false);
    }

    public void cleanup(Player player) {
        recPlayers.remove(player.getUniqueId());
    }
}
