package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
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
    public static int feedPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        players.forEach(FeedCommand::feedPlayer);
        if (players.size() == 1)
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have fed §b" + players.toArray()[0] + " §6!"), true);
        else if (players.size() > 1) {
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have fed §b" + players.size() + " §6players!"), true);
        }
        return 1;
    }

    public static void feedPlayer(ServerPlayerEntity player) {
        player.getHungerManager().setFoodLevel(20);
        player.getHungerManager().setSaturationLevel(20);
    }
}
