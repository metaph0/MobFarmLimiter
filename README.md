# Mob Farm Limiter

Mob Farm Limiter was designed to control farms that bypass vanilla mob caps. It provides full flexibility, allowing users to configure and manage specific mob farm limits via `config.yml` according to their needs.

## Features
Unlike other plugins that periodically delete existing mobs, Mob Farm Limiter proactively cancels mob spawn events before they occur. It restricts spawns based on:
- **Entity Types**: Specify exactly which mobs to limit.
- **Radius**: Set the distance (in blocks) to scan for nearby entities.
- **Counts**: Define the maximum number of entities allowed within that radius.
- **Spawn Reasons**: Target specific spawning methods (e.g., `NATURAL`, `NETHER_PORTAL`).

## Configuration
These are default values; you can add as many sections as needed to the configuration.

```
mob-limits:
  # The entity type to apply the limit
  # https://jd.papermc.io/paper/1.21.11/org/bukkit/entity/EntityType.html
  pillager:
    # Radius (in blocks) to check for nearby entities
    radius: 36
    # Maximum number of entities allowed within the radius
    count: 100
    # List of spawn reasons to apply this limit
    # Reference: https://jd.papermc.io/paper/1.21.11/org/bukkit/event/entity/CreatureSpawnEvent.SpawnReason.html
    # Multiple spawn reasons can be specified.
    # If empty [], all spawn reasons are applied.
    reasons:
      - NATURAL
  zombified_piglin:
    radius: 50
    count: 100
    reasons:
      - NETHER_PORTAL
```

## Commands
- `/mobfarmlimiter reload`: Reloads the configuration file.

## Permissions
- `mobfarmlimiter.command.reload`: Allows use of the reload command. (Default: OP)
