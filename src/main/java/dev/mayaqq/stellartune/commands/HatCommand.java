package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HatCommand {
    private static final int slot = 3;
    public static int normal(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack item = player.getInventory().getStack(player.getInventory().selectedSlot);
        ItemStack oldItem = player.getInventory().armor.get(slot);
        String message = "§b" + item.getName().getString() + " §6has been put on your head!";
        player.sendMessage(Text.of(message), true);
        player.getInventory().armor.set(slot, item);
        player.getInventory().setStack(player.getInventory().selectedSlot, oldItem);

        return 1;
    }
    public static int item(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ItemStackArgument itemStackArgument = ItemStackArgumentType.getItemStackArgument(context, "item");
        ItemStack item = itemStackArgument.createStack(1, false);
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack oldItem = player.getInventory().armor.get(slot);
        String message = "§b" + item.getName().getString() + " §6has been put on your head!";
        player.sendMessage(Text.of(message), true);
        player.getInventory().armor.set(slot, item);
        player.getInventory().insertStack(oldItem);

        return 1;
    }
}