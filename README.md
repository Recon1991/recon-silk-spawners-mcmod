# Recon Silk Spawners

A simple mod that allows spawners to be harvested with a Silk Touch pickaxe

---

## Features

- Harvest spawners using Silk Touch
- Preserves full NBT data (mob type, spawn rules, spawn potentials)
- Allows spawners to be placed with retained data
- Full whitelist / blacklist system for entity control
- Restrict placement in specific dimensions
- Optional debug logging of harvested and placed spawners
- Configurable entirely via `reconsilkspawners-common.toml`

---

## Configuration
```toml
enableSilkTouchSpawners = true
enableSpawnerPlacement = true
logSpawnerData = false

restrictedPlacementDimensions = ["minecraft:the_nether", "minecraft:the_end"]

enableWhitelist = false
whitelistEntityIds = ["minecraft:zombie"]

enableBlacklist = false
blacklistEntityIds = ["minecraft:warden"]
```

---

## Supported Versions
- Minecraft 1.21.1
- NeoForge 21.1.172

---

## License
ARR
