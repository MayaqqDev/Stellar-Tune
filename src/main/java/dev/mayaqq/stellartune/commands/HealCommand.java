package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class HealCommand {
    public static int heal(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        healPlayer(player);
        context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have been healed!"), true);

        return 1;
    }
    public static int healPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        players.forEach(HealCommand::healPlayer);
        if (players.size() == 1)
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have healed §b" + players.toArray()[0] + " §6!"), true);
        else if (players.size() > 1) {
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have healed §b" + players.size() + " §6players!"), true);
        }
        return 1;
    }

    public static void healPlayer(ServerPlayerEntity player) {
        float maxHealth = player.getMaxHealth();
        player.setHealth(maxHealth);
    }
}
