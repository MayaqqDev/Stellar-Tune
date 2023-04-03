package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.dataStorage.ServerState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SpawnCommand {
    public static int spawn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        int[] spawnCoords = ServerState.getServerState(player.getServer()).spawnCoords;
        Identifier spawnWorld = new Identifier(ServerState.getServerState(player.getServer()).spawnDimension.split(":")[0], ServerState.getServerState(player.getServer()).spawnDimension.split(":")[1]);
        ServerWorld world = player.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, spawnWorld));
        player.teleport(world, spawnCoords[0], spawnCoords[1], spawnCoords[2], player.getYaw(), player.getPitch());
        player.sendMessage(Text.of("§bYou §6have been teleported to §bspawn§6!"), true);

        return 1;
    }

    public static int setSpawn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        BlockPos spawnPos = player.getBlockPos();
        int[] spawnCoords = new int[]{spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()};
        Identifier spawnWorld = player.getWorld().getRegistryKey().getValue();
        ServerState.getServerState(player.getServer()).spawnCoords = spawnCoords;
        ServerState.getServerState(player.getServer()).spawnDimension = spawnWorld.toString();
        player.sendMessage(Text.of("§bYou §6have set the spawn to §b" + spawnPos.getX() + " " + spawnPos.getY() + " " + spawnPos.getZ() + "§6!"), true);

        return 1;
    }
}
