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
        player.sendMessage(Text.of("§bYou §6have been healed!"), true);

        return 1;
    }

    public static int healPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        ServerPlayerEntity player = context.getSource().getPlayer();
        players.forEach(HealCommand::healPlayer);
        StringBuilder sb = new StringBuilder();
        sb.append("§bYou §6have healed §b");
        if (players.size() == 1) {
            sb.append(players.toArray()[0]).append(" §6!");
        }
        else {
            sb.append(players.size()).append(" §6players!");
        }
        player.sendMessage(Text.of(sb.toString()));
        return 1;
    }

    public static void healPlayer(ServerPlayerEntity player) {
        float maxHealth = player.getMaxHealth();
        player.setHealth(maxHealth);
    }
}
