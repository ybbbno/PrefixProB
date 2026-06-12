package com.samvolvo.prefixPro.config.types;

public record MessagesConfig(
        String prefix,
        String playerNowAfk,
        String playerNoLongerAfk,
        String playerNoPermissions,
        String playerConsoleOnly,
        String commandUsage,
        String commandPluginReloaded,
        boolean isJoinLeave
) { }
