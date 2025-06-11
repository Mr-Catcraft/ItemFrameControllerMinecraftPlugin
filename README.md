![Background](https://cdn.modrinth.com/data/2nQ7yAHS/images/02f29fd273ff8328d58c7cd2a9aeba102f25ef82.png)

# ItemFrameController

**ItemFrameController** is a Minecraft plugin for Spigot/Paper 1.16.x - 1.21.1 that provides precise control over player interaction with item frames in specified worlds. It’s ideal for protecting custom builds, museum areas, adventure maps, or simply enhancing server security.

## 🎯 Features

- 🚫 Prevent players from rotating item frames while sneaking.
- 🚫 Block placing items into item frames unless in Creative mode.
- 🛡️ Stop players or entities from breaking item frames.
- 🛠️ Restrict item frame placement.
- 🌍 Limit functionality to specific worlds.
- 💬 Customizable messages for each blocked action.

## ⚙️ Configuration

The plugin uses a YAML configuration file with the following structure:

```yaml
restricted-worlds:
  - world
  - custom_world

prevent-rotation: true
prevent-placing-items: true
prevent-knocking-by-player: true
prevent-knocking-by-entity: true
prevent-placing-frames: true

messages:
  rotation: "§cYou cannot rotate item frames in this world!"
  placing-items: "§cYou cannot place items into item frames here!"
  knocking-by-player: "§cYou cannot break item frames here!"
  knocking-by-entity: "§cEntities cannot destroy item frames here!"
  placing-frames: "§cYou cannot place item frames in this world!"
