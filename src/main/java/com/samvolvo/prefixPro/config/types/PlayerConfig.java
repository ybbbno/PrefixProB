package com.samvolvo.prefixPro.config.types;

public record PlayerConfig(
        String name,
        String prefix,
        int prefixPriority,
        String suffix,
        int suffixPriority
) {}