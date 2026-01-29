package me.metapho.mobfarmlimiter;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.metapho.mobfarmlimiter.command.MobFarmLimiterCommand;
import me.metapho.mobfarmlimiter.config.MobFarmLimiterConfig;
import me.metapho.mobfarmlimiter.listener.PreSpawnListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobFarmLimiter extends JavaPlugin {

    private final MobFarmLimiterConfig config = new MobFarmLimiterConfig(this);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config.loadConfig();

        registerEvents();
        registerCommands();
        registerBStats();

        getLogger().info(getPluginMeta().getVersion() + " is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info(getPluginMeta().getVersion() + " is disabled.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PreSpawnListener(config), this);
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(MobFarmLimiterCommand.createCommand("mobfarmlimiter", this), "MobFarmlimiter Command");
            event.registrar().register(MobFarmLimiterCommand.createCommand("mfl", this), "MobFarmlimiter Command Alias");
        });
    }

    private void registerBStats() {
        new Metrics(this, 29095);
    }

    public void reload() {
        this.config.loadConfig();
    }
}
