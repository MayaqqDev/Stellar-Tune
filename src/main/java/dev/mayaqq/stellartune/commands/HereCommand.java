package dev.mayaqq.stellartune.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class HereCommand {
    public static int here(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        //broadcast to all players
        player.getServer().sendMessage(Text.of("§6" + player.getDisplayName() + " is at " + player.getBlockPos().getX() + " " + player.getBlockPos().getY() + " " + player.getBlockPos().getZ() + "!"));
        return 1;
    }
}
