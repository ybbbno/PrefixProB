package com.samvolvo.prefixPro.config;

import com.samvolvo.prefixPro.config.types.AfkConfig;
import com.samvolvo.prefixPro.config.types.DisplayConfig;
import com.samvolvo.prefixPro.config.types.MessagesConfig;
import com.samvolvo.prefixPro.config.types.RecConfig;
import org.jetbrains.annotations.NotNull;

public record PrefixConfig(MessagesConfig messages, DisplayConfig display, RecConfig rec, AfkConfig afk) {

    public @NotNull String playerNowAfkMessage() {
        return setPrefixToMessage(messages.playerNowAfk());
    }

    public @NotNull String playerNoLongerAfkMessage() {
        return setPrefixToMessage(messages.playerNoLongerAfk());
    }

    public @NotNull String playerNoPermissionMessage() {
        return setPrefixToMessage(messages.playerNoPermissions());
    }

    public @NotNull String playerConsoleOnlyMessage() {
        return setPrefixToMessage(messages.playerConsoleOnly());
    }

    public @NotNull String commandUsageMessage() {
        return setPrefixToMessage(messages.commandUsage());
    }

    public @NotNull String commandPluginReloadedMessage() {
        return setPrefixToMessage(messages.commandPluginReloaded());
    }

    public boolean isTab() {
        return display.tab();
    }

    public boolean isChat() {
        return display.chat();
    }

    public boolean isNametag() {
        return display.nametag();
    }

    public boolean isJoinLeaveMessages() {
        return messages.isJoinLeave();
    }

    private String setPrefixToMessage(String message) {
        return messages.prefix() + message;
    }
}
