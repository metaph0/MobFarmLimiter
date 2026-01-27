package me.metapho.mobfarmlimiter.listener;

import com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent;
import me.metapho.mobfarmlimiter.config.MobFarmLimiterConfig;
import me.metapho.mobfarmlimiter.config.MobFarmLimiterConfig.LimitEntry;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class PreSpawnListener implements Listener {
    private static final Logger log = getLogger(PreSpawnListener.class);
    private final MobFarmLimiterConfig config;

    public PreSpawnListener(MobFarmLimiterConfig config) {
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

        if (config.isDebug()) {
            log.info("entity:{} | world:{} | x:{} y:{} z:{} | radius:{} | nearby:{}/{} | reason:{}",
                    event.getType().name(),
                    loc.getWorld().getName(),
                    loc.getBlockX(),
                    loc.getBlockY(),
                    loc.getBlockZ(),
                    entry.radius(),
                    nearbyCount,
                    entry.count(),
                    event.getReason().name());
        }
    }
}