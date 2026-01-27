package me.metapho.mobfarmlimiter.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.metapho.mobfarmlimiter.MobFarmLimiter;

public class MobFarmLimiterCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand(MobFarmLimiter plugin) {
        return Commands.literal("mobfarmlimiter")
                .executes(ctx -> {
                    ctx.getSource().getSender().sendRichMessage("<green>MobFarmLimiter</green> " + plugin.getPluginMeta().getVersion());
                    ctx.getSource().getSender().sendRichMessage("<green>Author:</green> metapho");
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .requires(ctx -> ctx.getSender().hasPermission("mobfarmlimiter.command.reload"))
                        .executes(ctx -> {
                            plugin.reload();
                            ctx.getSource().getSender().sendRichMessage("<green>MobFarmLimiter reloaded successfully.</green>");
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}