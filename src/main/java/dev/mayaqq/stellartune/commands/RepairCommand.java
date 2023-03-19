package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RepairCommand {
    public static int single(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        ItemStack item = player.getInventory().getStack(player.getInventory().selectedSlot);
        item.setDamage(0);
        player.sendMessage(Text.of("§b" + item.getName().getString() + " §6has been repaired!"), true);

        return 1;
    }

    public static int all(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack item = player.getInventory().getStack(i);
            item.setDamage(0);
            player.sendMessage(Text.of("§bAll your items §6have been repaired!"), true);
        }

        return 1;
    }
}
