package me.metapho.mobfarmlimiter.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.metapho.mobfarmlimiter.MobFarmLimiter;
import me.metapho.mobfarmlimiter.command.argument.ReloadArgument;

public class MobFarmLimiterCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand(String root, MobFarmLimiter plugin) {
        return Commands.literal(root)
                .executes(ctx -> {
                    ctx.getSource().getSender().sendRichMessage(
                            "<green>MobFarmLimiter</green> " + plugin.getPluginMeta().getVersion() + "<br>" +
                            "<green>Authors:</green>" + plugin.getPluginMeta().getAuthors());
                    return Command.SINGLE_SUCCESS;
                })
                .then(ReloadArgument.build(plugin))
                .build();
    }
}