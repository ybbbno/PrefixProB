package com.samvolvo.prefixPro.config;

import com.samvolvo.prefixPro.config.types.*;
import org.jetbrains.annotations.NotNull;

public record PrefixConfig(MessagesConfig messages, DisplayConfig display, RecConfig rec, AfkConfig afk, RpConfig rp) {

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

    public boolean isRec() {
        return rec.enabled();
    }

    public boolean isAfk() {
        return afk.enabled();
    }

    public boolean isRp() {
        return rp.enabled();
    }

    public boolean isNametagVisible() {
        return display.isNametagVisible();
    }

    private String setPrefixToMessage(String message) {
        return messages.prefix() + message;
    }
}
