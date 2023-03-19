package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class GamemodeCommands {
    public static int gmc(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.changeGameMode(GameMode.CREATIVE);
        player.sendMessage(Text.of("§6Game mode changed to §bcreative§6!"), true);
        return 1;
    }
    public static int gms(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.changeGameMode(GameMode.SURVIVAL);
        player.sendMessage(Text.of("§6Game mode changed to §bsurvival§6!"), true);
        return 1;
    }
    public static int gma(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.changeGameMode(GameMode.ADVENTURE);
        player.sendMessage(Text.of("§6Game mode changed to §badventure§6!"), true);
        return 1;
    }
    public static int gmsp(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        player.changeGameMode(GameMode.SPECTATOR);
        player.sendMessage(Text.of("§6Game mode changed to §bspectator§6!"), true);
        return 1;
    }
}