package com.samvolvo.prefixPro.config;

public class DefaultsConfig {
    public final static String m_prefix                 = "§c[!] ";
    public final static String m_playerNowAfk           = "§fYou are now AFK";
    public final static String m_playerNoLongerAfk      = "§fYou are no longer AFK";
    public final static String m_playerNoPermission     = "You don't have permission to use this command!";
    public final static String m_playerConsoleOnly      = "This command can only be used by players!";
    public final static String m_commandUsage           = "Usage: /prefixprob reload";
    public final static String m_commandPluginReloaded  = "§aPlugin reloaded!";

    public final static boolean d_tab                   = true;
    public final static boolean d_chat                  = true;
    public final static boolean d_nametag               = false;

    public final static boolean rp_enabled              = true;
    public final static String rp_name                  = "RP";
    public final static String rp_prefix                = "§5[RP] §f";
    public final static int rp_prefixPriority           = 1;
    public final static String rp_suffix                = "";
    public final static int rp_suffixPriority           = -1;

    public final static boolean rec_enabled             = true;
    public final static String rec_name                 = "REC";
    public final static String rec_prefix               = "§4[⏺] §f";
    public final static int rec_prefixPriority          = 0;
    public final static String rec_suffix               = "";
    public final static int rec_suffixPriority          = -1;

    public final static boolean afk_enabled             = true;
    public final static String afk_name                 = "AFK";
    public final static String afk_prefix               = "";
    public final static int afk_prefixPriority          = -1;
    public final static String afk_suffix               = " &7[⌚]";
    public final static int afk_suffixPriority          = 0;
    public final static String afk_screenTitle          = " §cAFK";
    public final static String afk_screenSubtitle       = "§7Sneak To exit AFK";
    public final static boolean afk_auto                = true;
    public final static int afk_time                    = 300;
    public final static int afk_countdown               = 15;
    public final static String afk_actionbarCountdown   = "§7§oYou will become AFK in %s seconds";
}
