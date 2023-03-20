package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
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
    public static void healPlayers(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target) {
        target.forEach(HealCommand::healPlayer);
        if (target.size() == 1)
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have healed §b" + target.toArray()[0] + " §6!"), true);
        else if (target.size() > 1) {
            context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have healed §b" + target.size() + " §6players!"), true);
        }

    }

    public static void healPlayer(ServerPlayerEntity player) {
        float maxHealth = player.getMaxHealth();
        player.setHealth(maxHealth);
    }
}
