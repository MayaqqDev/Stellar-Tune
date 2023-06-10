package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.stellartune.config.StellarConfig;
import dev.mayaqq.stellartune.dataStorage.ServerState;
import dev.mayaqq.stellartune.utils.Common;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class HomeCommand {
    public static int setHomeUnnamed(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        if (playerState.homes.isEmpty() && StellarConfig.CONFIG.homeCount > 0) {
            int[] homePos = new int[]{player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ()};
            ServerWorld world = (ServerWorld) player.getWorld();
            playerState.homes.put("home", homePos);
            playerState.homesDimension.put("home", world.getRegistryKey().getValue().toString());
            player.sendMessage(Text.literal("Home set to your current position.").formatted(Formatting.GOLD), true);
        } else {
            if (StellarConfig.CONFIG.homeCount > 1) {
                player.sendMessage(Text.literal("You already have a home set. Use /sethome <name> to set a new one.").formatted(Formatting.RED), true);
            } else {
                player.sendMessage(Text.literal("You already have a home set. Use /removehome to remove it.").formatted(Formatting.RED), true);
            }
        }
        return 0;
    }
    public static int setHome(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);

        if (playerState.homes.size() >= StellarConfig.CONFIG.homeCount) {
            player.sendMessage(Text.literal("You have reached the maximum amount of homes you can set.").formatted(Formatting.RED), true);
            return 1;
        } else {
            String homeName = StringArgumentType.getString(context, "name");
            if (playerState.homes.containsKey(homeName)) {
                player.sendMessage(Text.literal("You already have a home named ").formatted(Formatting.RED).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(" set.").formatted(Formatting.RED)), true);
                return 1;
            }
            int[] homePos = new int[]{player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ()};
            ServerWorld world = (ServerWorld) player.getWorld();
            playerState.homes.put(homeName, homePos);
            playerState.homesDimension.put(homeName, world.getRegistryKey().getValue().toString());
            player.sendMessage(Text.literal("Home ").formatted(Formatting.GOLD).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(" set to your current position.").formatted(Formatting.GOLD)), true);
        }
        return 0;
    }
    public static int removeHomeUnnamed(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        if (playerState.homes.isEmpty()) {
            player.sendMessage(Text.literal("You don't have a home set.").formatted(Formatting.RED), true);
        } else if (playerState.homes.size() == 1) {
            playerState.homes.clear();
            playerState.homesDimension.clear();
            player.sendMessage(Text.literal("Home removed.").formatted(Formatting.GOLD), true);
        } else {
            player.sendMessage(Text.literal("You have multiple homes set. Use /removehome <name> to remove a specific one.").formatted(Formatting.RED), true);
        }
        return 0;
    }
    public static int removeHome(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        String homeName = StringArgumentType.getString(context, "name");
        if (playerState.homes.containsKey(homeName)) {
            playerState.homes.remove(homeName);
            playerState.homesDimension.remove(homeName);
            player.sendMessage(Text.literal("Home ").formatted(Formatting.GOLD).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(" removed.").formatted(Formatting.GOLD)), true);
        } else {
            player.sendMessage(Text.literal("You don't have a home named ").formatted(Formatting.RED).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(" set.").formatted(Formatting.RED)), true);
        }
        return 0;
    }
    public static int homeUnnamed(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        long lastTeleport = playerState.lastHomeUse;
        long nextUseTimestamp = lastTeleport + (StellarConfig.CONFIG.homeCount * 1000L);
        if (nextUseTimestamp > System.currentTimeMillis()) {
            Common.calcCooldown(lastTeleport, StellarConfig.CONFIG.homeCount);
            player.sendMessage(Text.of("§cYou have to wait §6" + Common.calcCooldown(lastTeleport, StellarConfig.CONFIG.homeCount) + "§c before using this command again!"), true);
            return 1;
        }

        if (playerState.homes.isEmpty()) {
            player.sendMessage(Text.literal("You don't have a home set.").formatted(Formatting.RED), true);
        } else if (playerState.homes.size() == 1) {
            String homeName = playerState.homes.keySet().iterator().next();
            int[] homePos = playerState.homes.get(homeName);
            Identifier homeDimension = new Identifier(playerState.homesDimension.get(homeName).split(":")[0], playerState.homesDimension.get(homeName).split(":")[1]);
            ServerWorld world = player.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, homeDimension));
            player.teleport(world, homePos[0], homePos[1], homePos[2], player.getYaw(), player.getPitch());
            playerState.lastHomeUse = System.currentTimeMillis();
            player.sendMessage(Text.literal("Teleported to home ").formatted(Formatting.GOLD).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(".").formatted(Formatting.GOLD)), true);
        } else {
            player.sendMessage(Text.literal("You have multiple homes set. Use /home <name> to teleport to a specific one.").formatted(Formatting.RED), true);
        }
        return 0;
    }
    public static int home(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        long lastTeleport = playerState.lastHomeUse;
        long nextUseTimestamp = lastTeleport + (StellarConfig.CONFIG.homeCount * 1000L);
        if (nextUseTimestamp > System.currentTimeMillis()) {
            // convert remainingCooldown, which is in seconds to hours, minutes, and seconds accordingly
            player.sendMessage(Text.of("§cYou have to wait §6" + Common.calcCooldown(lastTeleport, StellarConfig.CONFIG.homeCount) + "§c before using this command again!"), true);
            return 1;
        }
        String homeName = StringArgumentType.getString(context, "name");
        if (playerState.homes.containsKey(homeName)) {
            int[] homePos = playerState.homes.get(homeName);
            Identifier homeDimension = new Identifier(playerState.homesDimension.get(homeName).split(":")[0], playerState.homesDimension.get(homeName).split(":")[1]);
            ServerWorld world = player.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, homeDimension));
            player.teleport(world, homePos[0], homePos[1], homePos[2], player.getYaw(), player.getPitch());
            playerState.lastHomeUse = System.currentTimeMillis();
            player.sendMessage(Text.literal("Teleported to home ").formatted(Formatting.GOLD).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(".").formatted(Formatting.GOLD)), true);
        } else {
            player.sendMessage(Text.literal("You don't have a home named ").formatted(Formatting.RED).append(Text.literal(homeName).formatted(Formatting.AQUA)).append(Text.literal(" set.").formatted(Formatting.RED)), true);
        }
        return 0;
    }

    public static int getHomes(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = getPlayer(context);
        ServerState.PlayerState playerState = ServerState.getPlayerState(player);
        if (playerState.homes.isEmpty()) {
            player.sendMessage(Text.literal("You don't have any homes set.").formatted(Formatting.RED), true);
        } else {
            StringBuilder homes = new StringBuilder();
            for (String homeName : playerState.homes.keySet()) {
                homes.append(homeName + ", ");
            }
            homes.deleteCharAt(homes.length() - 2);
            player.sendMessage(Text.literal("You have the following homes set: ").formatted(Formatting.GOLD).append(Text.literal(homes.toString()).formatted(Formatting.AQUA)), false);
        }
        return 0;
    }

    public static ServerPlayerEntity getPlayer(CommandContext<ServerCommandSource> context) {
        return context.getSource().getPlayer();
    }
}