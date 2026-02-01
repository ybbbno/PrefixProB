package com.samvolvo.prefixPro.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility helpers for translating legacy color codes, including modern hex formats,
 * into strings Bukkit can render across scoreboards, chat, and player list entries.
 */
public final class ColorUtil {
    // Matches either &#RRGGBB or #RRGGBB (case-insensitive), optional backslash to escape.
    private static final Pattern HEX_PATTERN = Pattern.compile("(?i)(?<!\\\\)(?:&)?#([0-9a-f]{6})");

    private ColorUtil() {}

    /**
     * Translates ampersand color codes and hex codes into legacy section-prefixed
     * codes that Bukkit understands.
     *
     * @param input raw string containing color markers
     * @return formatted string safe to pass to Bukkit APIs
     */
    public static String colorize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String withHex = applyHexCodes(input);
        return ChatColor.translateAlternateColorCodes('&', withHex);
    }

    /**
     * Returns the trailing color codes (including hex) from a colorized string so they can be
     * applied to following text segments.
     */
    public static String getLastColors(String colorizedText) {
        if (colorizedText == null || colorizedText.isEmpty()) {
            return "";
        }

        // Regex captures either legacy (§a) or hex (§x§r§r§g§g§b§b) color sequences.
        Pattern pattern = Pattern.compile("(§x(§[0-9a-fA-F]){6})|(§[0-9a-fA-Fk-orK-OR])");
        Matcher matcher = pattern.matcher(colorizedText);

        String last = "";
        while (matcher.find()) {
            last = matcher.group();
        }
        return last;
    }

    private static String applyHexCodes(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String replacement = toLegacyHex(hex);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String toLegacyHex(String hex) {
        StringBuilder builder = new StringBuilder()
                .append(ChatColor.COLOR_CHAR)
                .append('x');

        for (char c : hex.toCharArray()) {
            builder.append(ChatColor.COLOR_CHAR).append(Character.toLowerCase(c));
        }

        return builder.toString();
    }
}
