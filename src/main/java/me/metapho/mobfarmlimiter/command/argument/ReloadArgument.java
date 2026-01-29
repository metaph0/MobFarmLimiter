package me.metapho.mobfarmlimiter.command.argument;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.metapho.mobfarmlimiter.MobFarmLimiter;

public class ReloadArgument {

    public static LiteralArgumentBuilder<CommandSourceStack> build(MobFarmLimiter plugin) {
        return Commands.literal("reload")
                .requires(ctx -> ctx.getSender().hasPermission("mobfarmlimiter.command.reload"))
                .executes(ctx -> {
                    plugin.reload();
                    ctx.getSource().getSender().sendRichMessage(
                            "<green>MobFarmLimiter " + plugin.getPluginMeta().getVersion() + " reloaded successfully.</green>");
                    return Command.SINGLE_SUCCESS;
                });
    }
}