package me.metapho.mobfarmlimiter.listener;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import me.metapho.mobfarmlimiter.config.MobFarmLimiterConfig;
import me.metapho.mobfarmlimiter.config.MobFarmLimiterConfig.LimitEntry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class EntityLimitListener implements Listener {
    private static final Logger log = getLogger("MobFarmLimiter");
    private final MobFarmLimiterConfig config;

    public EntityLimitListener(MobFarmLimiterConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPreSpawn(PreCreatureSpawnEvent event) {
        LimitEntry entry = config.getLimit(event.getType());
        if (entry == null) return;

        if (!entry.reasons().isEmpty() && !entry.reasons().contains(event.getReason())) {
            return;
        }

        Location loc = event.getSpawnLocation();
        int nearbyCount = loc.getNearbyEntitiesByType(event.getType().getEntityClass(), entry.radius()).size();

        if (nearbyCount < entry.count()) {
            return;
        }

        event.setCancelled(true);
        event.setShouldAbortSpawn(true);

        logDebug(event.getType(), loc, entry, nearbyCount, event.getReason().name());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPlace(EntityPlaceEvent event) {
        LimitEntry entry = config.getLimit(event.getEntityType());
        if (entry == null) return;

        Location loc = event.getBlock().getLocation();
        int nearbyCount = loc.getNearbyEntitiesByType(event.getEntityType().getEntityClass(), entry.radius()).size();

        if (nearbyCount < entry.count()) {
            return;
        }

        event.setCancelled(true);

        logDebug(event.getEntityType(), loc, entry, nearbyCount, "PLAYER_PLACE");
    }

    private void logDebug(EntityType type, Location loc, LimitEntry entry, int nearbyCount, String reason) {
        if (!config.isDebug()) return;

        log.info("\nentity:{} | world:{} | x:{} y:{} z:{} | radius:{} | nearby:{}/{} | reason:{}",
                type.name(),
                loc.getWorld().getName(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ(),
                entry.radius(),
                nearbyCount,
                entry.count(),
                reason);
    }
}