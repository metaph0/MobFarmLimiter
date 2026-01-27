package me.metapho.mobfarmlimiter.config;

import me.metapho.mobfarmlimiter.MobFarmLimiter;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MobFarmLimiterConfig {
    private final MobFarmLimiter plugin;
    private final Map<EntityType, LimitEntry> limits = new EnumMap<>(EntityType.class);
    private boolean debug = false;

    public MobFarmLimiterConfig(MobFarmLimiter plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        this.limits.clear();
        plugin.reloadConfig();

        final FileConfiguration fileConfig = plugin.getConfig();
        this.debug = fileConfig.getBoolean("debug", false);

        ConfigurationSection section = fileConfig.getConfigurationSection("mob-limits");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            NamespacedKey namespacedKey = NamespacedKey.fromString(key.toLowerCase(Locale.ROOT));
            if (namespacedKey == null) continue;

            EntityType type = Registry.ENTITY_TYPE.get(namespacedKey);
            if (type == null) {
                plugin.getLogger().warning("Cannot find entity type: " + key);
                continue;
            }

            ConfigurationSection entrySection = section.getConfigurationSection(key);
            if (entrySection == null) continue;


            int radius = entrySection.getInt("radius", 16);
            int count = entrySection.getInt("count", 32);
            List<String> reasonStrings = entrySection.getStringList("reasons");
            Set<SpawnReason> reasons;

            if (reasonStrings.isEmpty()) {
                reasons = Collections.emptySet();
            } else {
                reasons = EnumSet.noneOf(SpawnReason.class);
                for (String rs : reasonStrings) {
                    try {
                        reasons.add(SpawnReason.valueOf(rs.toUpperCase().trim()));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid spawn reason (" + key + "): " + rs);
                    }
                }
            }

            this.limits.put(type, new LimitEntry(radius, count, reasons));
        }
    }

    public boolean isDebug() {
        return debug;
    }

    @Nullable
    public LimitEntry getLimit(EntityType type) {
        return limits.get(type);
    }

    public record LimitEntry(int radius, int count, Set<SpawnReason> reasons) {}
}