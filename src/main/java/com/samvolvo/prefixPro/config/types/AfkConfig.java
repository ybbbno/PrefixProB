package com.samvolvo.prefixPro.config.types;

public record AfkConfig(
        boolean enabled,
        PlayerConfig config,
        String title,
        String subtitle,
        boolean auto,
        int time,
        int countdown,
        String actionbarCountdown
){ }
