# PrefixPro

A lightweight Paper plugin that manages dynamic player prefixes and suffixes for **RP**, **REC**, and **AFK** modes. It integrates with **LuckPerms** and supports tab list, chat, and nametag display (with optional nametag hiding).

## Features

- **RP mode** – Toggle a configurable RP prefix/suffix
- **REC mode** – Toggle a recording indicator prefix/suffix
- **AFK mode** – Toggle AFK status with:
  - Configurable prefix/suffix
  - Auto-AFK after inactivity
  - Countdown when using the command
  - Screen title + subtitle while AFK
  - Invulnerability while AFK
  - Exit AFK by sneaking
- Priority-based stacking of prefixes and suffixes
- Optional display in **tab list**, **chat**, and **nametag**
- LuckPerms prefix/suffix nodes for compatibility with other plugins

## How it works

- Prefixes/suffixes are applied via **scoreboard teams** (nametag + tab) and **LuckPerms** nodes (for other plugins that read LuckPerms prefixes).
- Multiple modes can be active at the same time; they are stacked according to priority.
- AFK players are made invulnerable and cannot move.
- Leaving AFK is done by sneaking (or by moving while sprinting / with velocity).

## Requirements

- Bukkit/Spigot/Paper 1.21+
- [LuckPerms](https://luckperms.net/)

## Commands

| Command            | Description                          | Permission          |
|--------------------|--------------------------------------|---------------------|
| `/rp`              | Toggle RP mode                       | `prefixprob.rp`     |
| `/rec`             | Toggle REC (recording) mode          | `prefixprob.rec`    |
| `/afk`             | Toggle AFK mode (with countdown)     | `prefixprob.afk`    |
| `/prefixprob reload` | Reload the plugin configuration    | `prefixprob.reload` |

## Permissions

| Permission           | Default | Description                     |
|----------------------|---------|---------------------------------|
| `prefixprob.rp`      | op      | Use `/rp`                       |
| `prefixprob.rec`     | op      | Use `/rec`                      |
| `prefixprob.afk`     | op      | Use `/afk` and auto-AFK         |
| `prefixprob.reload`  | op      | Reload the plugin               |

## Configuration

All options live in `config.yml`.

### Messages

```yaml
messages:
  prefix: "§c[!] "
  player-now-afk: "§fYou are now AFK"
  player-no-longer-afk: "§fYou are no longer AFK"
  player-no-permission: "You don't have permission to use this command!"
  player-console-only: "This command can only be used by players!"
  command-usage: "Usage: /prefixprob reload"
  command-plugin-reloaded: "§aPlugin reloaded!"
```

### Display

```yaml
display:
  tab: true                 # Show prefix/suffix in the tab list
  chat: true                # Show prefix/suffix in chat
  is-nametag-visible: false # Hide the player name tag above the head
```

### RP / REC / AFK sections

Each mode has:

- `enabled` – enable/disable the feature
- `prefix` / `suffix` – the text to apply
- `prefix-priority` / `suffix-priority` – lower number = higher priority (appears first)

**AFK** also supports:

| Option                | Description                                      | Default |
|-----------------------|--------------------------------------------------|---------|
| `auto-enabled`        | Automatically set AFK after inactivity           | `true`  |
| `auto-time`           | Seconds of inactivity before auto-AFK            | `300`   |
| `countdown`           | Seconds countdown when using `/afk`              | `15`    |
| `actionbar-countdown` | Actionbar message during countdown (`%s` = time) | …       |
| `screen-title`        | Title shown on screen while AFK                  | `§cAFK` |
| `screen-subtitle`     | Subtitle shown on screen while AFK               | …       |

### Priority example

With the default priorities:

- REC prefix priority `0`
- RP prefix priority `1`
- AFK suffix priority `0`

A player who is both REC + RP + AFK will see:
```
[⏺] [RP] PlayerName [⌚]
```

## License

This project is open source under the MIT License. Feel free to modify and distribute — credit is appreciated but not required.