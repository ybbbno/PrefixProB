# PrefixPro

PrefixPro is a powerful and lightweight Minecraft plugin that seamlessly integrates with LuckPerms to manage and display player prefixes. With extensive customization options and an advanced AFK system, it enhances your server's visual appearance and player interaction.

## ✨ Features

### 🎯 Core Features
- **LuckPerms Integration:** Automatically displays prefixes from LuckPerms in:
    - Tab list
    - Chat
    - Above-head nametags
- **Real-time Updates:** Instantly applies prefix changes when modified in LuckPerms
- **Configurable Display:** Toggle prefix visibility individually for tab, chat, and nametags

### 🛡️ AFK System
- **Auto-AFK:** Automatically marks players as AFK after 5 minutes (configurable)
- **Disable AFK:** Sneak to get out of afk.
- **AFK Command:** `/afk` to manually toggle AFK status
- **Protection:** AFK players receive god mode and can't be pushed
- **Visual Indicator:** Customizable AFK suffix shows other players who is AFK
- **AFK Message:** A big title on your screen will tell you you are afk.

### ⚙️ Administration
- **Easy Configuration:** Simple config.yml with detailed comments
- **Live Reloading:** Update settings without server restart
    - `/prefixpro config reload` - Reload configuration
    - `/prefixpro plugin reload` - Reload entire plugin
- **Customizable Messages:** Configure all plugin messages with color support

## 🔧 Permissions
- `prefixpro.admin` - Access to admin commands
- `prefixpro.afk` - Access to AFK features
- `prefixpro.version` - Receive update notifications on join

## 📋 Commands
- `/afk` - Toggle AFK status
- `/prefixpro <config|plugin> reload` - Reload configuration or plugin

## 📦 Dependencies
- [LuckPerms](https://luckperms.net/) - Required for prefix management

## ⚡ Performance
- Lightweight and optimized
- No database required
- Minimal resource usage

## 🔍 Configuration

```
# PrefixPro Configuration
config-version: 3

# Display Settings
display:
  tab: true
  chat: true
  nametag: true

# Message Settings
messages:
  prefix: "&8[&6PrefixPro&8] &7"
  join-leave-message:
    enabled: true

# AFK Settings
afk:
  # Time in seconds before auto-AFK (300 = 5 minutes)
  auto-time: 300
  # Suffix that appears when AFK
  suffix: " &7[AFK]"
  # Enable auto-AFK feature
  auto-enabled: true

```


## 🎯 Coming Soon
- Multi-language support
- Placeholder API integration
- Custom prefix commands
- And more!

## 💬 Support
Need help? Found a bug? Have a suggestion? Feel free to:
- Open an issue on our [Github](https://github.com/SamVolvo/PrefixPro) repository
- Join our [Discord](https://discord.gg/BejfuCsSUB) community

Enhance your server's appearance and player interaction with PrefixPro - the complete prefix management solution!