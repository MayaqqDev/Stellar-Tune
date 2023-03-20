package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class FeedCommand {
    public static int feed(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        feedPlayer(player);
        context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have been fed!"), true);

        return 1;
    }
    public static void feedPlayers(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target) {
        target.forEach(FeedCommand::feedPlayer);
        if (target.size() == 1)
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have fed §b" + target.toArray()[0] + " §6!"), true);
        else if (target.size() > 1) {
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have fed §b" + target.size() + " §6players!"), true);
        }

    }

    public static void feedPlayer(ServerPlayerEntity player) {
        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(20);
    }
}
