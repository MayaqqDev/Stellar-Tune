package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static dev.mayaqq.stellartune.StellarTune.LOGGER;

public class PowertoyCommand {
    public static int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack item = player.getMainHandStack();
        NbtCompound nbt = item.getOrCreateNbt();
        String commandString = StringArgumentType.getString(context, "command");
        if (commandString.startsWith("/")) {
            player.sendMessage(Text.of("§6Command set to: " + commandString), true);
            nbt.putString("command", commandString);
            LOGGER.info("Command set to: " + item.getNbt().getString("command"));
        } else {
            player.sendMessage(Text.of("§6You have to input a command!"), true);
        }
        return 1;
    }
    public static int remove(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack item = player.getMainHandStack();
        NbtCompound nbt = item.getOrCreateNbt();
        if (nbt.contains("command")) {
            nbt.remove("command");
            player.sendMessage(Text.of("§6Command removed!"), true);
        } else {
            player.sendMessage(Text.of("§6This item doesn't have a command!"), true);
        }
        return 1;
    }
}
