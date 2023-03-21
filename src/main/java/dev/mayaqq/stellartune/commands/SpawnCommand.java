package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SpawnCommand {
    public static int spawn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (StellarConfig.CONFIG.spawnCoords[0] == null) {
            player.sendMessage(Text.of("§4No spawnpoint set yet!"), true);
            return 1;
        } else {
            player.teleport(StellarConfig.CONFIG.spawnCoords[0], StellarConfig.CONFIG.spawnCoords[1], StellarConfig.CONFIG.spawnCoords[2]);
            player.sendMessage(Text.of("§bYou §6have been teleported to §bspawn§6!"), true);
        }

        return 1;
    }

    public static int setSpawn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        Integer[] spawnCoords = {player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ()};
        StellarConfig.CONFIG.spawnCoords = spawnCoords;
        StellarConfig.save();
        context.getSource().getPlayer().sendMessage(Text.of("§bYou §6have set the spawn to §b" + player.getBlockPos().getX() + player.getBlockPos().getY() + player.getBlockPos().getZ() + "§6!"), true);

        return 1;
    }
}
